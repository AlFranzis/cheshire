package cheshire.test.cdi.service.factory;

import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import cheshire.test.cdi.service.factory.IOverlay;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "CircleOverlay", providedServices = { "cheshire.test.cdi.service.factory.IOverlay" }, factory = "overlayFactory")
@ServiceImplementation
@SuppressWarnings("all")
public class CircleOverlay implements IOverlay, IServiceDefinition {
  public void paint() {
    InputOutput.<String>println("CircleOverlay.paint() called");
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
