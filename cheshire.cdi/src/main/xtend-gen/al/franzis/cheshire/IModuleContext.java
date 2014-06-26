package al.franzis.cheshire;

import al.franzis.cheshire.IModule;
import java.util.List;

@SuppressWarnings("all")
public interface IModuleContext {
  public abstract IModule getModule();
  
  public abstract IModule getModule(final String name);
  
  public abstract List<IModule> getModules();
}
