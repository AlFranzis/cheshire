package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.service.Service

@Service(definition=PluginCServiceDefinition)
class PluginC implements IPlugin {
	
	def PluginC() {
		println("PluginC created");
	}
	
	override def void foo() {
		System.out.println("PluginC.foo() called");		
	}
	
}