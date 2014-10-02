package cheshire.test.cdi.service

import java.util.ArrayList
import java.util.List
import al.franzis.cheshire.api.service.ServiceBindMethod
import al.franzis.cheshire.api.service.Service
import al.franzis.cheshire.api.service.ServiceActivationMethod
import al.franzis.cheshire.api.service.IServiceContext

@Service(
	name="PluginManager2",
	providedServices= #["cheshire.test.cdi.service.IPluginManager"],
	referencedServices=#["cheshire.test.cdi.service.IPlugin"],
	properties=#[ "Prop1", "Value1", "Prop2", "Value2"]
)
class PluginManager2 implements IPluginManager {
	val plugins = new ArrayList<IPlugin>()
	
	new() {
		println("PluginManager2 created")
	}
	
	override List<IPlugin> getPlugins() {
		plugins
	}
	
	@ServiceActivationMethod
	def void activate( IServiceContext serviceContext ) {
		println("PluginManager2.activate() called" );
		println("PluginManager2 service properties: " + serviceContext.getProperties());
	}
	
	@ServiceBindMethod
	def void addPlugin( IPlugin plugin ) {
		plugins.add(plugin)
	}
	
}