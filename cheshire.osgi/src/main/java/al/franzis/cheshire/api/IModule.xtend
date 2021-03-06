package al.franzis.cheshire.api

import java.net.URL
import java.util.Enumeration

interface IModule {
	
	def URL getResource( String name )
	
	def Enumeration<URL> getResources( String name )
	
	def String getName()
	
	def String getVersion()
	
	def IModuleContext getModuleContext();
	
}