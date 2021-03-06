package al.franzis.cheshire.cdi.proc

import al.franzis.cheshire.api.service.ServiceBindMethod
import al.franzis.cheshire.cdi.rt.ServiceImplementation
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.annotations.Data
import al.franzis.cheshire.api.service.Service

class ServiceProcessor extends AbstractClassProcessor {
	val StringBuffer logMsgBuf = new StringBuffer;
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val serviceDefinitionType = context.newTypeReference(Helpers.CLASSNAME_ISERVICEDEFINITION)
		val implInterfaces = annotatedClass.implementedInterfaces + #[ serviceDefinitionType ]
		annotatedClass.setImplementedInterfaces(implInterfaces)
		
		val serviceImplementationAnnotationType = context.newAnnotationReference(ServiceImplementation)
		annotatedClass.addAnnotation(serviceImplementationAnnotationType)
		
		val cdiModuleFrameworkType = context.newTypeReference(Helpers.CLASSNAME_CDIMODULEFRAMEWORK)
		
		annotatedClass.addField("moduleFramework") [
    		type = cdiModuleFrameworkType
      	]
		
//		val injectAnnotationType = context.findTypeGlobally(Helpers.CLASSNAME_INJECT)
		val injectAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_INJECT)
		
		annotatedClass.addMethod("setModuleFramework") [
			addAnnotation(injectAnnotationType)
      		addParameter( "moduleFramework", cdiModuleFrameworkType )
      		body = ['''
      		this.moduleFramework = moduleFramework;
      		''']
      	]
      	
//      	val postConstructAnnotationType = context.findTypeGlobally(Helpers.CLASSNAME_POSTCONSTRUCT)
      	val postConstructAnnotationType = context.newAnnotationReference(Helpers.CLASSNAME_POSTCONSTRUCT)
      	
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
      		
      		if ( instancesParameterType == null )
      			logMsgBuf.append( "WARN: Referenced service type name not found!\n" )
      		
      		val instancesType = context.newTypeReference(Helpers.CLASSNAME_INSTANCE, instancesParameterType)
      		
      		val bindMethodName = referencedService.bindMethodName
      		
      		annotatedClass.addMethod("setInstances" + i) [
				addAnnotation(injectAnnotationType)
				addParameter( "instances", instancesType ).addAnnotation(serviceImplementationAnnotationType)
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

	override doGenerateCode(List<? extends ClassDeclaration> annotatedClasses, extension CodeGenerationContext context) {
		for ( annotatedClass : annotatedClasses ) {
			val logger = Logger.getLogger( annotatedClass, context)
			logger.info(logMsgBuf.toString)		  	
		}
	}
	
	private def ServiceInfo parseServiceDefinition(ClassDeclaration annotatedClass, Class<?> annotation) {
		try {
			val serviceAnnotation = annotatedClass.annotations.findFirst( [ a | a.annotationTypeDeclaration.simpleName == annotation.simpleName ] ) 

			val serviceName = serviceAnnotation.getValue("name") as String
		
			val String[] providedServicesNames = serviceAnnotation.getValue("providedServices") as String[];
		
			val Map<String,String> bindMethodMap = getBindMethodsMap(annotatedClass)
		
			val String[] referencedServicesNames = serviceAnnotation.getValue("referencedServices") as String[];
			val List<ReferencedServiceInfo> referencedServices = referencedServicesNames.map[ sn | 
				val bindMethodName = bindMethodMap.get(sn)
				new ReferencedServiceInfo(sn, bindMethodName)
			]
			
			val String[] referencedServiceFactoryNames = serviceAnnotation.getValue("referencedServiceFactories") as String[];
			val List<ReferencedServiceFactoryInfo> referencedServiceFactories = referencedServiceFactoryNames.map[ fn | 
				val bindMethodName = bindMethodMap.get("al.franzis.cheshire.api.service.IServiceFactory")
				new ReferencedServiceFactoryInfo(fn, bindMethodName)
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
		
			val factory = serviceAnnotation.getValue("factory") as String
			new ServiceInfo(serviceName, referencedServices, referencedServiceFactories, providedServicesNames, factory, propertiesMap)
		
		} catch ( Throwable t ) {
			logMsgBuf.append("Error: " + t)
			null
		}
	}
	
	private def Map<String,String> getBindMethodsMap( ClassDeclaration clazzDeclaration ) {
		val Map<String,String> bindMethodsMap = new HashMap();
		for(MethodDeclaration method : Helpers.findAnnotatedMethod(clazzDeclaration, ServiceBindMethod)) {
			bindMethodsMap.put(method.parameters.iterator().next.type.name, method.simpleName)
		}
		bindMethodsMap
	}
	
}

@Data
class ServiceInfo {
	String name
	ReferencedServiceInfo[] referencedServices
	ReferencedServiceFactoryInfo[] referencedServiceFactories
	String[] providedServices
	String factory
	Map<String,String> properties
}

@Data
class ReferencedServiceInfo {
	String name
	String bindMethodName
}

@Data
class ReferencedServiceFactoryInfo {
	String name
	String bindMethodName
}
	