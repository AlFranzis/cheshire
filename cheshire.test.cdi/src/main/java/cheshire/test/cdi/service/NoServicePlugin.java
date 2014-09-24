package cheshire.test.cdi.service;


public class NoServicePlugin implements IPlugin {
	
	public NoServicePlugin() {
		System.out.println("PluginB created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginB.foo() called");		
	}
	
}
