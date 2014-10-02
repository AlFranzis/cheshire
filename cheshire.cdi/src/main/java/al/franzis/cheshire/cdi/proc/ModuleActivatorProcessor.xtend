package al.franzis.cheshire.cdi.proc

import java.util.List
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

class ModuleActivatorProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		// add ICDIModuleActivatorActivator to implemented interfaces
		val cdiModuleActivatorType = context.newTypeReference("al.franzis.cheshire.cdi.rt.ICDIModuleActivator")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ cdiModuleActivatorType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
//		val moduleContextMethod = Helpers.findAnnotatedMethod(annotatedClass, ModuleContextMethod)
//		val injectAnnotationType = context.findTypeGlobally("javax.inject.Inject")
//		moduleContextMethod.addAnnotation(injectAnnotationType)
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,
		extension CodeGenerationContext context) {}
	
}