package cheshire.test.cdi.helloworld;

import java.util.List;



import org.jboss.weld.environment.se.StartMain;
import org.junit.Assert;
import org.junit.Test;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;
import cheshire.test.cdi.activator.ModuleActivatorExample;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;

public class CDIModuleTest {
	
	@Test
	public void testCDIModule() {
		StartMain.main( null );
		usePluginService();
	}
	
	private static void usePluginService() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		IServiceReference<IPluginManager> serviceRef = moduleContext.getServiceReference(IPluginManager.class);
		IPluginManager pluginManager = moduleContext.getService(serviceRef);
		List<IPlugin> plugins = pluginManager.getPlugins();
		System.out.println("Plugins known to PluginManager: " + plugins);
		Assert.assertEquals(2, plugins.size());
		
		Assert.assertNotNull(ModuleActivatorExample.loadedResource);
	}
	
}
