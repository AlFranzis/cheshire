package cheshire.test.cdi.helloworld;


import org.jboss.weld.environment.se.StartMain;
import org.junit.Assert;
import org.junit.Test;

import cheshire.test.cdi.nat.JNIHelloWorld;

public class CDIModuleNativeTest {
	
	@Test
	public void testCDIModuleContainingNativeLib() {
		StartMain.main(null);
		String resultString = useJNI();
		Assert.assertEquals("Hello World!\n", resultString);
	}

	private static String useJNI() {
		JNIHelloWorld hw = new JNIHelloWorld();
		return hw.sayHello();
	}
	
}
