package al.franzis.cheshire.cdi.rt;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;


import javax.inject.Inject;

import al.franzis.cheshire.api.service.IServiceFactory;

public class CDIServiceFactoryImpl implements IServiceFactory {
	@Inject
	private BeanManager beanManager; 
	
	private Class<?> serviceImpl;
	
	@Override
	public Object newInstance() {
		return createService();
	}
	
	public void setServiceImplementation(Class<?> serviceImpl) {
		this.serviceImpl = serviceImpl;
	}
	
	private <S> S createService() {
		Set<Bean<?>> serviceImplBeans = beanManager.getBeans(serviceImpl, new AnnotationLiteral<ServiceImplementation>(){});
		CreationalContext ctx = beanManager.createCreationalContext(null);
		S serviceInstance = (S)serviceImplBeans.iterator().next().create(ctx);
		return serviceInstance;
	}

}
