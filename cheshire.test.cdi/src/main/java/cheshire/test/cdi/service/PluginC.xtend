package cheshire.test.cdi.service

import al.franzis.cheshire.api.service.Service
import al.franzis.cheshire.api.service.IServiceContext
import al.franzis.cheshire.api.service.ServiceActivationMethod

@Service(
	name="PluginC",
	providedServices=#["cheshire.test.cdi.service.IPlugin"]
)
class PluginC implements IPlugin {
	
	new() {
		println("PluginC created");
	}
	
	@ServiceActivationMethod
	def void activate(IServiceContext serviceContext) {
		System.out.println("PluginC.activate() called" );
		System.out.println("PluginC service properties: " + serviceContext.getProperties());
	}
	
	override void foo() {
		println("PluginC.foo() called");
	}
	
}