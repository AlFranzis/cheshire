package al.franzis.cheshire.api.nativecode;

import java.util.List;
import java.util.Map;

public interface IEnvironmentMatcher {
	
	public boolean matchesEnvironment(Map<String,List<String>> targetProperties);
	
}
