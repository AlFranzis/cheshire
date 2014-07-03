package al.franzis.cheshire.test.osgi.activator;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleActivator;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;
import java.net.URL;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

@ModuleActivator
@SuppressWarnings("all")
public class ModuleActivatorExample implements BundleActivator {
  private static IModuleContext moduleContext;
  
  @ModuleStartMethod
  public void start() {
    IModule _module = ModuleActivatorExample.moduleContext.getModule();
    final URL resourceUrl = _module.getResource("someResource");
    InputOutput.<String>println(("Resource URL: " + resourceUrl));
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
    setModuleContext( al.franzis.cheshire.osgi.OSGiModuleFramework.getInstance().getOrCreateModule( bundleContext ).getModuleContext() );
    start();
  }
  
  public void stop(final BundleContext bundleContext) {
    stop();
  }
}
