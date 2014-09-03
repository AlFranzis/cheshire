package al.franzis.cheshire

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

@Active(ModuleProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation Module {
}

class ModuleProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val moduleManifestType = context.newTypeReference("al.franzis.cheshire.cdi.ICDIModuleManifest")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ moduleManifestType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
      	
      	val nativeClauses = parseNativeClauses(annotatedClass)
		val libHandler = new NativeLibHandler(nativeClauses);
		val nativeLibs = libHandler.moduleNativeLibs
      	
//		val postConstructAnnotationType = context.findTypeGlobally("javax.annotation.PostConstruct")
		val postConstructAnnotationType = context.newAnnotationReference("javax.annotation.PostConstruct")
      	annotatedClass.addMethod("init") [
			addAnnotation(postConstructAnnotationType)
      		body = ['''
      			String nativeLibs = "«nativeLibs»";
      			al.franzis.cheshire.NativeLibHandler.augmentJavaLibraryPath(nativeLibs);
      		''']
      	]
	}

	override doGenerateCode(ClassDeclaration annotatedClass, extension CodeGenerationContext context) {
	}
	
	private def String parseNativeClauses(ClassDeclaration annotatedClass) {
		for ( field : annotatedClass.declaredFields) {
			val key = field.simpleName
			if ( "Bundle_NativeCode".equals(key) ) {
				val nativeClauses = field.initializer.toString
				return nativeClauses
			}
		}
		null
	}
}
