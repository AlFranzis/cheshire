package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test

class ActivatorTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(ModuleActivator)
	
	@Test
	def void testCompiler() {
		'''
			import al.franzis.cheshire.ModuleActivator
			import al.franzis.cheshire.ModuleStartMethod
			
			@ModuleActivator
			class ActivatorExample {
				@ModuleStartMethod
				def void start() {
					println("Start"
				}
			}
		'''.assertCompilesTo(
		'''
			
		''')
	}
}