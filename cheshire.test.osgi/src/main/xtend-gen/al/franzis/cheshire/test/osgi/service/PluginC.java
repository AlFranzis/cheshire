package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.api.service.IServiceContext;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceActivationMethod;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.osgi.service.component.ComponentContext;

@Service(name = "PluginC", providedServices = { "al.franzis.cheshire.test.osgi.service.IPlugin" }, referencedServices = {}, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginC implements IPlugin {
  public String PluginC() {
    return InputOutput.<String>println("PluginC created");
  }
  
  @ServiceActivationMethod
  public void activate(final IServiceContext serviceContext) {
    System.out.println("PluginC.activate() called");
    Map<String, String> _properties = serviceContext.getProperties();
    String _plus = ("PluginC service properties: " + _properties);
    System.out.println(_plus);
  }
  
  public void foo() {
    System.out.println("PluginC.foo() called");
  }
  
  public void activate(final ComponentContext componentContext) {
    al.franzis.cheshire.osgi.rt.OSGiServiceContext osgiComponentContext = new al.franzis.cheshire.osgi.rt.OSGiServiceContext(componentContext);
    activate(osgiComponentContext);
  }
}
