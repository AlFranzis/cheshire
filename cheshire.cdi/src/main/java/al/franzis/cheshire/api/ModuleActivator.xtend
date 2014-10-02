package al.franzis.cheshire.api

import al.franzis.cheshire.cdi.proc.ModuleActivatorProcessor
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.Active

@Active(ModuleActivatorProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation ModuleActivator {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ModuleContextMethod {	
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ModuleStartMethod {	
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ModuleStopMethod {	
}