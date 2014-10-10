package cheshire.test.cdi.service.factory

import al.franzis.cheshire.api.service.IServiceFactory
import java.util.List
import java.util.Arrays
import al.franzis.cheshire.api.service.Service
import al.franzis.cheshire.api.service.ServiceBindMethod

@Service(name="OverlayProvider", providedServices = #["cheshire.test.cdi.service.factory.IOverlayProvider"], referencedServiceFactories = #["overlayFactory"])
class OverlayProvider implements IOverlayProvider {
	IServiceFactory serviceFactory
	
	@ServiceBindMethod
	def void addComponentFactory( IServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory
	}
	
	override def List<IOverlay> getOverlays() {
		val IOverlay overlay = serviceFactory.newInstance as IOverlay
		return Arrays.asList( overlay )
	}
	
}