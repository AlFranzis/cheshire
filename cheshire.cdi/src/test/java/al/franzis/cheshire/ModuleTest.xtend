package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test
import al.franzis.cheshire.api.ModuleManifest

class ModuleTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(ModuleManifest)
	
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