package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test
import al.franzis.cheshire.service.ServiceProcessor

class ServiceTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester(ServiceProcessor)
	
	@Test
	def void testServiceProcessing() {
		'''
			import java.util.ArrayList
			import java.util.List
			import al.franzis.cheshire.service.ServiceBindMethod
			import al.franzis.cheshire.service.Service
			
			class PluginManagerServiceDefinition2 implements IServiceDefinition {
	
				override def String name() {
					"pluginManager2"
				}
	
				override def String implementation() {
					"cheshire.test.cdi.service.PluginManager2"
				}
	
				override def List<String> referencedServices() {
					#["cheshire.test.cdi.service.IPlugin"]
				}
	
				override def List<String> providedServices() {
					#["cheshire.test.cdi.service.IPluginManager"]
				}
	
				override def Map<String,String> properties() {
					#{ "Prop1" -> "Value1", "Prop2" -> "Value2"}
				}
			}

			@Service(definition=PluginManagerServiceDefinition2)
			class PluginManager2 {
				val plugins = new ArrayList<IPlugin>()
	
				override List<IPlugin> getPlugins() {
					plugins
				}
	
				@ServiceBindMethod
				def void addPlugin( IPlugin plugin ) {
					plugins.add(plugin)
				}	
			}
		'''.assertCompilesTo(
		'''
			
		''')
	}
}