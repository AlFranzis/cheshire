package cheshire.test.cdi.service

import al.franzis.cheshire.api.service.Service

@Service(
	name="PluginD",
	providedServices=#["cheshire.test.cdi.service.IPlugin"]
)
class PluginD implements IPlugin {
	
	new() {
		println("PluginD created");
	}
	
	override void foo() {
		println("PluginD.foo() called");
	}
	
}