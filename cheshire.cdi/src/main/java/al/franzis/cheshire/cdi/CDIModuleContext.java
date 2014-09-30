package al.franzis.cheshire.cdi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.service.IServiceReference;

public class CDIModuleContext implements IModuleContext {
	private final CDIModuleFramework moduleFramework;
	private final IModule cdiModule = new CDIModule();
	
	private CDIServiceFactory serviceFactory;
	
	public CDIModuleContext(CDIModuleFramework moduleFramework) {
		this.moduleFramework = moduleFramework;
		this.serviceFactory = moduleFramework.getServiceFactory();
	}
	
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
		return serviceFactory.createOrGetService(clazz);
	}

	@Override
	public <S> Collection<IServiceReference<S>> getServiceReferences(Class<S> clazz) {
		return serviceFactory.createOrGetServices(clazz);
	}

	@Override
	public <S> void registerService(Class<S> serviceClass, S service) {
		
	}

	@Override
	public <S> S getService(IServiceReference<S> serviceReference) {
		return ((CDIServiceReference<S>)serviceReference).getService();
	}

	@Override
	public void ungetService(IServiceReference<? extends Object> serviceReference) {
		
	}

}
