package cheshire.test.cdi.nat;

public class JNIHelloWorld {
	static {
		System.loadLibrary("JNIHelloWorld"); 
	}

	public native void sayHello();

}
