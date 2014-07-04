package cheshire.test.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIModuleFramework;
import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.Service;

@Service(
		name="PluginB",
		providedServices={"cheshire.test.cdi.service.IPlugin"},
		referencedServices={},
		properties={"Prop1", "Value1", "Prop2", "Value2"}
	)
public class PluginB implements IPlugin, IServiceDefinition {
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
