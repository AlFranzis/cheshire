package al.franzis.cheshire.api.service;

import al.franzis.cheshire.cdi.proc.ServiceProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

@Active(ServiceProcessor.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
  public String name();
  public String[] referencedServices();
  public String[] providedServices();
  public String[] properties();
}
