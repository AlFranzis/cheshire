package al.franzis.cheshire.cdi.rt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

import al.franzis.cheshire.api.service.IServiceContext;
import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.api.service.IServiceFactory;
import al.franzis.cheshire.api.service.IServiceReference;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceActivationMethod;
import al.franzis.cheshire.api.service.ServiceBindMethod;

@Singleton
public class CDIServiceFactory {
	
	@Inject
	private BeanManager beanManager;
	
	private Map<Class<?>,List<ServiceProviderContainer<?>>> services = new HashMap<>();
	
	public CDIServiceFactory() {
		System.out.println("CDIServiceFactory created");
	}
	
	@PostConstruct
	public void init() {
		try {
			Annotation serviceImplementationQualifier = new AnnotationLiteral<ServiceImplementation>() {};
			Set<Bean<?>> serviceDefinitionBeans = beanManager.getBeans(IServiceDefinition.class, serviceImplementationQualifier);
			for ( Bean<?> serviceDefinitionBean : serviceDefinitionBeans) {
				ServiceInfo serviceInfo = parseServiceInfo(serviceDefinitionBean.getBeanClass());
				ServiceProviderContainer<?> container = new ServiceProviderContainer<>(serviceInfo);

				for (String typeName : serviceInfo.getProvidedServices()) {
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
			new RuntimeException("Error while initializing CDI Service Factory", e);
		}
	}
	
	public void registerService(Object serviceInstance) {
		RuntimeLogger.getInstance().info("Register service instance: " + serviceInstance);
		
		ServiceProviderContainer container = getContainer(serviceInstance.getClass());
		CDIServiceReference<?> serviceRef = new CDIServiceReference(serviceInstance);
		container.addServiceInstance(serviceRef);
		
		injectServiceFactories(container, serviceInstance);
		
		IServiceContext serviceContext = new CDIServiceContext( container.getServiceInfo().getProperties() );
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
			/* when calling createService() the CDI container calls registerService() implicitly
			 * to register the service instance
			 */
			serviceContainer.serviceFactoryInstance.newInstance();
		}
		
		serviceInstance = serviceInstances.get(0);
		return serviceInstance;
	}
	
	public <S> List<IServiceReference<S>> createOrGetServices( Class<S> serviceType ) {
		List<ServiceProviderContainer<?>> serviceContainers = services.get(serviceType);
		if ( serviceContainers == null || serviceContainers.isEmpty())
			return null;
		
		List<IServiceReference<S>> refs = new ArrayList<>();
		
		for ( ServiceProviderContainer<?> _serviceContainer : serviceContainers ) {
			CDIServiceReference<S> serviceInstance = null;
			ServiceProviderContainer<S> serviceContainer = (ServiceProviderContainer<S>)_serviceContainer;
			List<CDIServiceReference<S>> serviceInstances = serviceContainer.getServiceInstances();
			if ( serviceInstances.isEmpty())
			{
				/* when calling createService() the CDI container calls registerService() implicitly
				 * to register the service instance
				 */
				serviceContainer.serviceFactoryInstance.newInstance();
			}
			
			serviceInstance = serviceInstances.get(0);
			refs.add(serviceInstance);
		}
			
		return refs;
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
	
	private void injectServiceFactories( ServiceProviderContainer container, Object serviceInstance ) {
		for( String referencedServiceFactory : container.getServiceInfo().getReferencedServiceFactories() ) {
			for (List<ServiceProviderContainer<?>> serviceContainers : services.values()) {
				for(ServiceProviderContainer sContainer : serviceContainers) {
					if ( sContainer.getServiceInfo().getFactory().equals(referencedServiceFactory)) {
						CDIServiceFactoryImpl serviceFactory = sContainer.serviceFactoryInstance;
						injectServiceFactory(serviceInstance, serviceFactory);
					}
				}
			}
		}
	}
	
	private void injectServiceFactory( Object serviceInstance, CDIServiceFactoryImpl serviceFactory) {
		for ( Method m : Helpers.getAnnotatedMethods( serviceInstance.getClass(), ServiceBindMethod.class) ) {
			Class<?>[] methodParams = m.getParameterTypes();
			if ( methodParams.length == 1 && methodParams[0].equals(IServiceFactory.class)) {
				try {
					m.invoke(serviceInstance, serviceFactory);
				} catch (Exception e) {
					throw new RuntimeException("Error while calling service bind method", e);
				}
			}
		}
	}
	
	
	
	private class ServiceProviderContainer<S> {
		private final ServiceInfo serviceInfo;
		private final Class<S> implementationClass;
		private final List<CDIServiceReference<S>> serviceInstances = new LinkedList<>();
		private CDIServiceFactoryImpl serviceFactoryInstance;
		
		public ServiceProviderContainer(ServiceInfo serviceInfo) {
			this.serviceInfo = serviceInfo;
			this.implementationClass = (Class<S>)serviceInfo.getServiceImplementation();
			this.serviceFactoryInstance = createServiceFactory( implementationClass );
		}
		
		private <S> CDIServiceFactoryImpl createServiceFactory( Class<S> serviceImpl ) {
			Set<Bean<?>> serviceImplBeans = beanManager.getBeans(CDIServiceFactoryImpl.class);
			CreationalContext ctx = beanManager.createCreationalContext(null);
			CDIServiceFactoryImpl serviceFactoryInstance = (CDIServiceFactoryImpl)serviceImplBeans.iterator().next().create(ctx);
			serviceFactoryInstance.setServiceImplementation(serviceImpl);
			return serviceFactoryInstance;
		}

		public void addServiceInstance(CDIServiceReference<S> serviceInstance) {
			serviceInstances.add(serviceInstance);
		}
		
		public List<CDIServiceReference<S>> getServiceInstances() {
			return serviceInstances;
		}
		
		public ServiceInfo getServiceInfo() {
			return serviceInfo;
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
				throw new RuntimeException("Error while calling service activation method", e);
			}
		}
	}
	
	
	private ServiceInfo parseServiceInfo( Class<?> clazz ) {
		Service service = clazz.getAnnotation(Service.class);
		if ( service == null)
			return null;
		
		return new ServiceInfo(service.name(), clazz, service.factory(), service.referencedServices(), service.referencedServiceFactories(), service.providedServices(), service.properties());
	}
	
	private static class ServiceInfo {
		private final String name;
		private final Class<?> serviceImplementation;
		private final String factory;
		private final List<String> referencedServices;
		private final List<String> referencedServiceFactories;
		private final List<String> providedServices;
		private final Map<String, String> properties;

		private ServiceInfo(String name, Class<?> serviceImplementation, String factory, String[] referencedServices, String[] referencedServiceFactories,
				String[] providedServices, String[] properties) {
			this.name = name;
			this.serviceImplementation = serviceImplementation;
			this.factory = factory;
			this.referencedServices = Arrays.asList(referencedServices);
			this.referencedServiceFactories = Arrays.asList(referencedServiceFactories);
			this.providedServices = Arrays.asList(providedServices);
			this.properties = new HashMap<>();
			for (int i = 0; i < properties.length;) {
				this.properties.put(properties[i++], properties[i++]);
			}
		}

		public String getName() {
			return name;
		}
		
		public Class<?> getServiceImplementation() {
			return serviceImplementation;
		}
		
		public String getFactory() {
			return factory;
		}

		public List<String> getReferencedServices() {
			return referencedServices;
		}
		
		public List<String> getReferencedServiceFactories() {
			return referencedServiceFactories;
		}

		public List<String> getProvidedServices() {
			return providedServices;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

	}

}
