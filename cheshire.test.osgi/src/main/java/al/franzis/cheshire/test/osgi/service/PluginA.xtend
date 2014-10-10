package al.franzis.cheshire.test.osgi.service

import al.franzis.cheshire.api.service.Service
import al.franzis.cheshire.api.service.ServiceActivationMethod
import al.franzis.cheshire.api.service.IServiceContext

@Service(
	name="PluginA",
	providedServices= #["al.franzis.cheshire.test.osgi.service.IPlugin"]
)
class PluginA implements IPlugin {
	
	new() {
		println("PluginA created");
	}
	
	@ServiceActivationMethod
	def void activate(IServiceContext serviceContext) {
		System.out.println("PluginA.activate() called" );
		System.out.println("PluginA service properties: " + serviceContext.getProperties());
	}
	
	override def void foo() {
		System.out.println("PluginA.foo() called");		
	}
}