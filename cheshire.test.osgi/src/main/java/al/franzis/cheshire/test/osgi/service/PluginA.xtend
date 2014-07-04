package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.service.Service

@Service(
	name="PluginA",
	providedServices= #["al.franzis.cheshire.test.osgi.service.IPlugin"],
	referencedServices=#[],
	properties=#[ "Prop1", "Value1", "Prop2", "Value2"]
)
class PluginA implements IPlugin {
	
	def PluginA() {
		println("PluginA created");
	}
	
	override def void foo() {
		System.out.println("PluginA.foo() called");		
	}
}