package al.franzis.cheshire.service;

import java.util.List;
import java.util.Map;

public interface IServiceDefinition {
	
	public String name();

	public String implementation();

	public List<String> referencedServices();

	public List<String> providedServices();
	
	public Map<String,String> properties();
	
}
