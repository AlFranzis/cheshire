package al.franzis.cheshire.osgi;

import java.util.List;

import org.osgi.framework.BundleContext;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;

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
	
}
