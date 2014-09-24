package cheshire.test.cdi.service

import al.franzis.cheshire.service.Service

@Service(
	name="PluginC",
	providedServices=#["cheshire.test.cdi.service.IPlugin"],
	referencedServices=#[],
	properties=#["Prop1", "Value1", "Prop2", "Value2"]
)
class PluginC implements IPlugin {
	
	new() {
		println("PluginC created");
	}
	
	override void foo() {
		println("PluginC.foo() called");
	}
	
}