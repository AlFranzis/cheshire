package al.franzis.cheshire.api

import java.lang.annotation.Target
import java.lang.annotation.Retention
import org.eclipse.xtend.lib.macro.Active
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import al.franzis.cheshire.cdi.proc.ModuleManifestProcessor

@Active(ModuleManifestProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation Module {
}
