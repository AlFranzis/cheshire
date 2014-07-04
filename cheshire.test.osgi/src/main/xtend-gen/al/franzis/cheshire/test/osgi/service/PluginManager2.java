package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.IServiceContext;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceActivationMethod;
import al.franzis.cheshire.service.ServiceBindMethod;
import al.franzis.cheshire.test.osgi.service.IPlugin;
import al.franzis.cheshire.test.osgi.service.IPluginManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Service(name = "PluginManager2", providedServices = { "al.franzis.cheshire.test.osgi.service.IPluginManager" }, referencedServices = { "al.franzis.cheshire.test.osgi.service.IPlugin" }, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginManager2 implements IPluginManager {
  private final ArrayList<IPlugin> plugins = new Function0<ArrayList<IPlugin>>() {
    public ArrayList<IPlugin> apply() {
      ArrayList<IPlugin> _arrayList = new ArrayList<IPlugin>();
      return _arrayList;
    }
  }.apply();
  
  public void PluginManager2() {
    System.out.println("PluginManager2 created");
  }
  
  @ServiceActivationMethod
  public void activate(final IServiceContext serviceContext) {
    System.out.println("PluginManager2.activate() called");
    Map<String,String> _properties = serviceContext.getProperties();
    String _plus = ("PluginManager2 service properties: " + _properties);
    System.out.println(_plus);
  }
  
  public List<IPlugin> getPlugins() {
    return this.plugins;
  }
  
  @ServiceBindMethod
  public void addPlugin(final IPlugin plugin) {
    this.plugins.add(plugin);
  }
}
