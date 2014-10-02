package al.franzis.cheshire.test.osgi.service

import java.util.ArrayList
import java.util.List
import al.franzis.cheshire.api.service.ServiceBindMethod
import al.franzis.cheshire.api.service.Service
import al.franzis.cheshire.api.service.IServiceContext
import al.franzis.cheshire.api.service.ServiceActivationMethod

@Service(
	name="PluginManager2",
	providedServices= #["al.franzis.cheshire.test.osgi.service.IPluginManager"],
	referencedServices=#["al.franzis.cheshire.test.osgi.service.IPlugin"],
	properties=#[ "Prop1", "Value1", "Prop2", "Value2"]
)
class PluginManager2 implements IPluginManager {
	val plugins = new ArrayList<IPlugin>()
	
	def PluginManager2() {
		System.out.println("PluginManager2 created");
	}
	
	@ServiceActivationMethod
	def void activate(IServiceContext serviceContext) {
		System.out.println("PluginManager2.activate() called" );
		System.out.println("PluginManager2 service properties: " + serviceContext.getProperties());
	}
	
	override List<IPlugin> getPlugins() {
		plugins
	}
	
	@ServiceBindMethod
	def void addPlugin(IPlugin plugin) {
		plugins.add(plugin)
	}
	
}