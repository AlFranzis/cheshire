package al.franzis.cheshire.cdi;

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

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;

@Singleton
public class CDIModuleFramework {
	private Map<ClassLoader,IModuleContext> contextMap = new HashMap<>();
	
	@Inject
	private CDIServiceFactory serviceFactory;
	
	@Inject
	private BeanManager beanManager;
	
	public CDIModuleFramework() {
		System.out.println("Module framework instance created");
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
		
		System.out.println("Starting module framework");
		Set<Bean<?>> manifestBeans = beanManager.getBeans(ICDIModuleManifest.class);
		for(Bean<?> manifestBean : manifestBeans) {
			createBeanInstance(manifestBean);
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
		System.out.println("Module framework started");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void injectModuleContext( Object activator, IModuleContext moduleContext ) {
		try {
			Class<?> activatorClass = activator.getClass();
			Method moduleContextMethod = Helpers.getAnnotatedMethod(activatorClass, ModuleContextMethod.class);
			if ( moduleContextMethod != null )
				moduleContextMethod.invoke(activator, moduleContext);
			else
				System.out.println( String.format( "Module Activator class %s does not contain module context setter-method (@ModuleContextMethod)", activatorClass.getSimpleName() ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
