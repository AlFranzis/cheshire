package cheshire.test.cdi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIServiceEvent;
import al.franzis.cheshire.service.IServiceContext;

public class PluginManager implements IPluginManager {
	private IServiceContext serviceContext;
	private Event<CDIServiceEvent> event;
	private List<IPlugin> plugins = new ArrayList<IPlugin>();
	
	public PluginManager() {
		System.out.println("PluginManager created");
	}
	
	@Inject
	public void setEventBus( Event<CDIServiceEvent> event) {
		this.event = event;
	}
	
	@PostConstruct
	public void init() {
		event.fire(new CDIServiceEvent(this));
	}
	
	@Inject
	public void setInstances(Instance<IPlugin> plugins) {
		Iterator<IPlugin> it = plugins.iterator();
		while(it.hasNext()) {
			addPlugin( it.next() );
		}
	}
	
	@Inject
	public void activate( IServiceContext serviceContext ) {
		this.serviceContext = serviceContext;
	}
	
	public void addPlugin( IPlugin plugin ) {
		this.plugins.add( plugin );
	}
	
	@Override
	public List<IPlugin> getPlugins() {
		return plugins;
	}

}
