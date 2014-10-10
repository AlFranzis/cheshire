package al.franzis.cheshire.osgi.rt;

import org.osgi.service.component.ComponentFactory;

import al.franzis.cheshire.api.service.IServiceFactory;

public class OSGiServiceFactory implements IServiceFactory {
	private final ComponentFactory osgiComponentFactory;
	
	public OSGiServiceFactory(ComponentFactory osgiComponentFactory) {
		this.osgiComponentFactory = osgiComponentFactory;
	}
	
	@Override
	public Object newInstance() {
		return osgiComponentFactory.newInstance(null).getInstance();
	}

}
