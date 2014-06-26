package al.franzis.cheshire.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleStartMethod;

@Singleton
public class CDIModuleFramework {
	private static IModuleContext moduleContext;
	
	@Inject
	private BeanManager beanManager;
	
	public CDIModuleFramework() {
		System.out.println("Module framework instance created");
	}
	
	@Produces
	public IModuleContext getModuleContext() {
		if ( moduleContext == null )
			moduleContext = new CDIModuleContext();
		return moduleContext;
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
