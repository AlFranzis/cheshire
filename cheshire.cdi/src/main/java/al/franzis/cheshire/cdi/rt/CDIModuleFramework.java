package al.franzis.cheshire.cdi.rt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import al.franzis.cheshire.api.IModuleContext;
import al.franzis.cheshire.api.ModuleContextMethod;
import al.franzis.cheshire.api.ModuleStartMethod;

@Singleton
public class CDIModuleFramework {
	private Map<ClassLoader,IModuleContext> contextMap = new HashMap<>();
	
	@Inject
	private CDIServiceFactory serviceFactory;
	
	@Inject
	private BeanManager beanManager;
	
	public CDIModuleFramework() {
		RuntimeLogger.getInstance().info("Module framework instance created");
	}
	
	public CDIServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	protected IModuleContext getModuleContext(Object instance) {
		Class<?> targetClass = instance.getClass();
		ClassLoader targetLoader = targetClass.getClassLoader();
		IModuleContext moduleCxt = contextMap.get(targetLoader);
		if (moduleCxt == null) {
			moduleCxt = new CDIModuleContext( this );
			contextMap.put(targetLoader, moduleCxt);
		}
		
		return moduleCxt;
	}
	
	public void start(@Observes Object event) {
		/*
		 * Ugly: We are only interested in beeing notified about container-initialization
		 * - @Observes Object -> we observe every event -> potential performance hit
		 * - @Observes: org.jboss.weld.environment.se.events.ContainerInitialized -> Dependency to Weld implementation
		 */
		if ( !event.getClass().getName().equals("org.jboss.weld.environment.se.events.ContainerInitialized") )
			return;
		
		start();
	}
	
	public void start() {
		RuntimeLogger.getInstance().info("Starting module framework -- START");
		Set<Bean<?>> manifestBeans = beanManager.getBeans(ICDIModuleManifest.class);
		for(Bean<?> manifestBean : manifestBeans) {
			ICDIModuleManifest manifestInstance = (ICDIModuleManifest)createBeanInstance(manifestBean);
			RuntimeLogger.getInstance().info("Created Module Manifest instance for module: " + manifestInstance.getBundleName());
		}
		
		Set<Bean<?>> activatorBeans = beanManager.getBeans(ICDIModuleActivator.class);
		for (Bean<?> activatorBean : activatorBeans) {
			// 1) create activator
			Object activator = createBeanInstance(activatorBean);
			// 2) inject module context
			IModuleContext moduleContext = getModuleContext(activator);
			injectModuleContext(activator, moduleContext);
			// 3) start activator
			startActivator(activator);
		}
		RuntimeLogger.getInstance().info("Starting module framework -- FINISHED");
	}
	
	public void registerCDIService(Object serviceInstance) {
		serviceFactory.registerService(serviceInstance);
	}
	
	private Object createBeanInstance( Bean<?> bean ) {
		CreationalContext ctx = beanManager.createCreationalContext(null);
		Object beanInstance = bean.create(ctx);
		return beanInstance;
	}
	
	private void startActivator( Object activator ) {
		try {
			Method startMethod = Helpers.getAnnotatedMethod(activator.getClass(), ModuleStartMethod.class);
			startMethod.invoke(activator);
			RuntimeLogger.getInstance().info("Started Module Activator: " + activator.getClass().getCanonicalName());
		} catch (Exception e) {
			throw new RuntimeException("Error while starting Module Activator", e);
		}
	}
	
	private void injectModuleContext( Object activator, IModuleContext moduleContext ) {
		try {
			Class<?> activatorClass = activator.getClass();
			Method moduleContextMethod = Helpers.getAnnotatedMethod(activatorClass, ModuleContextMethod.class);
			if ( moduleContextMethod != null )
				moduleContextMethod.invoke(activator, moduleContext);
			else
				RuntimeLogger.getInstance().info( String.format( "Module Activator class %s does not contain module context setter-method (@ModuleContextMethod)", activatorClass.getSimpleName() ) );
		} catch (Exception e) {
			throw new RuntimeException("Error while injecting Module Context into Module Activator", e);
		}
	}
	
}
