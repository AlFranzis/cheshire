package cheshire.test.cdi.service.factory

import al.franzis.cheshire.api.service.Service

@Service(name="CircleOverlay", providedServices = #["cheshire.test.cdi.service.factory.IOverlay"], factory= "overlayFactory")
class CircleOverlay implements IOverlay {
	
	override paint() {
		println("CircleOverlay.paint() called")
	}
	
}