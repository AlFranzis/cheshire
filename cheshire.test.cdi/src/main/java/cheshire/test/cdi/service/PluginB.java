package cheshire.test.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIModuleFramework;

public class PluginB implements IPlugin {
	private CDIModuleFramework moduleFramework;
	
	public PluginB() {
		System.out.println("PluginB created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginB.foo() called");		
	}
	
	@Inject
	private void setModuleFramework(CDIModuleFramework moduleFramework) {
		this.moduleFramework = moduleFramework;
	}

	// GEN
	@PostConstruct
	private void init() {
		moduleFramework.registerCDIService(this);
	}

}
