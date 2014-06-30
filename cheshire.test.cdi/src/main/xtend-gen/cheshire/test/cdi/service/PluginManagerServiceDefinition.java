package cheshire.test.cdi.service;

import al.franzis.cheshire.service.IServiceDefinition;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class PluginManagerServiceDefinition implements IServiceDefinition {
  public String name() {
    return "pluginManager";
  }
  
  public String implementation() {
    return "cheshire.test.cdi.service.PluginManager";
  }
  
  public List<String> referencedServices() {
    return Collections.<String>unmodifiableList(Lists.<String>newArrayList("cheshire.test.cdi.service.IPlugin"));
  }
  
  public List<String> providedServices() {
    return Collections.<String>unmodifiableList(Lists.<String>newArrayList("cheshire.test.cdi.service.IPluginManager"));
  }
  
  public Map<String,String> properties() {
    Map<String,String> _xsetliteral = null;
    Map<String,String> _tempMap = Maps.<String, String>newHashMap();
    _tempMap.put("Prop1", "Value1");
    _tempMap.put("Prop2", "Value2");
    _xsetliteral = Collections.<String, String>unmodifiableMap(_tempMap);
    return _xsetliteral;
  }
}
