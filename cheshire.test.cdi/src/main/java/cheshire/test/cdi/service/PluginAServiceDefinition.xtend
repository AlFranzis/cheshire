package cheshire.test.cdi.service

import java.util.List
import al.franzis.cheshire.service.ICDIServiceDefinition
import java.util.Map

class PluginAServiceDefinition implements ICDIServiceDefinition {
	
	override def String name() {
		"pluginA"
	}
	
	override def String implementation() {
		"cheshire.test.cdi.service.PluginA"
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