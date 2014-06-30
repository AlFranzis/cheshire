package al.franzis.cheshire.cdi;

import java.util.Map;

import al.franzis.cheshire.service.IServiceContext;

public class CDIServiceContext implements IServiceContext {
	private final Map<String,String> serviceProperties;
	
	public CDIServiceContext( Map<String,String> serviceProperties ) {
		this.serviceProperties = serviceProperties;
	}
	
	@Override
	public Map<String, String> getProperties() {
		return serviceProperties;
	}

}
