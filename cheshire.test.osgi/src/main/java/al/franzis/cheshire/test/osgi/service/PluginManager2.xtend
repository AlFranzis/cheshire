package al.franzis.cheshire.test.osgi.service

import java.util.ArrayList
import java.util.List
import al.franzis.cheshire.service.ServiceBindMethod
import al.franzis.cheshire.service.Service

@Service(definition=PluginManagerServiceDefinition2)
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