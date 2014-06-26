package cheshire.test.cdi.service;

import javax.inject.Singleton;

@Singleton
public class PluginA implements IPlugin {

	public PluginA() {
		System.out.println("PluginA created");
	}
	
	@Override
	public void foo() {
		System.out.println("PluginA.foo() called");
	}

}
