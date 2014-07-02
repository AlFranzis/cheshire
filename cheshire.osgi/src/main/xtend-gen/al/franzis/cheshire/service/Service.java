package al.franzis.cheshire.service;

import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.ServiceProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

@Active(ServiceProcessor.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
  public Class<? extends IServiceDefinition> definition();
}
