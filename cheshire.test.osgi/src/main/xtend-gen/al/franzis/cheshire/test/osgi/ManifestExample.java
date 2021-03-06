package al.franzis.cheshire.test.osgi;

import al.franzis.cheshire.api.ModuleManifest;

@ModuleManifest
@SuppressWarnings("all")
public class ManifestExample {
  public final static String Bundle_Name = "cheshire.test.osgi";
  
  public final static String Bundle_Version = "0.0.1.qualifier";
  
  public final static String Bundle_Vendor = "Al Franzis";
  
  public final static String Bundle_ActivationPolicy = "lazy";
  
  public final static String[] Import_Package = { "org.osgi.framework;version=\"1.7.0\"", "org.osgi.service.component;version=\"1.1.0\"" };
  
  public final static String[] Require_Bundle = { "cheshire.osgi", "org.junit;bundle-version=\"4.11.0\"" };
  
  public final static String Bundle_Activator = "al.franzis.cheshire.test.osgi.activator.ModuleActivatorExample";
  
  public final static String[] Service_Component = { "al.franzis.cheshire.test.osgi.service.PluginA", "al.franzis.cheshire.test.osgi.service.PluginManager2", "al.franzis.cheshire.test.osgi.service.PluginC", "al.franzis.cheshire.test.osgi.service.factory.OverlayProvider", "al.franzis.cheshire.test.osgi.service.factory.CircleOverlay" };
}
