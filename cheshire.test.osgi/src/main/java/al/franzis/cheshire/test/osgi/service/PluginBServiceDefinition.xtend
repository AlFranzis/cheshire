package al.franzis.cheshire.test.osgi.service

import java.util.List
import java.util.Map
import al.franzis.cheshire.service.IServiceDefinition

class PluginBServiceDefinition implements IServiceDefinition {
	
	override def String name() {
		"pluginA"
	}
	
	override def String implementation() {
		"cheshire.test.cdi.service.PluginB"
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