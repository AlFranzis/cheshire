package cheshire.test.cdi.service;

import al.franzis.cheshire.cdi.CDIModuleFramework;
import al.franzis.cheshire.service.IServiceContext;
import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceActivationMethod;
import al.franzis.cheshire.service.ServiceBindMethod;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginManager2", providedServices = { "cheshire.test.cdi.service.IPluginManager" }, referencedServices = { "cheshire.test.cdi.service.IPlugin" }, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginManager2 implements IPluginManager, IServiceDefinition {
  private final ArrayList<IPlugin> plugins = new ArrayList<IPlugin>();
  
  public PluginManager2() {
    InputOutput.<String>println("PluginManager2 created");
  }
  
  public List<IPlugin> getPlugins() {
    return this.plugins;
  }
  
  @ServiceActivationMethod
  public void activate(final IServiceContext serviceContext) {
    InputOutput.<String>println("PluginManager2.activate() called");
    Map<String, String> _properties = serviceContext.getProperties();
    String _plus = ("PluginManager2 service properties: " + _properties);
    InputOutput.<String>println(_plus);
  }
  
  @ServiceBindMethod
  public void addPlugin(final IPlugin plugin) {
    this.plugins.add(plugin);
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
  
  @Inject
  public void setInstances0(final Instance<IPlugin> instances) {
    java.util.Iterator<cheshire.test.cdi.service.IPlugin> it = instances.iterator();
    					while(it.hasNext()) {
    						addPlugin(it.next());
    					}
  }
}
