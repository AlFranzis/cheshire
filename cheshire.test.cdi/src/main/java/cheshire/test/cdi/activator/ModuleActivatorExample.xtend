package cheshire.test.cdi.activator

import al.franzis.cheshire.api.ModuleActivator
import al.franzis.cheshire.api.ModuleStartMethod
import al.franzis.cheshire.api.ModuleStopMethod
import al.franzis.cheshire.api.IModuleContext
import java.net.URL
import al.franzis.cheshire.api.ModuleContextMethod

@ModuleActivator
class ModuleActivatorExample {
	private static IModuleContext moduleContext
	public static URL loadedResource;
	
	@ModuleStartMethod
	def void start() {
		val URL resourceUrl = moduleContext.getModule().getResource( "/resource.xml" )
		// do some individual magic
		println( resourceUrl )
		loadedResource = resourceUrl
	}
	
	@ModuleStopMethod
	def void stop() {
		// nothing to do
	}
	
	@ModuleContextMethod
	def void setModuleContext( IModuleContext moduleContext ) {
		ModuleActivatorExample.moduleContext = moduleContext;
	}
	
	def static IModuleContext getModuleContext() {
		return moduleContext;
	}
}