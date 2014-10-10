package al.franzis.cheshire.api.service;

import al.franzis.cheshire.osgi.proc.ServiceProcessor;
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
  public String[] referencedServices() default {};
  public String[] providedServices() default {};
  public String factory() default "";
  public String[] referencedServiceFactories() default {};
  public String[] properties() default {};
}
