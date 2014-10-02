package al.franzis.cheshire.osgi.proc

import al.franzis.cheshire.osgi.rt.OSGiModuleFramework
import java.util.List
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import al.franzis.cheshire.osgi.rt.OSGiServiceContext

class Helpers {
	
	static def List<MethodDeclaration> findAnnotatedMethod( ClassDeclaration annotatedClass, Class<?> annotation ) {
		val List<? extends MethodDeclaration> annotatedMethods = annotatedClass.declaredMethods.filter([m | m.annotations.exists( [ a|a.annotationTypeDeclaration.simpleName == annotation.simpleName])]).toList
		annotatedMethods as List<MethodDeclaration>
	}
	
	static def MutableMethodDeclaration findAnnotatedMethod( MutableClassDeclaration annotatedClass, Class<?> annotation ) {
		for ( method : annotatedClass.declaredMethods ) {
			if ( method.annotations.exists( [ m | m.annotationTypeDeclaration.simpleName == annotation.simpleName ]))
				return method
		}
		return null
	}
	
	public static val String CLASSNAME_BUNDLEACTIVATOR = BundleActivator.canonicalName
	public static val String ClASSNAME_BUNDLECONTEXT = BundleContext.canonicalName
	public static val String CLASSNAME_STRING = String.canonicalName
	public static val String CLASSNAME_OSGIMODULEFRAMEWORK= OSGiModuleFramework.canonicalName
	// use String not class reference as ComponentContext class is optional dependency and might not be available at runtime
	public static val String CLASSNAME_COMPONENTCONTEXT = "org.osgi.service.component.ComponentContext"
	public static val String CLASSNAME_OSGISERVICECONTEXT = OSGiServiceContext.canonicalName
	
	
}
	