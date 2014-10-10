package al.franzis.cheshire.cdi.proc;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ReferencedServiceFactoryInfo {
  private final String name;
  
  private final String bindMethodName;
  
  public ReferencedServiceFactoryInfo(final String name, final String bindMethodName) {
    super();
    this.name = name;
    this.bindMethodName = bindMethodName;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.name== null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.bindMethodName== null) ? 0 : this.bindMethodName.hashCode());
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
    ReferencedServiceFactoryInfo other = (ReferencedServiceFactoryInfo) obj;
    if (this.name == null) {
      if (other.name != null)
        return false;
    } else if (!this.name.equals(other.name))
      return false;
    if (this.bindMethodName == null) {
      if (other.bindMethodName != null)
        return false;
    } else if (!this.bindMethodName.equals(other.bindMethodName))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("name", this.name);
    b.add("bindMethodName", this.bindMethodName);
    return b.toString();
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
  
  @Pure
  public String getBindMethodName() {
    return this.bindMethodName;
  }
}
