package cheshire.test.cdi.service;

import al.franzis.cheshire.cdi.CDIModuleFramework;
import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceImplementation;
import cheshire.test.cdi.service.IPlugin;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginD", providedServices = { "cheshire.test.cdi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@ServiceImplementation
@SuppressWarnings("all")
public class PluginD implements IPlugin, IServiceDefinition {
  public PluginD() {
    InputOutput.<String>println("PluginD created");
  }
  
  public void foo() {
    InputOutput.<String>println("PluginD.foo() called");
  }
  
  private CDIModuleFramework moduleFramework;
  
  @Inject
  public void setModuleFramework(final CDIModuleFramework moduleFramework) {
    this.moduleFramework = moduleFramework;
  }
  
  @PostConstruct
  public void init() {
    moduleFramework.registerCDIService(this);
  }
}
