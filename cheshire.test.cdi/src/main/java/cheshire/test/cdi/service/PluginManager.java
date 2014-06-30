package cheshire.test.cdi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIModuleFramework;
import al.franzis.cheshire.service.IServiceContext;
import al.franzis.cheshire.service.ServiceActivationMethod;
import al.franzis.cheshire.service.ServiceBindMethod;

public class PluginManager implements IPluginManager {
	private CDIModuleFramework moduleFramework;
	private List<IPlugin> plugins = new ArrayList<IPlugin>();
	
	public PluginManager() {
		System.out.println("PluginManager created");
	}
	
	@Inject
	private void setModuleFramework(CDIModuleFramework moduleFramework) {
		this.moduleFramework = moduleFramework;
	}
	
	// GEN
	@PostConstruct
	private void init() {
		moduleFramework.registerCDIService(this);
	}
	
	// GEN
	@Inject
	private void setInstances(Instance<IPlugin> plugins) {
		Iterator<IPlugin> it = plugins.iterator();
		while(it.hasNext()) {
			addPlugin( it.next() );
		}
	}
	
	@ServiceActivationMethod
	public void activate( IServiceContext serviceContext ) {
		System.out.println("PluginManager.activate() called" );
		System.out.println("PluginManager service properties: " + serviceContext.getProperties());
	}
	
	@ServiceBindMethod
	public void addPlugin( IPlugin plugin ) {
		this.plugins.add( plugin );
	}
	
	@Override
	public List<IPlugin> getPlugins() {
		return plugins;
	}

}
