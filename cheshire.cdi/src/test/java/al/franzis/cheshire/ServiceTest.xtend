package al.franzis.cheshire

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test

class ServiceTest {
	extension XtendCompilerTester compilerTester = XtendCompilerTester.newXtendCompilerTester()
	
	@Test
	def void testServiceProcessing() {
		'''
			import java.util.ArrayList
			import java.util.List
			import java.util.Map
			import al.franzis.cheshire.service.ServiceBindMethod
			import al.franzis.cheshire.service.Service
			import al.franzis.cheshire.service.IServiceDefinition
			
			public interface IPlugin {
				def void foo();
			}
			
			public interface IPluginManager {
				def List<IPlugin> getPlugins();
			}
			
			class PluginManagerServiceDefinition2 implements IServiceDefinition {
	
				override def String name() {
					"pluginManager"
				}
	
				override def String implementation() {
					"cheshire.test.cdi.service.PluginManager"
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
			
			@Service(definition=typeof(PluginManagerServiceDefinition2), 
				definitionName="PluginManagerServiceDefintion2",
				name="PluginManager2",
				providedServices= #[typeof(IPluginManager)],
				referencedServices=#[typeof(IPlugin)],
				properties=#["Prop1", "Value1", "Prop2", "Value2"])
			class PluginManager2 implements IPluginManager {
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