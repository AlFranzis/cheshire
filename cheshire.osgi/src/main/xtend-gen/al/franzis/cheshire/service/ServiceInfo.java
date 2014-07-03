package al.franzis.cheshire.service;

import al.franzis.cheshire.service.ReferencedServiceInfo;
import java.util.Map;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ServiceInfo {
  private final String _name;
  
  public String getName() {
    return this._name;
  }
  
  private final ReferencedServiceInfo[] _referencedServices;
  
  public ReferencedServiceInfo[] getReferencedServices() {
    return this._referencedServices;
  }
  
  private final String[] _providedServices;
  
  public String[] getProvidedServices() {
    return this._providedServices;
  }
  
  private final Map<String,String> _properties;
  
  public Map<String,String> getProperties() {
    return this._properties;
  }
  
  public ServiceInfo(final String name, final ReferencedServiceInfo[] referencedServices, final String[] providedServices, final Map<String,String> properties) {
    super();
    this._name = name;
    this._referencedServices = referencedServices;
    this._providedServices = providedServices;
    this._properties = properties;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name== null) ? 0 : _name.hashCode());
    result = prime * result + ((_referencedServices== null) ? 0 : _referencedServices.hashCode());
    result = prime * result + ((_providedServices== null) ? 0 : _providedServices.hashCode());
    result = prime * result + ((_properties== null) ? 0 : _properties.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServiceInfo other = (ServiceInfo) obj;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_referencedServices == null) {
      if (other._referencedServices != null)
        return false;
    } else if (!_referencedServices.equals(other._referencedServices))
      return false;
    if (_providedServices == null) {
      if (other._providedServices != null)
        return false;
    } else if (!_providedServices.equals(other._providedServices))
      return false;
    if (_properties == null) {
      if (other._properties != null)
        return false;
    } else if (!_properties.equals(other._properties))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
