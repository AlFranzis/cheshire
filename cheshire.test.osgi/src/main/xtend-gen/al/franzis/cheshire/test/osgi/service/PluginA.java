package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "PluginA", providedServices = { "al.franzis.cheshire.test.osgi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginA implements IPlugin {
  public String PluginA() {
    String _println = InputOutput.<String>println("PluginA created");
    return _println;
  }
  
  public void foo() {
    System.out.println("PluginA.foo() called");
  }
}