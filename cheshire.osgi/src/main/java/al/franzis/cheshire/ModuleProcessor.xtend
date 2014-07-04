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
import java.util.HashMap
import java.util.Map

@Active(ModuleProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
annotation Module {
}

class ModuleProcessor extends AbstractClassProcessor {

	override doTransform(MutableClassDeclaration annotatedClass, extension TransformationContext context) {
		println(context)
	}

	override doGenerateCode(List<? extends ClassDeclaration> annotatedSourceElements,
		extension CodeGenerationContext context) {
			for (clazz : annotatedSourceElements) {
				val Map<String,String> fieldMap = parse(clazz)
				val bundleName = fieldMap.remove("Bundle-Name")
				
				val filePath = clazz.compilationUnit.filePath
				val file = filePath.targetFolder.append("MANIFEST.MF")
				file.contents = '''
					Manifest-Version: 1.0
					Bundle-ManifestVersion: 2
					Bundle-Name: «bundleName»
					Bundle-SymbolicName: «bundleName»
					«FOR entry : fieldMap.entrySet»
						«entry.key»: «entry.value»
					«ENDFOR»
					'''
			}
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
				val buf = new StringBuffer();
				var boolean first = true;
				for(String sv : multiValues) {
					val trimmed = sv.trim
					val s = trimmed.substring(1, trimmed.length-1)
					if ( !first) {
						buf.append(",\n ")
					}
					buf.append(s)
					
					first = false
				}
				value = buf.toString
			}
			else {
				value = fieldValue
			}
			
			keyValueMap.put(key, value)
		}
		keyValueMap
	}
}