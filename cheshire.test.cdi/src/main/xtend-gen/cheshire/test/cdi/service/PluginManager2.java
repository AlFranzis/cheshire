package cheshire.test.cdi.service;

import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceBindMethod;
import cheshire.test.cdi.service.IPlugin;
import cheshire.test.cdi.service.IPluginManager;
import cheshire.test.cdi.service.PluginManagerServiceDefinition2;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Service(definition = PluginManagerServiceDefinition2.class, definitionName = "PluginManagerServiceDefintion2", name = "PluginManager2", providedServices = { IPluginManager.class }, referencedServices = { IPlugin.class }, properties = { "Prop1", "Value1", "Prop2", "Value2" })
@SuppressWarnings("all")
public class PluginManager2 implements IPluginManager {
  private final ArrayList<IPlugin> plugins = new Function0<ArrayList<IPlugin>>() {
    public ArrayList<IPlugin> apply() {
      ArrayList<IPlugin> _arrayList = new ArrayList<IPlugin>();
      return _arrayList;
    }
  }.apply();
  
  public List<IPlugin> getPlugins() {
    return this.plugins;
  }
  
  @ServiceBindMethod
  public void addPlugin(final IPlugin plugin) {
    this.plugins.add(plugin);
  }
}
