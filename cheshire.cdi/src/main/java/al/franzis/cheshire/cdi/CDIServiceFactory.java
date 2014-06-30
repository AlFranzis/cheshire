package al.franzis.cheshire.cdi;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import al.franzis.cheshire.service.ICDIServiceDefinition;
import al.franzis.cheshire.service.IServiceContext;
import al.franzis.cheshire.service.IServiceReference;
import al.franzis.cheshire.service.ServiceActivationMethod;

@Singleton
public class CDIServiceFactory {
	
	@Inject
	private BeanManager beanManager;
	
	@Inject
	private Instance<ICDIServiceDefinition> serviceDefinitions;
	
	private Map<Class<?>,List<ServiceProviderContainer<?>>> services = new HashMap<>();
	
	public CDIServiceFactory() {
		System.out.println("CDIServiceFactory created");
	}
	
	@PostConstruct
	public void init() {
		try {
			for (ICDIServiceDefinition serviceDef : serviceDefinitions) {
				ServiceProviderContainer<?> container = new ServiceProviderContainer<>(serviceDef);

				for (String typeName : serviceDef.providedServices()) {
					Class<?> serviceType = Class.forName(typeName);
					List<ServiceProviderContainer<?>> serviceContainers = services.get(serviceType);
					
					if (serviceContainers == null) {
						serviceContainers = new LinkedList<>();
						services.put(serviceType, serviceContainers);
					}
					serviceContainers.add(container);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void registerService(Object serviceInstance) {
		System.out.println("Register service instance: " + serviceInstance);
		
		ServiceProviderContainer container = getContainer(serviceInstance.getClass());
		CDIServiceReference<?> serviceRef = new CDIServiceReference(serviceInstance);
		container.addServiceInstance(serviceRef);
		
		IServiceContext serviceContext = new CDIServiceContext( container.getServiceDefinition().properties() );
		callActivationMethod(serviceInstance, serviceContext);
	}
	
	public <S> IServiceReference<S> createOrGetService( Class<S> serviceType ) {
		List<ServiceProviderContainer<?>> serviceContainers = services.get(serviceType);
		if ( serviceContainers == null || serviceContainers.isEmpty())
			return null;
		
		if ( serviceContainers.size() > 1 )
			throw new IllegalArgumentException("Multiple service implementations");
		
		CDIServiceReference<S> serviceInstance = null;
		ServiceProviderContainer<S> serviceContainer = (ServiceProviderContainer<S>)serviceContainers.get(0);
		List<CDIServiceReference<S>> serviceInstances = serviceContainer.getServiceInstances();
		if ( serviceInstances.isEmpty())
		{
			Class<S> serviceImpl = (Class<S>)serviceContainer.getImplementationClass();
			/* when calling createService() the CDI container calls registerService() implicitly
			 * to register the service instance
			 */
			createService(serviceImpl);
		}
		
		serviceInstance = serviceInstances.get(0);
		return serviceInstance;
	}
	
	public <S> List<IServiceReference<S>> createOrGetServices( Class<S> serviceType ) {
		
		return null;
	}
	
	private ServiceProviderContainer getContainer(Class<?> serviceImplementation) {
		for (List<ServiceProviderContainer<?>> serviceContainers : services.values()) {
			for(ServiceProviderContainer container : serviceContainers) {
				if ( container.getImplementationClass().equals(serviceImplementation) ) {
					return container;
				}
			}
		}
		
		return null;
	}
	
	private <S> S createService( Class<S> serviceImpl ) {
		Set<Bean<?>> serviceImplBeans = beanManager.getBeans(serviceImpl);
		CreationalContext ctx = beanManager.createCreationalContext(null);
		S serviceInstance = (S)serviceImplBeans.iterator().next().create(ctx);
		return serviceInstance;
	}
	
	private static class ServiceProviderContainer<S> {
		private final ICDIServiceDefinition serviceDefinition;
		private final Class<S> implementationClass;
		private final List<CDIServiceReference<S>> serviceInstances = new LinkedList<>();
		
		public ServiceProviderContainer(ICDIServiceDefinition serviceDefinition) {
			this.serviceDefinition = serviceDefinition;
			this.implementationClass = (Class<S>)getServiceClass(serviceDefinition);
		}

		public void addServiceInstance(CDIServiceReference<S> serviceInstance) {
			serviceInstances.add(serviceInstance);
		}
		
		public List<CDIServiceReference<S>> getServiceInstances() {
			return serviceInstances;
		}
		
		public ICDIServiceDefinition getServiceDefinition() {
			return serviceDefinition;
		}
		
		private Class<?> getServiceClass(ICDIServiceDefinition serviceDefinition) {
			try {
				return Class.forName(serviceDefinition.implementation());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public Class<S> getImplementationClass() {
			return implementationClass;
		}
		
	}
	
	private void callActivationMethod(Object serviceInstance, IServiceContext serviceContext) {
		Method activationMethod = Helpers.getAnnotatedMethod(serviceInstance.getClass(), ServiceActivationMethod.class);
		if (activationMethod != null) {
			try {
				activationMethod.invoke(serviceInstance, serviceContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
