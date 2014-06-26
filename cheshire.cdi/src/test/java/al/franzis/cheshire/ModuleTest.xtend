package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test

class ModuleTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(Module)
	
	@Test
	def void testManifestProcessing() {
		'''
			import al.franzis.cheshire.Module
			
			@Module
			class ManifestExample {
				val bundleName = "bundleABC"
				val exportedPackages = #[ "com", "a"]
			}
		'''.assertCompilesTo(
		'''
			
		''')
	}
}