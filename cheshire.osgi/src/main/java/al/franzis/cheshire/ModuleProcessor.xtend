package al.franzis.cheshire

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

@Active(ModuleProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation Module {
}

class ModuleProcessor extends AbstractClassProcessor {
	Logger logger

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,extension CodeGenerationContext context) {
		for (clazz : annotatedSourceElements) {
			logger = Logger.getLogger(clazz, context)
			try {
				val Map<String, String> fieldMap = parse(clazz)
				val bundleName = fieldMap.remove("Bundle-Name")
				val serviceDefs = processServiceDefinitions(fieldMap.remove("Service-Component"))
				
				val filePath = clazz.compilationUnit.filePath
				val projectPath = context.getProjectFolder(filePath)
				val metaInfPath = projectPath.append("META-INF")
				val file = metaInfPath.append("MANIFEST.MF")
				file.contents = '''
					Manifest-Version: 1.0
					Bundle-ManifestVersion: 2
					Bundle-Name: «bundleName»
					Bundle-SymbolicName: «bundleName»
					Service-Component: «serviceDefs»
					«FOR entry : fieldMap.entrySet»
						«entry.key»: «entry.value»
					«ENDFOR»
				'''
			} catch (Throwable t) {
				logger.error("Error while processing Module Manifest", t )
				throw t
			}
		}

	}
	
	private def String processServiceDefinitions( String serviceDefinitions ) {
		var sdefs = serviceDefinitions.replaceAll("\\s", "").split(",")
		
		val ss = sdefs.map([s | 
			val idx = s.lastIndexOf(".")
			var unq = s
			if ( idx != -1 )
				unq = s.substring(idx + 1, s.length)
			
			unq = "OSGI-INF/" + unq + ".xml"
		])
		
		flatten(ss)
	}
	
	private def Map<String,String> parse(ClassDeclaration annotatedClass) {
		val Map<String,String> keyValueMap = new HashMap();
		for ( field : annotatedClass.declaredFields) {
			val key = field.simpleName.replace("_", "-")
			
			var fieldValue = field.initializer.toString
			fieldValue = fieldValue.substring(1, fieldValue.length - 1)
			fieldValue = fieldValue.replace("\\\"", "\"")
			var String value =""
			if ( field.type.array) {
				fieldValue = fieldValue.replace("[", "").replace("]","")
				val String[] multiValues = fieldValue.split(",")
				value = flatten(multiValues)
			}
			else {
				value = fieldValue
			}
			
			keyValueMap.put(key, value)
		}
		keyValueMap
	}
	
	private def String flatten(String[] ss) {
		val buf = new StringBuffer();
		var boolean first = true;
		for (String s : ss) {
			var _s = s.trim
			if (_s.startsWith("\"") && _s.endsWith("\""))
				_s = _s.substring(1, _s.length - 1)
			if (!first) {
				buf.append(",\n ")
			}
			buf.append(_s)

			first = false
		}
		buf.toString
	}
	
}