package cheshire.test.cdi.service

import java.util.List
import java.util.Map
import al.franzis.cheshire.service.IServiceDefinition

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