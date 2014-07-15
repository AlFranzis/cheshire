package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.service.Service
import al.franzis.cheshire.service.ServiceActivationMethod
import al.franzis.cheshire.service.IServiceContext

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
	
	@ServiceActivationMethod
	def void activate(IServiceContext serviceContext) {
		System.out.println("PluginC.activate() called" );
		System.out.println("PluginC service properties: " + serviceContext.getProperties());
	}
	
	override def void foo() {
		System.out.println("PluginC.foo() called");		
	}
	
}