package cheshire.test.cdi.helloworld;


import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;


public class HelloWorldPrinter {
	
	@Inject
	private HelloWorldPojo pojo;
	
	public void printHello(@Observes ContainerInitialized event, @Parameters List<String> parameters) {
		System.out.println(pojo.sayHello());
	}
	
}
