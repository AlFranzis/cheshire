package al.franzis.cheshire.api

import al.franzis.cheshire.osgi.proc.ModuleManifestProcessor
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.Active

@Active(ModuleManifestProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation ModuleManifest {
}
