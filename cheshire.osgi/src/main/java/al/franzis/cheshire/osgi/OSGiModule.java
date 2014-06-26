package al.franzis.cheshire.osgi;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

import al.franzis.cheshire.IModule;

public class OSGiModule implements IModule {
	private final Bundle bundle;
	
	public OSGiModule( Bundle bundle ) {
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
	
	
}
