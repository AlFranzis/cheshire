package cheshire.test.cdi.helloworld;

import java.util.List;






import org.jboss.weld.environment.se.StartMain;
import org.junit.Assert;
import org.junit.Test;

import al.franzis.cheshire.api.IModuleContext;
import al.franzis.cheshire.api.service.IServiceReference;
import cheshire.test.cdi.activator.ModuleActivatorExample;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;
import cheshire.test.cdi.service.factory.IOverlay;
import cheshire.test.cdi.service.factory.IOverlayProvider;

public class CDIModuleTest {
	
	@Test
	public void testCDIModule() {
		StartMain.main( null );
		usePluginService();
		useOverlayService();
	}
	
	private static void usePluginService() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		IServiceReference<IPluginManager> serviceRef = moduleContext.getServiceReference(IPluginManager.class);
		IPluginManager pluginManager = moduleContext.getService(serviceRef);
		List<IPlugin> plugins = pluginManager.getPlugins();
		System.out.println("Plugins known to PluginManager: " + plugins);
		Assert.assertEquals(3, plugins.size());
		
		Assert.assertNotNull(ModuleActivatorExample.loadedResource);
	}
	
	private static void useOverlayService() {
		IModuleContext moduleContext = ModuleActivatorExample.getModuleContext();
		IServiceReference<IOverlayProvider> serviceRef = moduleContext.getServiceReference(IOverlayProvider.class);
		IOverlayProvider overlayProvider = moduleContext.getService(serviceRef);
		List<IOverlay> overlays = overlayProvider.getOverlays();
		System.out.println("Overlays known to OverlayProvider: " + overlays);
		Assert.assertEquals(1, overlays.size());
	}
	
}
