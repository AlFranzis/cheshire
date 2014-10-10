package al.franzis.cheshire.test.osgi.service.factory;

import al.franzis.cheshire.api.service.IServiceFactory;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.api.service.ServiceBindMethod;
import al.franzis.cheshire.test.osgi.service.factory.IOverlay;
import al.franzis.cheshire.test.osgi.service.factory.IOverlayProvider;
import java.util.Arrays;
import java.util.List;
import org.osgi.service.component.ComponentFactory;

@Service(name = "OverlayProvider", providedServices = { "al.franzis.cheshire.test.osgi.service.factory.IOverlayProvider" }, referencedServiceFactories = { "overlayFactory" })
@SuppressWarnings("all")
public class OverlayProvider implements IOverlayProvider {
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
  
  public void addComponentFactory(final ComponentFactory componentFactory) {
    al.franzis.cheshire.osgi.rt.OSGiServiceFactory osgiComponentFactory = new al.franzis.cheshire.osgi.rt.OSGiServiceFactory(componentFactory);
    	addComponentFactory(osgiComponentFactory);
  }
}
