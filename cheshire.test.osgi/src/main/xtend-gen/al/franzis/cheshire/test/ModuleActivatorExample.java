package al.franzis.cheshire.test;

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
  private IModuleContext moduleContext;
  
  @ModuleStartMethod
  public void start() {
    IModule _module = this.moduleContext.getModule();
    final URL resourceUrl = _module.getResource("someResource");
    InputOutput.<URL>println(resourceUrl);
  }
  
  @ModuleStopMethod
  public void stop() {
  }
  
  @ModuleContextMethod
  public void setModuleContext(final IModuleContext context) {
    this.moduleContext = this.moduleContext;
  }
  
  public void start(final BundleContext bundleContext) {
    setModuleContext( new al.franzis.cheshire.osgi.OSGiModuleContext( bundleContext ) );
    start();
  }
  
  public void stop(final BundleContext bundleContext) {
    stop();
  }
}
