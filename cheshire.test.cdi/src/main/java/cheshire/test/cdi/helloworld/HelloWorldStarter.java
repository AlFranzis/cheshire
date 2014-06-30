package cheshire.test.cdi.helloworld;

import java.util.List;

import org.jboss.weld.environment.se.StartMain;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;
import cheshire.test.cdi.activator.ModuleActivatorExample;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;

public class HelloWorldStarter {
	
	public static void main(String[] args) {
		StartMain.main(args);
		usePluginService();
	}
	

	private static void usePluginService() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		IServiceReference<IPluginManager> serviceRef = moduleContext.getServiceReference(IPluginManager.class);
		IPluginManager pluginManager = moduleContext.getService(serviceRef);
		List<IPlugin> plugins = pluginManager.getPlugins();
		System.out.println("Plugins known to PluginManager: " + plugins);
		
	}
	
}
