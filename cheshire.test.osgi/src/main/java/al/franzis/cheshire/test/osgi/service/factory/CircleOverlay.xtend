package al.franzis.cheshire.test.osgi.service.factory

import al.franzis.cheshire.api.service.Service

@Service(name="CircleOverlay", providedServices = #["al.franzis.cheshire.test.osgi.service.factory.IOverlay"], factory= "overlayFactory")
class CircleOverlay implements IOverlay {
	
	override paint() {
		println("CircleOverlay.paint() called")
	}
	
}