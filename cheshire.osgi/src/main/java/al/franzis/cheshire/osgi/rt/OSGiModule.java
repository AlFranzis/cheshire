package al.franzis.cheshire.osgi.rt;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

import al.franzis.cheshire.api.IModule;
import al.franzis.cheshire.api.IModuleContext;

public class OSGiModule implements IModule {
	private final Bundle bundle;
	private final IModuleContext moduleContext;
	
	public OSGiModule( IModuleContext moduleContext, Bundle bundle ) {
		this.moduleContext = moduleContext;
		this.bundle = bundle;
	}

	public URL getResource(String name) {
		return bundle.getResource( name );
	}

	public Enumeration<URL> getResources(String name) {
		try {
			return bundle.getResources( name );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getName() {
		return bundle.getSymbolicName();
	}

	@Override
	public IModuleContext getModuleContext() {
		return moduleContext;
	}
	
	
}
