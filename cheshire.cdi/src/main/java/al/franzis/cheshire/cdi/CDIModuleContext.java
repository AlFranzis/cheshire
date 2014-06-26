package al.franzis.cheshire.cdi;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.Alternative;

import al.franzis.cheshire.IModule;
import al.franzis.cheshire.IModuleContext;

@Alternative
public class CDIModuleContext implements IModuleContext {
	private IModule cdiModule = new CDIModule();
	
	public IModule getModule() {
		return cdiModule;
	}

	public IModule getModule(String name) {
		return getModule();
	}

	public List<IModule> getModules() {
		return Arrays.asList(cdiModule);
	}

}
