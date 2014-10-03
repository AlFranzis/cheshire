package al.franzis.cheshire.cdi.proc;

import al.franzis.cheshire.cdi.proc.ReferencedServiceInfo;
import java.util.Map;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ServiceInfo {
  private final String _name;
  
  private final ReferencedServiceInfo[] _referencedServices;
  
  private final String[] _providedServices;
  
  private final Map<String, String> _properties;
  
  public ServiceInfo(final String name, final ReferencedServiceInfo[] referencedServices, final String[] providedServices, final Map<String, String> properties) {
    super();
    this._name = name;
    this._referencedServices = referencedServices;
    this._providedServices = providedServices;
    this._properties = properties;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._name== null) ? 0 : this._name.hashCode());
    result = prime * result + ((this._referencedServices== null) ? 0 : this._referencedServices.hashCode());
    result = prime * result + ((this._providedServices== null) ? 0 : this._providedServices.hashCode());
    result = prime * result + ((this._properties== null) ? 0 : this._properties.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServiceInfo other = (ServiceInfo) obj;
    if (this._name == null) {
      if (other._name != null)
        return false;
    } else if (!this._name.equals(other._name))
      return false;
    if (this._referencedServices == null) {
      if (other._referencedServices != null)
        return false;
    } else if (!this._referencedServices.equals(other._referencedServices))
      return false;
    if (this._providedServices == null) {
      if (other._providedServices != null)
        return false;
    } else if (!this._providedServices.equals(other._providedServices))
      return false;
    if (this._properties == null) {
      if (other._properties != null)
        return false;
    } else if (!this._properties.equals(other._properties))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
  
  @Pure
  public String getName() {
    return this._name;
  }
  
  @Pure
  public ReferencedServiceInfo[] getReferencedServices() {
    return this._referencedServices;
  }
  
  @Pure
  public String[] getProvidedServices() {
    return this._providedServices;
  }
  
  @Pure
  public Map<String, String> getProperties() {
    return this._properties;
  }
}
