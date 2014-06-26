package cheshire.test.cdi.activator;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleActivator;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;
import al.franzis.cheshire.cdi.ICDIModuleActivator;
import java.net.URL;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@ModuleActivator
@SuppressWarnings("all")
public class ModuleActivatorExample implements ICDIModuleActivator {
  private IModuleContext moduleContext;
  
  @ModuleStartMethod
  public void start() {
    IModule _module = this.moduleContext.getModule();
    final URL resourceUrl = _module.getResource("/resource.xml");
    InputOutput.<URL>println(resourceUrl);
  }
  
  @ModuleStopMethod
  public void stop() {
  }
  
  @ModuleContextMethod
  @Inject
  public void setModuleContext(final IModuleContext moduleContext) {
    this.moduleContext = moduleContext;
  }
}
