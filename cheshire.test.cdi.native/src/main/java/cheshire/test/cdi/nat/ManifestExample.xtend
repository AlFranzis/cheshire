package cheshire.test.cdi.nat

import al.franzis.cheshire.Module

@Module
class ManifestExample {
	public static val String Bundle_Name = "cheshire.test.cdi.native"
	public static val String Bundle_Version = "0.0.1.qualifier"
	
	public static val String[] Bundle_NativeCode = 
 	#[ "/lib/JNIHelloWorld.dll" ]
}