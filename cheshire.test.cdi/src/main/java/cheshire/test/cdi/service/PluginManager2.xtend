package cheshire.test.cdi.service

import java.util.ArrayList
import java.util.List
import al.franzis.cheshire.service.ServiceBindMethod
import al.franzis.cheshire.service.Service

@Service(definition=typeof(PluginManagerServiceDefinition2), 
	definitionName="PluginManagerServiceDefintion2",
	name="PluginManager2",
	providedServices= #["cheshire.test.cdi.service.IPluginManager"],
	referencedServices=#["cheshire.test.cdi.service.IPlugin"],
	properties=#[ "Prop1", "Value1", "Prop2", "Value2"]
)
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