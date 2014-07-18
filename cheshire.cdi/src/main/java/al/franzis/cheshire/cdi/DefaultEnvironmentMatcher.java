package al.franzis.cheshire.cdi;

import java.util.List;
import java.util.Map;

import al.franzis.cheshire.IEnvironmentMatcher;

public class DefaultEnvironmentMatcher implements IEnvironmentMatcher {

	@Override
	public boolean matchesEnvironment(Map<String, List<String>> targetProperties) {
		return true;
	}

}
