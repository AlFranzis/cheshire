package al.franzis.cheshire.test.osgi.service.factory;

import al.franzis.cheshire.api.service.Service;
import al.franzis.cheshire.test.osgi.service.factory.IOverlay;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Service(name = "CircleOverlay", providedServices = "al.franzis.cheshire.test.osgi.service.factory.IOverlay", factory = "overlayFactory")
@SuppressWarnings("all")
public class CircleOverlay implements IOverlay {
  public void paint() {
    InputOutput.<String>println("CircleOverlay.paint() called");
  }
}
