package al.franzis.cheshire.test.osgi.activator

import al.franzis.cheshire.api.IModuleContext
import al.franzis.cheshire.api.ModuleActivator
import al.franzis.cheshire.api.ModuleContextMethod
import al.franzis.cheshire.api.ModuleStartMethod
import al.franzis.cheshire.api.ModuleStopMethod
import java.net.URL

@ModuleActivator
class ModuleActivatorExample {
	private static IModuleContext moduleContext
	public static URL loadedResource;
	
	@ModuleStartMethod
	def void start() {
		val URL resourceUrl = ModuleActivatorExample.moduleContext.getModule().getResource( "someResource" )
		loadedResource = resourceUrl
	}
	
	@ModuleStopMethod
	def void stop() {
		// nothing to do
	}
	
	@ModuleContextMethod
	def void setModuleContext( IModuleContext context ) {
		ModuleActivatorExample.moduleContext = context;
	}
	
	def static IModuleContext getModuleContext() {
		return moduleContext;
	}
}