package al.franzis.cheshire.osgi.proc

import al.franzis.cheshire.api.ModuleContextMethod
import al.franzis.cheshire.api.ModuleStartMethod
import al.franzis.cheshire.api.ModuleStopMethod
import java.util.List
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration

class ModuleActivatorProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		turnIntoOSGiActivator(annotatedClass, context);
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,
		extension CodeGenerationContext context) {
	}
	
	def void turnIntoOSGiActivator(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		// add BundleActivator to implemented interfaces
		val osgiBundleActivatorType = context.newTypeReference(Helpers.CLASSNAME_BUNDLEACTIVATOR)
		val implInterfaces = annotatedClass.implementedInterfaces + #[ osgiBundleActivatorType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		val osgiBundleContextType = context.newTypeReference(Helpers.ClASSNAME_BUNDLECONTEXT)
		
		val startMethodName = findAnnotatedMethod(annotatedClass, ModuleStartMethod).simpleName
		
		var CompilationStrategy startMethodBody
		val moduleContextMethod = findAnnotatedMethod(annotatedClass, ModuleContextMethod);
		if (moduleContextMethod != null) {
			val moduleContextMethodName = moduleContextMethod.simpleName
			
			startMethodBody =  ['''
			try {
      			«moduleContextMethodName»( «Helpers.CLASSNAME_OSGIMODULEFRAMEWORK».getInstance().getOrCreateModule( bundleContext ).getModuleContext() );
      			«startMethodName»();
			} catch(Exception e) {
				throw new RuntimeException("Exception while starting Activator", e);
			}
      		''']
		} else {
			startMethodBody =  ['''
			try {
      			«startMethodName»();
			} catch(Exception e) {
				throw new RuntimeException("Exception while starting Activator", e);
			}
      		''']
		}
		
		val finalStartMethodBody = startMethodBody
		
		val stopMethodName = findAnnotatedMethod(annotatedClass, ModuleStopMethod).simpleName
				
		// add 'BundleActivator.start(BundleContext)' method
		annotatedClass.addMethod("start") [
      		addParameter( "bundleContext", osgiBundleContextType )
      		body = finalStartMethodBody
      	]
      	// add 'BundleActivator.stop(BundleContext)' method
      	annotatedClass.addMethod("stop") [
      		addParameter( "bundleContext", osgiBundleContextType )
      		body = ['''
      		try {
      		«stopMethodName»();
      		} catch(Exception e) {
      			throw new RuntimeException("Exception while stopping Activator", e);
      		}
      		
      		''']
		]    
	}
	
	private def MutableMethodDeclaration findAnnotatedMethod( MutableClassDeclaration annotatedClass, Class<?> annotation ) {
		for ( method : annotatedClass.declaredMethods ) {
			if ( method.annotations.exists( [ m | m.annotationTypeDeclaration.simpleName == annotation.simpleName ]))
				return method
		}
		return null
	}
}