package al.franzis.cheshire.test.osgi

import al.franzis.cheshire.api.ModuleManifest

@ModuleManifest
class ManifestExample {
	public static val String Bundle_Name = "cheshire.test.osgi"
	public static val String Bundle_Version = "0.0.1.qualifier"
	public static val String Bundle_Vendor = "Al Franzis"
	public static val String Bundle_ActivationPolicy = "lazy"
	public static val String[] Import_Package = #["org.osgi.framework;version=\"1.7.0\"", "org.osgi.service.component;version=\"1.1.0\""]
	public static val String[] Require_Bundle = #["cheshire.osgi",
 		"org.junit;bundle-version=\"4.11.0\""]
	public static val String Bundle_Activator = "al.franzis.cheshire.test.osgi.activator.ModuleActivatorExample"
	public static val String[] Service_Component = #["al.franzis.cheshire.test.osgi.service.PluginA",
 		"al.franzis.cheshire.test.osgi.service.PluginManager2",
 		"al.franzis.cheshire.test.osgi.service.PluginC",
 		"al.franzis.cheshire.test.osgi.service.factory.OverlayProvider",
 		"al.franzis.cheshire.test.osgi.service.factory.CircleOverlay"]
}