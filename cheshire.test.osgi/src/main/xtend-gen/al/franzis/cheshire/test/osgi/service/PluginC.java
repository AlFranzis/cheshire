package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import al.franzis.cheshire.test.osgi.service.PluginCServiceDefinition;

@Service(definition = PluginCServiceDefinition.class)
@SuppressWarnings("all")
public class PluginC implements IPlugin {
  public Object PluginC() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println is undefined for the type PluginC");
  }
  
  public void foo() {
    System.out.println("PluginC.foo() called");
  }
}
