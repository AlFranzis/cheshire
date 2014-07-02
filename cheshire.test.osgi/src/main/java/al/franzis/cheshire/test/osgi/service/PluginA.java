package al.franzis.cheshire.test.osgi.service;

import al.franzis.cheshire.service.Service;

public class PluginA implements IPlugin {

	public PluginA() {
		System.out.println("PluginA created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginA.foo() called");
	}
	
	

}
