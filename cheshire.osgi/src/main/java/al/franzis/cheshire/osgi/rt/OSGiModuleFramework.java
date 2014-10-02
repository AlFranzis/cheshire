package al.franzis.cheshire.osgi.rt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import al.franzis.cheshire.api.IModule;

public class OSGiModuleFramework {
	private static OSGiModuleFramework INSTANCE = new OSGiModuleFramework();
	
	private final Map<Bundle,IModule> modules = new HashMap<>();
	
	public static OSGiModuleFramework getInstance() {
		return INSTANCE;
	}
	
	public IModule getOrCreateModule(BundleContext bundleContext) {
		return getOrCreateModule(bundleContext.getBundle());
	}
	
	public IModule getOrCreateModule(Bundle bundle) {
		IModule module = modules.get(bundle);
		if (module == null) {
			module = new OSGiModuleContext(bundle.getBundleContext()).getModule();
			modules.put(bundle, module);
		}
		
		return module;
	}
	
	public IModule getModule(String name) {
		return null;
	}
	
	public List<IModule> getModules() {
		return null;
	}
	
}
