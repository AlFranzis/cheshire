package al.franzis.cheshire.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.service.IServiceContext;

@Singleton
public class CDIModuleFramework {
	private Map<ClassLoader,IModuleContext> contextMap = new HashMap<>();
	private IServiceContext serviceContext;
	
	private List<Object> services = new LinkedList<>();
	
	@Inject
	private BeanManager beanManager;
	
	public CDIModuleFramework() {
		System.out.println("Module framework instance created");
	}
	
	@Produces
	public IModuleContext getModuleContext(InjectionPoint injectionPoint) {
		Class<?> targetClass = injectionPoint.getMember().getDeclaringClass();
		ClassLoader targetLoader = targetClass.getClassLoader();
		IModuleContext moduleCxt = contextMap.get(targetLoader);
		if (moduleCxt == null) {
			moduleCxt = new CDIModuleContext();
			contextMap.put(targetLoader, moduleCxt);
		}
		
		return moduleCxt;
	}

	@Produces
	public IServiceContext getServiceContext() {
		if ( serviceContext == null )
			serviceContext = new CDIServiceContext();
		return serviceContext;
	}
	
	public void start(@Observes ContainerInitialized event) {
		System.out.println("Starting module framework");
		Set<Bean<?>> beans = beanManager.getBeans(ICDIModuleActivator.class);
		for ( Bean<?> bean : beans ) {
			Object activator = createActivator(bean);
			startActivator(activator);
		}
		System.out.println("Module framework started");
	}
	
	public void registerCDIService(@Observes CDIServiceEvent serviceEvent) {
		System.out.println("Register service: " + serviceEvent.getService());
		services.add(serviceEvent.getService());
	}
	
	private Object createActivator( Bean<?> bean ) {
		CreationalContext ctx = beanManager.createCreationalContext(null);
		Object activatorInstance = bean.create(ctx);
		return activatorInstance;
	}
	
	private void startActivator( Object activator ) {
		try {
			Method startMethod = getAnnotatedMethod(activator.getClass(),
					ModuleStartMethod.class);
			startMethod.invoke(activator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Method getAnnotatedMethod( Class<?> annotatedClass, Class<? extends Annotation> annotation ) {
		for(Method m : annotatedClass.getDeclaredMethods()) {
			if(m.isAnnotationPresent(annotation))
				return m;
		}
		return null;
	}
	
}
