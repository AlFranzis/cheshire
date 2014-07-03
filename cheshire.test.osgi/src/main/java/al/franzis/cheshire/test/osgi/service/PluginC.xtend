package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.service.Service

@Service(
	name="PluginManager2",
	providedServices= #["cheshire.test.cdi.service.IPlugin"],
	referencedServices=#["cheshire.test.cdi.service.IPlugin"],
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