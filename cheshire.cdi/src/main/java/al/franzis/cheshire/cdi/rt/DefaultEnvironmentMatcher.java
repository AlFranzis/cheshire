package al.franzis.cheshire.cdi.rt;

import java.util.List;
import java.util.Map;

import al.franzis.cheshire.api.nativecode.IEnvironmentMatcher;

public class DefaultEnvironmentMatcher implements IEnvironmentMatcher {

	@Override
	public boolean matchesEnvironment(Map<String, List<String>> targetProperties) {
		return true;
	}

}
