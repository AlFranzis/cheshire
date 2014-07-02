package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.IServiceDefinition;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class PluginAServiceDefinition implements IServiceDefinition {
  public String name() {
    return "pluginA";
  }
  
  public String implementation() {
    return "cheshire.test.cdi.service.PluginA";
  }
  
  public List<String> referencedServices() {
    return Collections.<String>unmodifiableList(com.google.common.collect.Lists.<String>newArrayList());
  }
  
  public List<String> providedServices() {
    return Collections.<String>unmodifiableList(com.google.common.collect.Lists.<String>newArrayList("cheshire.test.cdi.service.IPlugin"));
  }
  
  public Map<String,String> properties() {
    throw new Error("Unresolved compilation problems:"
      + "\n-> cannot be resolved."
      + "\n-> cannot be resolved."
      + "\nType mismatch: cannot convert from Set<Object> to Map<String, String>");
  }
}
