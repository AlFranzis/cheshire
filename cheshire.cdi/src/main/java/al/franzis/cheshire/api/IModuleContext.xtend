package al.franzis.cheshire.api

import java.util.List
import java.util.Collection
import al.franzis.cheshire.api.service.IServiceReference

interface IModuleContext {
	
	def IModule getModule()
	
	def IModule getModule( String name )
	
	def List<IModule> getModules()
	
	def <S> void registerService(Class<S> serviceClass, S service)
	
	def <S> IServiceReference<S> getServiceReference(Class<S> clazz)
	
	def <S> Collection<IServiceReference<S>> getServiceReferences(Class<S> clazz)
	
	def <S> S getService(IServiceReference<S> serviceReference)
	
	def void ungetService(IServiceReference<?> serviceReference)
	
}