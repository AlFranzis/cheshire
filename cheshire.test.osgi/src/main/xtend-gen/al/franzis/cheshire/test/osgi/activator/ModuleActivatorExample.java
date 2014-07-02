package al.franzis.cheshire.test.osgi.activator;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleActivator;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;

@ModuleActivator
@SuppressWarnings("all")
public class ModuleActivatorExample {
  private static IModuleContext moduleContext;
  
  @ModuleStartMethod
  public void start() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println is undefined for the type ModuleActivatorExample"
      + "\n+ cannot be resolved.");
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
}
