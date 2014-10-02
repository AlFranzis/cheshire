package al.franzis.cheshire.cdi.proc

import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import java.util.List
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import al.franzis.cheshire.cdi.rt.ICDIModuleManifest
import al.franzis.cheshire.api.nativecode.IRuntimeLibPathProvider
import al.franzis.cheshire.cdi.rt.NativeLibHandler
import al.franzis.cheshire.api.service.IServiceDefinition
import al.franzis.cheshire.cdi.rt.CDIModuleFramework
import javax.enterprise.inject.Instance
import javax.inject.Inject
import javax.annotation.PostConstruct

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
	
	public static val String CLASSNAME_ICDIModuleManifest = ICDIModuleManifest.canonicalName
	public static val String ClASSNAME_IRuntimeLibPathProvider = IRuntimeLibPathProvider.canonicalName
	public static val String CLASSNAME_STRING = String.canonicalName
	public static val String CLASSNAME_INJECT = Inject.canonicalName
	public static val String CLASSNAME_POSTCONSTRUCT = PostConstruct.canonicalName
	public static val String CLASSNAME_NATIVELIBHANDLER = NativeLibHandler.canonicalName
	public static val String CLASSNAME_ISERVICEDEFINITION = IServiceDefinition.canonicalName
	public static val String CLASSNAME_CDIMODULEFRAMEWORK = CDIModuleFramework.canonicalName
	public static val String CLASSNAME_INSTANCE = Instance.canonicalName
	
}
	