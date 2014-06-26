package al.franzis.cheshire.cdi;

public class CDIServiceEvent {
	private final Object service;
	
	public CDIServiceEvent( Object service ) {
		this.service = service;
	}
	
	public Object getService() {
		return service;
	}
}
