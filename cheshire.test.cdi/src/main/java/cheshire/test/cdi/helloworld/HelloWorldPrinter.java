package cheshire.test.cdi.helloworld;


import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;
import cheshire.test.cdi.activator.ModuleActivatorExample;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;


public class HelloWorldPrinter {
	
	@Inject
	private HelloWorldPojo pojo;
	
//	@Inject
//	private IPluginManager pluginManager;
	
	public void printHello(@Observes ContainerInitialized event,
			@Parameters List<String> parameters) {
		System.out.println(pojo.sayHello());
		
		
	}
	
}
