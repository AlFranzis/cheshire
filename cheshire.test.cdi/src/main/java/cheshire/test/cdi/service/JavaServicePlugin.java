package cheshire.test.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import al.franzis.cheshire.cdi.CDIModuleFramework;
import al.franzis.cheshire.service.IServiceDefinition;
import al.franzis.cheshire.service.Service;
import al.franzis.cheshire.service.ServiceImplementation;

@Service(
	name="JavaServicePlugin",
	providedServices={"cheshire.test.cdi.service.IPlugin"},
	referencedServices={},
	properties={"Prop1", "Value1", "Prop2", "Value2"}
)
@ServiceImplementation
public class JavaServicePlugin implements IPlugin, IServiceDefinition {
	private CDIModuleFramework moduleFramework;

	public JavaServicePlugin() {
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
