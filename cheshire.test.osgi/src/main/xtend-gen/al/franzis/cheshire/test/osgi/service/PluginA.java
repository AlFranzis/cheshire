package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.api.service.IServiceContext;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceActivationMethod;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.osgi.service.component.ComponentContext;

@Service(name = "PluginA", providedServices = { "al.franzis.cheshire.test.osgi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginA implements IPlugin {
  public String PluginA() {
    String _println = InputOutput.<String>println("PluginA created");
    return _println;
  }
  
  @ServiceActivationMethod
  public void activate(final IServiceContext serviceContext) {
    System.out.println("PluginA.activate() called");
    Map<String,String> _properties = serviceContext.getProperties();
    String _plus = ("PluginA service properties: " + _properties);
    System.out.println(_plus);
  }
  
  public void foo() {
    System.out.println("PluginA.foo() called");
  }
  
  public void activate(final ComponentContext componentContext) {
    al.franzis.cheshire.osgi.rt.OSGiServiceContext osgiComponentContext = new al.franzis.cheshire.osgi.rt.OSGiServiceContext(componentContext);
    activate(osgiComponentContext);
  }
}
