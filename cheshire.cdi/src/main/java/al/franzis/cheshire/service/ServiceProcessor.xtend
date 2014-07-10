package al.franzis.cheshire.service

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration

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
		
		val serviceDefinitionType = context.newTypeReference("al.franzis.cheshire.service.IServiceDefinition")
		val implInterfaces = annotatedClass.implementedInterfaces + #[ serviceDefinitionType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		val cdiModuleFrameworkType = context.newTypeReference("al.franzis.cheshire.cdi.CDIModuleFramework")
		
		annotatedClass.addField("moduleFramework") [
    		type = cdiModuleFrameworkType
      	]
		
		val injectAnnotationType = context.findTypeGlobally("javax.inject.Inject")
		
		annotatedClass.addMethod("setModuleFramework") [
			addAnnotation(injectAnnotationType)
      		addParameter( "moduleFramework", cdiModuleFrameworkType )
      		body = ['''
      		this.moduleFramework = moduleFramework;
      		''']
      	]
      	
      	val postConstructAnnotationType = context.findTypeGlobally("javax.annotation.PostConstruct")
      	
      	annotatedClass.addMethod("init") [
			addAnnotation(postConstructAnnotationType)
      		body = ['''
      		moduleFramework.registerCDIService(this);
      		''']
      	]
      	
      	val serviceInfo = parseServiceDefinition(annotatedClass,Service)
      	
      	var i = 0
      	for ( referencedService : serviceInfo.referencedServices) {
      		val refServiceTypeName = referencedService.name
      		val instancesParameterType = context.newTypeReference(refServiceTypeName)
      		val instancesType = context.newTypeReference("javax.enterprise.inject.Instance", instancesParameterType)
      		
      		val bindMethodName = referencedService.bindMethodName
      		
      		annotatedClass.addMethod("setInstances" + i) [
				addAnnotation(injectAnnotationType)
				addParameter( "instances", instancesType )
      			body = ['''
      				java.util.Iterator<«refServiceTypeName»> it = instances.iterator();
					while(it.hasNext()) {
						«bindMethodName»(it.next());
					}
      			''']
      		]
      		i = i + 1	
      	}
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements, extension CodeGenerationContext context) {
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
	
