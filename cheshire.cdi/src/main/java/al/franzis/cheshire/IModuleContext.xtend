package al.franzis.cheshire

import java.util.List

interface IModuleContext {
	
	def IModule getModule()
	
	def IModule getModule( String name )
	
	def List<IModule> getModules()
	
}