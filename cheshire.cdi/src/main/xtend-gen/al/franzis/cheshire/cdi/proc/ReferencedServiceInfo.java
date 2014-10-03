package al.franzis.cheshire.cdi.proc;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ReferencedServiceInfo {
  private final String _name;
  
  private final String _bindMethodName;
  
  public ReferencedServiceInfo(final String name, final String bindMethodName) {
    super();
    this._name = name;
    this._bindMethodName = bindMethodName;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._name== null) ? 0 : this._name.hashCode());
    result = prime * result + ((this._bindMethodName== null) ? 0 : this._bindMethodName.hashCode());
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
    ReferencedServiceInfo other = (ReferencedServiceInfo) obj;
    if (this._name == null) {
      if (other._name != null)
        return false;
    } else if (!this._name.equals(other._name))
      return false;
    if (this._bindMethodName == null) {
      if (other._bindMethodName != null)
        return false;
    } else if (!this._bindMethodName.equals(other._bindMethodName))
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
  public String getBindMethodName() {
    return this._bindMethodName;
  }
}
