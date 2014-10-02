package al.franzis.cheshire.test.osgi.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import al.franzis.cheshire.api.IModuleContext;
import al.franzis.cheshire.api.service.IServiceReference;
import al.franzis.cheshire.test.osgi.activator.ModuleActivatorExample;

public class OSGiServiceTest {

	@Test
	public void testOSGiServiceAPI() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		IServiceReference<IPluginManager> serviceRef = moduleContext.getServiceReference(IPluginManager.class);
		IPluginManager pluginManager = moduleContext.getService(serviceRef);
		List<IPlugin> plugins = pluginManager.getPlugins();
		System.out.println("Plugins known to PluginManager: " + plugins);
		Assert.assertEquals(2, plugins.size());
		
		// check that module activator was 1. started and 2. resource was loaded correctly
		Assert.assertNotNull(ModuleActivatorExample.loadedResource); 
	}
}
