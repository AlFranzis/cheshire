package al.franzis.cheshire.service

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.List
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceActivationMethod {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceBindMethod {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation ServiceUnbindMethod {
}

@Active(ServiceProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation Service {
	String name
	String[] referencedServices
	String[] providedServices
	String[] properties
}

class ServiceProcessor extends AbstractClassProcessor {
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements, extension CodeGenerationContext context) {
	}
	
}
