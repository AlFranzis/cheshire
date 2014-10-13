package al.franzis.cheshire.cdi.proc

import al.franzis.cheshire.cdi.rt.NativeOSGiLibraryClauseParser
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

class ModuleManifestProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val moduleManifestType = context.newTypeReference(Helpers.CLASSNAME_ICDIModuleManifest)
		val implInterfaces = annotatedClass.implementedInterfaces + #[ moduleManifestType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		annotatedClass.addMethod("getBundleName") [
			returnType = context.newTypeReference(Helpers.CLASSNAME_STRING)
      		body = ['''
      			return Bundle_Name;
      		''']
      	]
		
		val injectAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_INJECT)
		val runtimeLibPathProviderType = context.newTypeReference(Helpers.ClASSNAME_ICDIRuntimeLibPathProvider)
		annotatedClass.addField("libPathProvider") [
			addAnnotation(injectAnnotationType)
    		type = runtimeLibPathProviderType
      	]
      	
      	val nativeClauses = parseNativeClauses(annotatedClass)
		val libManager = new NativeOSGiLibraryClauseParser(nativeClauses)
		val nativeLibs = libManager.toStringLiteral
      	
//		val postConstructAnnotationType = context.findTypeGlobally(Helpers.CLASSNAME_POSTCONSTRUCT)
		val postConstructAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_POSTCONSTRUCT)
      	annotatedClass.addMethod("init") [
			addAnnotation(postConstructAnnotationType)
      		body = ['''
      			String[][][] nativeLibsPaths = «nativeLibs»;
      			String effectiveNativeLibsPathDirs = «Helpers.CLASSNAME_NATIVELIBMANAGER».effectiveNativeLibsPaths(this, libPathProvider, nativeLibsPaths);
      			«Helpers.CLASSNAME_NATIVELIBMANAGER».augmentJavaLibraryPath(effectiveNativeLibsPathDirs);
      		''']
      	]
	}

	override doGenerateCode(ClassDeclaration annotatedClass, extension CodeGenerationContext context) {}
	
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
