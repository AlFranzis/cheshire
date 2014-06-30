package al.franzis.cheshire.cdi;

import al.franzis.cheshire.service.IServiceReference;

public class CDIServiceReference<S> implements IServiceReference<S> {
	private final S serviceInstance;
	
	public CDIServiceReference( S serviceInstance ) {
		this.serviceInstance = serviceInstance;
	}
	
	public S getService() {
		return serviceInstance;
	}
}
