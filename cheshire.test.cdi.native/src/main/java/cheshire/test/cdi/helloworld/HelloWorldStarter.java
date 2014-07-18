package cheshire.test.cdi.helloworld;

import org.jboss.weld.environment.se.StartMain;

import al.franzis.cheshire.IModuleContext;
import cheshire.test.cdi.activator.ModuleActivatorExample;

public class HelloWorldStarter {
	
	public static void main(String[] args) {
		StartMain.main(args);
		usePluginService();
	}
	

	private static void usePluginService() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		
		
	}
	
}
