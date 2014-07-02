package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import al.franzis.cheshire.test.osgi.service.PluginCServiceDefinition;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(definition = PluginCServiceDefinition.class)
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
