package cheshire.test.cdi.service

import al.franzis.cheshire.service.Service

@Service(
	name="PluginD",
	providedServices=#["cheshire.test.cdi.service.IPlugin"],
	referencedServices=#[],
	properties=#["Prop1", "Value1", "Prop2", "Value2"]
)
class PluginD implements IPlugin {
	
	new() {
		println("PluginD created");
	}
	
	override void foo() {
		println("PluginD.foo() called");
	}
	
}