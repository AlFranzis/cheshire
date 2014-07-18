package cheshire.test.cdi.nat

import al.franzis.cheshire.Module

@Module
class ManifestExample {
	public static val String Bundle_Name = "cheshire.test.cdi.native"
	public static val String Bundle_Version = "0.0.1.qualifier"
	public static val String Bundle_Vendor = "Al Franzis"
	public static val String Bundle_ActivationPolicy = "lazy"
	public static val String[] Import_Package = #["org.osgi.framework;version=\"1.7.0\"", "org.osgi.service.component;version=\"1.1.0\""]
	public static val String[] Require_Bundle = #["cheshire.osgi",
 		"org.junit;bundle-version=\"4.11.0\""]
	public static val String Bundle_Activator = "al.franzis.cheshire.test.osgi.activator.ModuleActivatorExample"
	public static val String[] Bundle_NativeCode = 
 	#[ "/lib/JNIHelloWorld.dll" ]
}