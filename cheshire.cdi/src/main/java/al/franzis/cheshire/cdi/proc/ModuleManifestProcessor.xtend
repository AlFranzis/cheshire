package al.franzis.cheshire.cdi.proc

import al.franzis.cheshire.cdi.rt.NativeLibHandler
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

class ModuleManifestProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val moduleManifestType = context.newTypeReference("al.franzis.cheshire.cdi.rt.ICDIModuleManifest")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ moduleManifestType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		annotatedClass.addMethod("getBundleName") [
			returnType = context.newTypeReference("java.lang.String")
      		body = ['''
      			return Bundle_Name;
      		''']
      	]
		
		val injectAnnotationType = context.newAnnotationReference("javax.inject.Inject")
		val runtimeLibPathProviderType = context.newTypeReference("al.franzis.cheshire.api.nativecode.IRuntimeLibPathProvider")
		annotatedClass.addField("libPathProvider") [
			addAnnotation(injectAnnotationType)
    		type = runtimeLibPathProviderType
      	]
      	
      	val nativeClauses = parseNativeClauses(annotatedClass)
		val libHandler = new NativeLibHandler(nativeClauses);
		val nativeLibs = libHandler.moduleNativeLibs
      	
//		val postConstructAnnotationType = context.findTypeGlobally("javax.annotation.PostConstruct")
		val postConstructAnnotationType = context.newAnnotationReference("javax.annotation.PostConstruct")
      	annotatedClass.addMethod("init") [
			addAnnotation(postConstructAnnotationType)
      		body = ['''
      			String[] nativeLibsPaths = «nativeLibs»;
      			String effectiveNativeLibsPaths = al.franzis.cheshire.cdi.rt.NativeLibHandler.effectiveNativeLibsPaths(this, libPathProvider, nativeLibsPaths);
      			al.franzis.cheshire.cdi.rt.NativeLibHandler.augmentJavaLibraryPath(effectiveNativeLibsPaths);
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
