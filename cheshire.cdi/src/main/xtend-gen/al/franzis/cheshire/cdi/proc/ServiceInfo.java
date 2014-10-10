package al.franzis.cheshire.cdi.proc;

import al.franzis.cheshire.cdi.proc.ReferencedServiceFactoryInfo;
import al.franzis.cheshire.cdi.proc.ReferencedServiceInfo;
import java.util.Map;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ServiceInfo {
  private final String name;
  
  private final ReferencedServiceInfo[] referencedServices;
  
  private final ReferencedServiceFactoryInfo[] referencedServiceFactories;
  
  private final String[] providedServices;
  
  private final String factory;
  
  private final Map<String, String> properties;
  
  public ServiceInfo(final String name, final ReferencedServiceInfo[] referencedServices, final ReferencedServiceFactoryInfo[] referencedServiceFactories, final String[] providedServices, final String factory, final Map<String, String> properties) {
    super();
    this.name = name;
    this.referencedServices = referencedServices;
    this.referencedServiceFactories = referencedServiceFactories;
    this.providedServices = providedServices;
    this.factory = factory;
    this.properties = properties;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.name== null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.referencedServices== null) ? 0 : this.referencedServices.hashCode());
    result = prime * result + ((this.referencedServiceFactories== null) ? 0 : this.referencedServiceFactories.hashCode());
    result = prime * result + ((this.providedServices== null) ? 0 : this.providedServices.hashCode());
    result = prime * result + ((this.factory== null) ? 0 : this.factory.hashCode());
    result = prime * result + ((this.properties== null) ? 0 : this.properties.hashCode());
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
    if (this.name == null) {
      if (other.name != null)
        return false;
    } else if (!this.name.equals(other.name))
      return false;
    if (this.referencedServices == null) {
      if (other.referencedServices != null)
        return false;
    } else if (!this.referencedServices.equals(other.referencedServices))
      return false;
    if (this.referencedServiceFactories == null) {
      if (other.referencedServiceFactories != null)
        return false;
    } else if (!this.referencedServiceFactories.equals(other.referencedServiceFactories))
      return false;
    if (this.providedServices == null) {
      if (other.providedServices != null)
        return false;
    } else if (!this.providedServices.equals(other.providedServices))
      return false;
    if (this.factory == null) {
      if (other.factory != null)
        return false;
    } else if (!this.factory.equals(other.factory))
      return false;
    if (this.properties == null) {
      if (other.properties != null)
        return false;
    } else if (!this.properties.equals(other.properties))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("name", this.name);
    b.add("referencedServices", this.referencedServices);
    b.add("referencedServiceFactories", this.referencedServiceFactories);
    b.add("providedServices", this.providedServices);
    b.add("factory", this.factory);
    b.add("properties", this.properties);
    return b.toString();
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
  
  @Pure
  public ReferencedServiceInfo[] getReferencedServices() {
    return this.referencedServices;
  }
  
  @Pure
  public ReferencedServiceFactoryInfo[] getReferencedServiceFactories() {
    return this.referencedServiceFactories;
  }
  
  @Pure
  public String[] getProvidedServices() {
    return this.providedServices;
  }
  
  @Pure
  public String getFactory() {
    return this.factory;
  }
  
  @Pure
  public Map<String, String> getProperties() {
    return this.properties;
  }
}
