package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.service.Service

@Service(
	name="PluginC",
	providedServices= #["al.franzis.cheshire.test.osgi.service.IPlugin"],
	referencedServices=#[],
	properties=#[ "Prop1", "Value1", "Prop2", "Value2"]
)
class PluginC implements IPlugin {
	
	def PluginC() {
		println("PluginC created");
	}
	
	override def void foo() {
		System.out.println("PluginC.foo() called");		
	}
	
}