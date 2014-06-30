package al.franzis.cheshire

import org.eclipse.xtend.lib.macro.Active
import java.lang.annotation.Target
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import java.lang.annotation.ElementType
import java.util.List
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration

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

class ModuleActivatorProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		// add ICDIModuleActivatorActivator to implemented interfaces
		val cdiModuleActivatorType = context.newTypeReference("al.franzis.cheshire.cdi.ICDIModuleActivator")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ cdiModuleActivatorType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
//		val moduleContextMethod = findAnnotatedMethod(annotatedClass, ModuleContextMethod)
//		val injectAnnotationType = context.findTypeGlobally("javax.inject.Inject")
//		moduleContextMethod.addAnnotation(injectAnnotationType)
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,
		extension CodeGenerationContext context) {	
	}
	
	private def MutableMethodDeclaration findAnnotatedMethod( MutableClassDeclaration annotatedClass, Class<?> annotation ) {
		for ( method : annotatedClass.declaredMethods ) {
			if ( method.annotations.exists( [ m | m.annotationTypeDeclaration.simpleName == annotation.simpleName ]))
				return method
		}
		return null
	}
	
}