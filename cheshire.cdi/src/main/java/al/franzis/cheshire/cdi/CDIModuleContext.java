package al.franzis.cheshire.cdi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.Alternative;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;

@Alternative
public class CDIModuleContext implements IModuleContext {
	private IModule cdiModule = new CDIModule();
	
	public IModule getModule() {
		return cdiModule;
	}

	public IModule getModule(String name) {
		return getModule();
	}

	public List<IModule> getModules() {
		return Arrays.asList(cdiModule);
	}

	@Override
	public <S> IServiceReference<S> getServiceReference(Class<S> clazz) {
		return null;
	}

	@Override
	public <S> Collection<IServiceReference<S>> getServiceReferences(
			Class<S> clazz) {
		return null;
	}

	@Override
	public <S> void registerService(Class<S> serviceClass, S service) {
		
	}

	@Override
	public <S> S getService(IServiceReference<S> serviceReference) {
		return null;
	}

	@Override
	public void ungetService(IServiceReference<? extends Object> serviceReference) {
		
	}

}
