package al.franzis.cheshire.test.osgi.activator

import al.franzis.cheshire.ModuleActivator
import al.franzis.cheshire.ModuleStartMethod
import al.franzis.cheshire.ModuleStopMethod
import al.franzis.cheshire.IModuleContext
import java.net.URL
import al.franzis.cheshire.ModuleContextMethod

@ModuleActivator
class ModuleActivatorExample {
	private static IModuleContext moduleContext
	
	@ModuleStartMethod
	def void start() {
		val URL resourceUrl = moduleContext.getModule().getResource( "someResource" )
		
		// do some individual magic
		println( resourceUrl )
	}
	
	@ModuleStopMethod
	def void stop() {
		// nothing to do
	}
	
	@ModuleContextMethod
	def void setModuleContext( IModuleContext context ) {
		ModuleActivatorExample.moduleContext = moduleContext;
	}
	
	def static IModuleContext getModuleContext() {
		return moduleContext;
	}
}