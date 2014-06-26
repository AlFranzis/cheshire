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
				val filePath = clazz.compilationUnit.filePath
				
//				for ( fieldDec : clazz.declaredFields ) {
//					val fieldName = fieldDec.simpleName
//					val v = fieldDec.initializer
//					val field = clazz.class.getDeclaredField( fieldName )
//					val value = field.get( clazz.class )
//					println("Value: " + value)
//				}
				
				val file = filePath.targetFolder.append(clazz.qualifiedName.replace('.', '/') + ".properties")
				file.contents = '''
					«FOR field : clazz.declaredFields»
						«field.simpleName» : «field.initializer.toString»
					«ENDFOR»
					'''
			}
	}
}