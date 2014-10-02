package al.franzis.cheshire.cdi.rt;

import al.franzis.cheshire.api.service.IServiceReference;

public class CDIServiceReference<S> implements IServiceReference<S> {
	private final S serviceInstance;
	
	public CDIServiceReference( S serviceInstance ) {
		this.serviceInstance = serviceInstance;
	}
	
	public S getService() {
		return serviceInstance;
	}
}
