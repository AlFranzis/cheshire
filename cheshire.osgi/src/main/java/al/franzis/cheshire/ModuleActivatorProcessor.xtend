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
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy

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
		turnIntoOSGiActivator(annotatedClass, context);
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,
		extension CodeGenerationContext context) {
	}
	
	def void turnIntoOSGiActivator(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		// add BundleActivator to implemented interfaces
		val osgiBundleActivatorType = context.newTypeReference("org.osgi.framework.BundleActivator")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ osgiBundleActivatorType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		val osgiBundleContextType = context.newTypeReference( "org.osgi.framework.BundleContext" )
		
		val startMethodName = findAnnotatedMethod(annotatedClass, ModuleStartMethod).simpleName
		
		var CompilationStrategy startMethodBody
		val moduleContextMethod = findAnnotatedMethod(annotatedClass, ModuleContextMethod);
		if (moduleContextMethod != null) {
			val moduleContextMethodName = moduleContextMethod.simpleName
			
			startMethodBody =  ['''
			try {
      			«moduleContextMethodName»( al.franzis.cheshire.osgi.OSGiModuleFramework.getInstance().getOrCreateModule( bundleContext ).getModuleContext() );
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