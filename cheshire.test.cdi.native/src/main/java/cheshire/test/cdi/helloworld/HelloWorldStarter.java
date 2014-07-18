package cheshire.test.cdi.helloworld;

import org.jboss.weld.environment.se.StartMain;

import al.franzis.cheshire.IModuleContext;
import cheshire.test.cdi.activator.ModuleActivatorExample;
import cheshire.test.cdi.nat.JNIHelloWorld;

public class HelloWorldStarter {
	
	public static void main(String[] args) {
		StartMain.main(args);
		usePluginService();
	}
	

	private static void usePluginService() {
		JNIHelloWorld hw = new JNIHelloWorld();
		hw.sayHello();
		
	}
	
}
