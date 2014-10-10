package cheshire.test.cdi.service;

import al.franzis.cheshire.api.service.IServiceContext;
import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceActivationMethod;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import cheshire.test.cdi.service.IPlugin;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginC", providedServices = { "cheshire.test.cdi.service.IPlugin" })
@ServiceImplementation
@SuppressWarnings("all")
public class PluginC implements IPlugin, IServiceDefinition {
  public PluginC() {
    InputOutput.<String>println("PluginC created");
  }
  
  @ServiceActivationMethod
  public void activate(final IServiceContext serviceContext) {
    System.out.println("PluginC.activate() called");
    Map<String, String> _properties = serviceContext.getProperties();
    String _plus = ("PluginC service properties: " + _properties);
    System.out.println(_plus);
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
