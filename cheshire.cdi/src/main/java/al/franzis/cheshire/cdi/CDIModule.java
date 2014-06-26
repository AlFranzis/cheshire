package al.franzis.cheshire.cdi;

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import al.franzis.cheshire.IModule;

public class CDIModule implements IModule {

	public URL getResource(String name) {
		return this.getClass().getResource(name);
	}

	public Enumeration<URL> getResources(String name) {
		Vector<URL> resourceUrls = new Vector<>();
		URL url = getResource(name);
		if(url != null)
			resourceUrls.add(url);
		return resourceUrls.elements();
	}

	public String getName() {
		return "CDIModule";
	}

}
