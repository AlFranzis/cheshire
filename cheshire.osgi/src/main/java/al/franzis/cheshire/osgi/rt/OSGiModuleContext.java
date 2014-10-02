package al.franzis.cheshire.osgi.rt;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import al.franzis.cheshire.api.IModule;
import al.franzis.cheshire.api.IModuleContext;
import al.franzis.cheshire.api.service.IServiceReference;

public class OSGiModuleContext implements IModuleContext {
	private final BundleContext bundleContext;
	private final IModule module;
	
	public OSGiModuleContext( BundleContext bundleContext ) {
		this.bundleContext = bundleContext;
		this.module = new OSGiModule( this, bundleContext.getBundle() );
	}

	public IModule getModule() {
		return module;
	}

	public IModule getModule(String name) {
		return OSGiModuleFramework.getInstance().getModule(name);
	}

	public List<IModule> getModules() {
		return OSGiModuleFramework.getInstance().getModules();
	}

	@Override
	public <S> void registerService(Class<S> serviceClass, S service) {
		bundleContext.registerService(serviceClass, service, null);
	}

	@Override
	public <S> IServiceReference<S> getServiceReference(Class<S> clazz) {
		ServiceReference osgiServiceRef = bundleContext.getServiceReference(clazz.getCanonicalName());
		return new OSGiServiceReference<S>(osgiServiceRef);
	}

	@Override
	public <S> Collection<IServiceReference<S>> getServiceReferences(Class<S> clazz) {
		try {
			List<IServiceReference<S>> serviceReferences = new LinkedList<>();
			for (ServiceReference osgiServiceRef : bundleContext.getServiceReferences(clazz.getCanonicalName(), null)) {
				IServiceReference<S> serviceRef = new OSGiServiceReference<>(osgiServiceRef);
				serviceReferences.add(serviceRef);
			}
			
			return serviceReferences;
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <S> S getService(IServiceReference<S> serviceReference) {
		ServiceReference<S> osgiServiceRef = getOSGiServiceRef(serviceReference);
		return bundleContext.getService(osgiServiceRef);
	}

	@Override
	public void ungetService(IServiceReference<? extends Object> serviceReference) {
		ServiceReference<?> osgiServiceRef = getOSGiServiceRef(serviceReference);
		bundleContext.ungetService(osgiServiceRef);
	}
	
	private static <S> ServiceReference<S> getOSGiServiceRef(IServiceReference<S> serviceReference) {
		if(!(serviceReference instanceof OSGiServiceReference<?>))
			throw new IllegalArgumentException( "Expected OSGi service reference type, but was: " + serviceReference.getClass().toString());
		
		OSGiServiceReference<S> serviceRef = (OSGiServiceReference<S>)serviceReference;
		ServiceReference<S> osgiServiceRef = serviceRef.getOSGiServiceReference();
		return osgiServiceRef;
	}
	
}
