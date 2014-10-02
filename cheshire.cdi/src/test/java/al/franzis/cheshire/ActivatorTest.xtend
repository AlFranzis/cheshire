package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test
import al.franzis.cheshire.api.ModuleActivator

class ActivatorTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(ModuleActivator)
	
	@Test
	def void testActivatorProcessing() {
		'''
			import al.franzis.cheshire.ModuleActivator
			import al.franzis.cheshire.ModuleStartMethod
			import al.franzis.cheshire.ModuleStopMethod
			import al.franzis.cheshire.ModuleContextMethod
			import al.franzis.cheshire.IModuleContext
			
			@ModuleActivator
			class ActivatorExample {
				@ModuleStartMethod
				def void start() {
					println("Start")
				}
				@ModuleStopMethod
				def void end() {
					println("end")
				}
				
				@ModuleContextMethod
				def void setModuleContext(IModuleContext cxt) {
				}
			}
		'''.assertCompilesTo(
		'''
			
		''')
	}
}