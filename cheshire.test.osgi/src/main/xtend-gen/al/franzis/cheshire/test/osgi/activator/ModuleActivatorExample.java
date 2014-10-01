package al.franzis.cheshire.test.osgi.activator;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleActivator;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;
import java.net.URL;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

@ModuleActivator
@SuppressWarnings("all")
public class ModuleActivatorExample implements BundleActivator {
  private static IModuleContext moduleContext;
  
  public static URL loadedResource;
  
  @ModuleStartMethod
  public void start() {
    IModule _module = ModuleActivatorExample.moduleContext.getModule();
    final URL resourceUrl = _module.getResource("someResource");
    ModuleActivatorExample.loadedResource = resourceUrl;
  }
  
  @ModuleStopMethod
  public void stop() {
  }
  
  @ModuleContextMethod
  public void setModuleContext(final IModuleContext context) {
    ModuleActivatorExample.moduleContext = context;
  }
  
  public static IModuleContext getModuleContext() {
    return ModuleActivatorExample.moduleContext;
  }
  
  public void start(final BundleContext bundleContext) {
    try {
          			setModuleContext( al.franzis.cheshire.osgi.OSGiModuleFramework.getInstance().getOrCreateModule( bundleContext ).getModuleContext() );
          			start();
    } catch(Exception e) {
    	throw new RuntimeException("Exception while starting Activator", e);
    }
  }
  
  public void stop(final BundleContext bundleContext) {
    try {
    stop();
    } catch(Exception e) {
    	throw new RuntimeException("Exception while stopping Activator", e);
    }
    
  }
}
