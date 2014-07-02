package al.franzis.cheshire.service

import java.lang.annotation.Target
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.ElementType
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.TransformationContext
import java.util.List
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration

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
}

class ServiceProcessor extends AbstractClassProcessor {
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements, extension CodeGenerationContext context) {
		for (clazz : annotatedSourceElements) {
			val filePath = clazz.compilationUnit.filePath
			val file = filePath.targetFolder.append(clazz.qualifiedName.replace('.', '/') + ".xml")
//			val serviceDefinition = getServiceDefinition(clazz, Service)
			
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
	
	private def Class<? extends IServiceDefinition> getServiceDefinition(ClassDeclaration annotatedClass, Class<?> annotation) {
		val serviceAnnotation = annotatedClass.class.annotations.findFirst( [ a | a.class.simpleName == annotation.simpleName ] ) as Service
		serviceAnnotation.definition
	}
	
}