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
import org.eclipse.xtext.common.types.JvmType

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
	Class<? extends IServiceDefinition> definition
	String definitionName
	String name
	Class<?>[] referencedServices
	Class<?>[] providedServices
	String[] properties
}

class ServiceProcessor extends AbstractClassProcessor {
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements, extension CodeGenerationContext context) {
		for (clazz : annotatedSourceElements) {
			val filePath = clazz.compilationUnit.filePath
			
			val projectPath = context.getProjectFolder(filePath)
			val osgiInfPath = projectPath.append("OSGI-INF");
			val success = context.mkdir( osgiInfPath )
			val file = osgiInfPath.append(clazz.simpleName + ".xml")
			
//			val file = filePath.targetFolder.append(clazz.qualifiedName.replace('.', '/') + ".xml")
			parseServiceDefinition(clazz,Service)
			file.contents = '''
				<?xml version="1.0" encoding="UTF-8"?>
				<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" name="«clazz.qualifiedName»">
   				<implementation class="«clazz.qualifiedName»"/>
   				<service>
      				<provide interface="s"/>
   				</service>
   				<reference bind="addPlugin" cardinality="0..n" interface="al.franzis.cheshire.test.osgi.service.IPlugin" name="IPlugin" policy="static"/>
				</scr:component>
			'''
		
		
		}
	}
	
	private def void parseServiceDefinition(ClassDeclaration annotatedClass, Class<?> annotation) {
		val serviceAnnotation = annotatedClass.annotations.findFirst( [ a | a.annotationTypeDeclaration.simpleName == annotation.simpleName ] ) 

		val serviceName = serviceAnnotation.getValue("name") as String
		println( "Service name: " + serviceName)
		
//		val JvmType[] providedServices = serviceAnnotation.getValue("providedServices") as JvmType[];
//		for ( JvmType providedService : providedServices) {
//			println( providedService.qualifiedName )
//		}
//		
//		val JvmType[] referencedServices = serviceAnnotation.getValue("referencedServices") as JvmType[];
//		for ( JvmType referencedService : referencedServices) {
//			println( referencedService.qualifiedName )
//		}
//		
//		val String[] properties = serviceAnnotation.getValue("properties") as String[];
//		println( "Service properties: " + properties)
	}
	
	
	
	
}