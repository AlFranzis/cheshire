package al.franzis.cheshire.osgi.proc

import al.franzis.cheshire.api.service.ServiceActivationMethod
import al.franzis.cheshire.api.service.ServiceBindMethod
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.file.FileLocations
import org.eclipse.xtend.lib.macro.file.FileSystemSupport
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport
import al.franzis.cheshire.api.service.Service
import org.eclipse.xtend.lib.annotations.Data

class ServiceProcessor extends AbstractClassProcessor {
	val StringBuffer logMsgBuf = new StringBuffer;
	
	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		val componentContextType = context.newTypeReference(Helpers.CLASSNAME_COMPONENTCONTEXT)
	
		val List<MethodDeclaration> activationMethods = findAnnotatedMethod(annotatedClass, ServiceActivationMethod);
		if ( !activationMethods.empty ) {
			if(activationMethods.size > 1) {
				logMsgBuf.append("ERROR: Multiple service activation methods!")
			} else {
				val activationMethodName = activationMethods.iterator.next.simpleName
				annotatedClass.addMethod("activate") [
      				addParameter( "componentContext", componentContextType )
      				body = ['''
      				«Helpers.CLASSNAME_OSGISERVICECONTEXT» osgiComponentContext = new «Helpers.CLASSNAME_OSGISERVICECONTEXT»(componentContext);
      				«activationMethodName»(osgiComponentContext);
      				''']
	      		]
	      	}
      	}
      	
      	val serviceInfo = parseServiceDefinition(annotatedClass, Service)
      	
      	if(!serviceInfo.referencedServiceFactories.empty) {
      		val referencedServiceFactory = serviceInfo.referencedServiceFactories.iterator.next
      	
      		val osgiComponentFactoryType = context.newTypeReference("org.osgi.service.component.ComponentFactory")
      		
      		val bindMethodName = referencedServiceFactory.bindMethodName
      		
      		annotatedClass.addMethod(bindMethodName) [
				addParameter( "componentFactory", osgiComponentFactoryType )
      			body = ['''
      			«Helpers.CLASSNAME_OSGISERVICEFACTORY» osgiComponentFactory = new «Helpers.CLASSNAME_OSGISERVICEFACTORY»(componentFactory);
      				«bindMethodName»(osgiComponentFactory);
      			''']
      		]
      	}
      	
      	if(!PathHelper.eclipseEnvironment)
      		createOSGiServiceFile(context, null, context, annotatedClass)
	}

	override doGenerateCode(ClassDeclaration annotatedClass, extension CodeGenerationContext context) {
		if(PathHelper.eclipseEnvironment)
			createOSGiServiceFile(context, context, context, annotatedClass)
			
		val logger = Logger.getLogger( annotatedClass, context)
		logger.info(logMsgBuf.toString)		  	
	}
	
	private def void createOSGiServiceFile(FileLocations fl, extension MutableFileSystemSupport mfss, extension FileSystemSupport fs,
		ClassDeclaration serviceClass) {
		val filePath = serviceClass.compilationUnit.filePath

		val projectPath = fl.getProjectFolder(filePath)
		val osgiInfPath = projectPath.append("OSGI-INF");

		val file = osgiInfPath.append(serviceClass.simpleName + ".xml")

		val serviceInfo = parseServiceDefinition(serviceClass, Service)
		PathHelper.getInstance().setContents(mfss, osgiInfPath, file,
		'''
			<?xml version="1.0" encoding="UTF-8"?>
			<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" 
			«IF !serviceInfo.factory.empty»
			  factory="«serviceInfo.factory»"
			«ENDIF»
			name="«serviceInfo.name»">
						<implementation class="«serviceClass.qualifiedName»"/>
						<service>
							«FOR providedService : serviceInfo.providedServices»
								<provide interface="«providedService»"/>
							«ENDFOR»
						</service>
						
						«FOR refService : serviceInfo.referencedServices»
							<reference bind="«refService.bindMethodName»" cardinality="0..n" interface="«refService.name»" name="IPlugin" policy="static"/>
						«ENDFOR»
						«FOR refServiceFactory : serviceInfo.referencedServiceFactories»
							<reference bind="«refServiceFactory.bindMethodName»" cardinality="0..n" target="(component.factory=«refServiceFactory.name»)" interface="org.osgi.service.component.ComponentFactory" name="IPlugin" policy="static"/>
						«ENDFOR»
			«FOR prop : serviceInfo.properties.entrySet»
				<property name="«prop.key»" type="String" value="«prop.value»"/>					
			«ENDFOR»
			</scr:component>
		''');
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