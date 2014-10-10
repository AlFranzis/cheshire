package cheshire.test.cdi.service.factory;

import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.api.service.IServiceFactory;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceBindMethod;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import cheshire.test.cdi.service.factory.IOverlay;
import cheshire.test.cdi.service.factory.IOverlayProvider;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Service(name = "OverlayProvider", providedServices = { "cheshire.test.cdi.service.factory.IOverlayProvider" }, referencedServiceFactories = { "overlayFactory" })
@ServiceImplementation
@SuppressWarnings("all")
public class OverlayProvider implements IOverlayProvider, IServiceDefinition {
  private IServiceFactory serviceFactory;
  
  @ServiceBindMethod
  public void addComponentFactory(final IServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }
  
  public List<IOverlay> getOverlays() {
    Object _newInstance = this.serviceFactory.newInstance();
    final IOverlay overlay = ((IOverlay) _newInstance);
    return Arrays.<IOverlay>asList(overlay);
  }
  
  private CDIModuleFramework moduleFramework;
  
  @Inject
  public void setModuleFramework(final CDIModuleFramework moduleFramework) {
    this.moduleFramework = moduleFramework;
  }
  
  @PostConstruct
  public void init() {
    moduleFramework.registerCDIService(this);
  }
}
