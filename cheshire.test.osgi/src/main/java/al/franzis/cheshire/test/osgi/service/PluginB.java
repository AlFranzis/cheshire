package al.franzis.cheshire.test.osgi.service;



public class PluginB implements IPlugin {
	
	public PluginB() {
		System.out.println("PluginB created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginB.foo() called");		
	}

}
