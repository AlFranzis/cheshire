package al.franzis.cheshire.test.osgi.service;

import java.util.ArrayList;
import java.util.List;

import al.franzis.cheshire.service.IServiceContext;
import al.franzis.cheshire.service.ServiceActivationMethod;
import al.franzis.cheshire.service.ServiceBindMethod;

public class PluginManager implements IPluginManager {
	private List<IPlugin> plugins = new ArrayList<IPlugin>();
	
	public PluginManager() {
		System.out.println("PluginManager created");
	}
	
	@ServiceActivationMethod
	public void activate(IServiceContext serviceContext) {
		System.out.println("PluginManager.activate() called" );
		System.out.println("PluginManager service properties: " + serviceContext.getProperties());
	}
	
	@ServiceBindMethod
	public void addPlugin(IPlugin plugin) {
		this.plugins.add( plugin );
	}
	
	@Override
	public List<IPlugin> getPlugins() {
		return plugins;
	}

}
