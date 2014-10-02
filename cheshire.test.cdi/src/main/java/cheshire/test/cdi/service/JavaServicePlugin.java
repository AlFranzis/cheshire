package cheshire.test.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import al.franzis.cheshire.api.service.IServiceDefinition;
import al.franzis.cheshire.cdi.rt.CDIModuleFramework;
import al.franzis.cheshire.cdi.rt.ServiceImplementation;
import al.franzis.cheshire.api.service.Service;

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
