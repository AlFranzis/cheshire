package al.franzis.cheshire.service

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.jar.Attributes
import java.util.jar.Manifest
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.file.Path

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
	String name
	String[] referencedServices
	String[] providedServices
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
			context.mkdir( osgiInfPath )
			val file = osgiInfPath.append(clazz.simpleName + ".xml")
			
			val rel = projectPath.relativize(file)
			
			writeManifest(projectPath, rel, context)
			
			val serviceInfo = parseServiceDefinition(clazz,Service)
			file.contents = '''
				<?xml version="1.0" encoding="UTF-8"?>
				<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" name="«serviceInfo.name»">
   				<implementation class="«clazz.qualifiedName»"/>
   				<service>
   					«FOR providedService : serviceInfo.providedServices»
      					<provide interface="«providedService»"/>
      				«ENDFOR»
   				</service>
   				
   				«FOR refService : serviceInfo.referencedServices»
   					<reference bind="«refService.bindMethodName»" cardinality="0..n" interface="«refService.name»" name="IPlugin" policy="static"/>
				«ENDFOR»
				</scr:component>
			'''
			
		}
	}
	
	private def writeManifest(Path projectPath, Path osgiServiceFile, extension CodeGenerationContext context) {
		val manifestFile = projectPath.append("META-INF").append("MANIFEST.MF")

		val InputStream inStream = manifestFile.contentsAsStream
		val Manifest manifest = new Manifest(inStream)
		val Attributes attributes = manifest.mainAttributes
		inStream.close
		
		var serviceComponents = attributes.getValue("Service-Component")
		if ( serviceComponents != null)
			serviceComponents + "," + osgiServiceFile.toString
		else
			serviceComponents = osgiServiceFile.toString
			
		// write manifest values
		attributes.put(new Attributes.Name("Service-Component"), serviceComponents)

		val OutputStream out = new ByteArrayOutputStream()
		manifest.write(out)
		out.close

		val String manifestContent = out.toString
		context.setContents(manifestFile, manifestContent)
	}
	
	private def ServiceInfo parseServiceDefinition(ClassDeclaration annotatedClass, Class<?> annotation) {
		val serviceAnnotation = annotatedClass.annotations.findFirst( [ a | a.annotationTypeDeclaration.simpleName == annotation.simpleName ] ) 

		val serviceName = serviceAnnotation.getValue("name") as String
		println( "Service name: " + serviceName)
		
		val String[] providedServicesNames = serviceAnnotation.getValue("providedServices") as String[];
		println(providedServicesNames)
		
		val String[] referencedServicesNames = serviceAnnotation.getValue("referencedServices") as String[];
		println(referencedServicesNames)
		
		val Map<String,String> bindMethodMap = getBindMethodsMap(annotatedClass)
		val List<ReferencedServiceInfo> referencedServices = referencedServicesNames.map[ sn | 
			val bindMethodName = bindMethodMap.get(sn)
			new ReferencedServiceInfo(sn, bindMethodName)
		]

		val String[] properties = serviceAnnotation.getValue("properties") as String[];
		val Map<String,String> propertiesMap = new HashMap();
	
		var i = -1;
		while(i+2 < properties.length) {
			val k = i + 1;
			val v = i + 2;
			i = i + 2;
			propertiesMap.put( properties.get(k), properties.get(v))
		}
		println( "Service properties: " + propertiesMap)
		
		new ServiceInfo(serviceName, referencedServices, providedServicesNames, propertiesMap)
	}
	
	private def Map<String,String> getBindMethodsMap( ClassDeclaration clazzDeclaration ) {
		val Map<String,String> bindMethodsMap = new HashMap();
		for(MethodDeclaration method : findAnnotatedMethod(clazzDeclaration, ServiceBindMethod)) {
			bindMethodsMap.put(method.parameters.iterator().next.type.name, method.simpleName)
		}
		bindMethodsMap
	}
	
	private def List<MethodDeclaration> findAnnotatedMethod( ClassDeclaration annotatedClass, Class<?> annotation ) {
		val List<? extends MethodDeclaration> annotatedMethods = annotatedClass.declaredMethods.filter([m | m.annotations.exists( [ a|a.annotationTypeDeclaration.simpleName == annotation.simpleName])]).toList
		annotatedMethods as List<MethodDeclaration>
	}
	
}

@Data
class ServiceInfo {
	String name
	ReferencedServiceInfo[] referencedServices
	String[] providedServices
	Map<String,String> properties
}

@Data
class ReferencedServiceInfo {
	String name
	String bindMethodName
}