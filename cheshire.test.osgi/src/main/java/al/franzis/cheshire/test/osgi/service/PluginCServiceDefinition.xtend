package al.franzis.cheshire.test.osgi.service

import java.util.List
import java.util.Map
import al.franzis.cheshire.service.IServiceDefinition

class PluginCServiceDefinition implements IServiceDefinition {
	
	override def String name() {
		"pluginC"
	}
	
	override def String implementation() {
		"cheshire.test.cdi.service.PluginC"
	}
	
	override def List<String> referencedServices() {
		#[]
	}
	
	override def List<String> providedServices() {
		#["cheshire.test.cdi.service.IPlugin"]
	}
	
	override def Map<String,String> properties() {
		#{ "Prop1" -> "Value1", "Prop2" -> "Value2"}
	}
}