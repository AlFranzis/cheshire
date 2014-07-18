package cheshire.test.cdi.helloworld;

import org.jboss.weld.environment.se.StartMain;

import cheshire.test.cdi.nat.JNIHelloWorld;

public class HelloWorldStarter {
	
	public static void main(String[] args) {
		StartMain.main(args);
		useJNI();
	}

	private static void useJNI() {
		JNIHelloWorld hw = new JNIHelloWorld();
		hw.sayHello();
	}
	
}
