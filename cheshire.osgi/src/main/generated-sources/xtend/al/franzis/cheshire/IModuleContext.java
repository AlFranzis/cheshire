package al.franzis.cheshire;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.service.IServiceReference;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("all")
public interface IModuleContext {
  public abstract IModule getModule();
  
  public abstract IModule getModule(final String name);
  
  public abstract List<IModule> getModules();
  
  public abstract <S extends Object> void registerService(final Class<S> serviceClass, final S service);
  
  public abstract <S extends Object> IServiceReference<S> getServiceReference(final Class<S> clazz);
  
  public abstract <S extends Object> Collection<IServiceReference<S>> getServiceReferences(final Class<S> clazz);
  
  public abstract <S extends Object> S getService(final IServiceReference<S> serviceReference);
  
  public abstract void ungetService(final IServiceReference<? extends Object> serviceReference);
}
