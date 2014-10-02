package cheshire.test.cdi.service;

import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import cheshire.test.cdi.service.IPlugin;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginC", providedServices = { "cheshire.test.cdi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@ServiceImplementation
@SuppressWarnings("all")
public class PluginC implements IPlugin, IServiceDefinition {
  public PluginC() {
    InputOutput.<String>println("PluginC created");
  }
  
  public void foo() {
    InputOutput.<String>println("PluginC.foo() called");
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
