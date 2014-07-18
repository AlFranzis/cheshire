package al.franzis.cheshire;

import java.util.List;
import java.util.Map;

public interface IEnvironmentMatcher {
	
	public boolean matchesEnvironment(Map<String,List<String>> targetProperties);
	
}
