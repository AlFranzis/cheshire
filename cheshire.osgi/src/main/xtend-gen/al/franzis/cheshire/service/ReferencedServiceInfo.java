package al.franzis.cheshire.service;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ReferencedServiceInfo {
  private final String _name;
  
  public String getName() {
    return this._name;
  }
  
  private final String _bindMethodName;
  
  public String getBindMethodName() {
    return this._bindMethodName;
  }
  
  public ReferencedServiceInfo(final String name, final String bindMethodName) {
    super();
    this._name = name;
    this._bindMethodName = bindMethodName;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name== null) ? 0 : _name.hashCode());
    result = prime * result + ((_bindMethodName== null) ? 0 : _bindMethodName.hashCode());
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
    ReferencedServiceInfo other = (ReferencedServiceInfo) obj;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_bindMethodName == null) {
      if (other._bindMethodName != null)
        return false;
    } else if (!_bindMethodName.equals(other._bindMethodName))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}