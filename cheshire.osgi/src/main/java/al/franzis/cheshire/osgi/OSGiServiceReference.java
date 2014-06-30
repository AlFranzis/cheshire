package al.franzis.cheshire.osgi;

import org.osgi.framework.ServiceReference;

import al.franzis.cheshire.service.IServiceReference;

public class OSGiServiceReference<S> implements IServiceReference<S> {
	private final ServiceReference<S> osgiServiceReference;

	public OSGiServiceReference(ServiceReference<S> osgiServiceReference) {
		this.osgiServiceReference = osgiServiceReference;
	}
	
	public ServiceReference<S> getOSGiServiceReference() {
		return osgiServiceReference;
	}
	
}
