package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginC", providedServices = { "al.franzis.cheshire.test.osgi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginC implements IPlugin {
  public String PluginC() {
    String _println = InputOutput.<String>println("PluginC created");
    return _println;
  }
  
  public void foo() {
    System.out.println("PluginC.foo() called");
  }
}
