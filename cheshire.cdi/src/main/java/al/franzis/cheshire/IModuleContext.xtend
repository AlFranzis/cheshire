package al.franzis.cheshire

import java.util.List
import al.franzis.cheshire.service.IServiceReference
import java.util.Collection

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