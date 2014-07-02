package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.IServiceDefinition;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class PluginManagerServiceDefinition2 implements IServiceDefinition {
  public String name() {
    return "pluginManager2";
  }
  
  public String implementation() {
    return "cheshire.test.cdi.service.PluginManager2";
  }
  
  public List<String> referencedServices() {
    return Collections.<String>unmodifiableList(com.google.common.collect.Lists.<String>newArrayList("cheshire.test.cdi.service.IPlugin"));
  }
  
  public List<String> providedServices() {
    return Collections.<String>unmodifiableList(com.google.common.collect.Lists.<String>newArrayList("cheshire.test.cdi.service.IPluginManager"));
  }
  
  public Map<String,String> properties() {
    throw new Error("Unresolved compilation problems:"
      + "\n-> cannot be resolved."
      + "\n-> cannot be resolved."
      + "\nType mismatch: cannot convert from Set<Object> to Map<String, String>");
  }
}
