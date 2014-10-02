package al.franzis.cheshire.api;

import al.franzis.cheshire.api.IModuleContext;
import java.net.URL;
import java.util.Enumeration;

@SuppressWarnings("all")
public interface IModule {
  public abstract URL getResource(final String name);
  
  public abstract Enumeration<URL> getResources(final String name);
  
  public abstract String getName();
  
  public abstract IModuleContext getModuleContext();
}
