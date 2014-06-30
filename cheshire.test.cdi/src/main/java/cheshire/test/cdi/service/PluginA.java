package cheshire.test.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIModuleFramework;

public class PluginA implements IPlugin {
	private CDIModuleFramework moduleFramework;

	public PluginA() {
		System.out.println("PluginA created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginA.foo() called");
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
