package cheshire.test.cdi.activator;

import al.franzis.cheshire.api.IModule;
import al.franzis.cheshire.api.IModuleContext;
import al.franzis.cheshire.api.ModuleActivator;
import al.franzis.cheshire.api.ModuleContextMethod;
import al.franzis.cheshire.api.ModuleStartMethod;
import al.franzis.cheshire.api.ModuleStopMethod;
import al.franzis.cheshire.cdi.rt.ICDIModuleActivator;
import java.net.URL;
import org.eclipse.xtext.xbase.lib.InputOutput;

@ModuleActivator
@SuppressWarnings("all")
public class ModuleActivatorExample implements ICDIModuleActivator {
  private static IModuleContext moduleContext;
  
  public static URL loadedResource;
  
  @ModuleStartMethod
  public void start() {
    IModule _module = ModuleActivatorExample.moduleContext.getModule();
    final URL resourceUrl = _module.getResource("/resource.xml");
    InputOutput.<URL>println(resourceUrl);
    ModuleActivatorExample.loadedResource = resourceUrl;
  }
  
  @ModuleStopMethod
  public void stop() {
  }
  
  @ModuleContextMethod
  public void setModuleContext(final IModuleContext moduleContext) {
    ModuleActivatorExample.moduleContext = moduleContext;
  }
  
  public static IModuleContext getModuleContext() {
    return ModuleActivatorExample.moduleContext;
  }
}
