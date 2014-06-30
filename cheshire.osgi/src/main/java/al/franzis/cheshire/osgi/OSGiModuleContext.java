package al.franzis.cheshire.osgi;

import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;

public class OSGiModuleContext implements IModuleContext {
	private final BundleContext bundleContext;
	private final IModule module;
	
	public OSGiModuleContext( BundleContext bundleContext ) {
		this.bundleContext = bundleContext;
		this.module = new OSGiModule( bundleContext.getBundle() );
	}

	public IModule getModule() {
		return module;
	}

	public IModule getModule(String name) {
		return null;
	}

	public List<IModule> getModules() {
		return null;
	}

	@Override
	public <S> void registerService(Class<S> serviceClass, S service) {
	}

	@Override
	public <S> IServiceReference<S> getServiceReference(Class<S> clazz) {
		return null;
	}

	@Override
	public <S> Collection<IServiceReference<S>> getServiceReferences(Class<S> clazz) {
		return null;
	}

	@Override
	public <S> S getService(IServiceReference<S> serviceReference) {
		return null;
	}

	@Override
	public void ungetService(IServiceReference<? extends Object> serviceReference) {
	}
	
}
