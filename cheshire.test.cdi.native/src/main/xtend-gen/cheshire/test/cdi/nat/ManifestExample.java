package cheshire.test.cdi.nat;

import al.franzis.cheshire.Module;
import al.franzis.cheshire.cdi.ICDIModuleManifest;
import javax.annotation.PostConstruct;

@Module
@SuppressWarnings("all")
public class ManifestExample implements ICDIModuleManifest {
  public final static String Bundle_Name = "cheshire.test.cdi.native";
  
  public final static String Bundle_Version = "0.0.1.qualifier";
  
  public final static String Bundle_Vendor = "Al Franzis";
  
  public final static String Bundle_ActivationPolicy = "lazy";
  
  public final static String[] Import_Package = { "org.osgi.framework;version=\"1.7.0\"", "org.osgi.service.component;version=\"1.1.0\"" };
  
  public final static String[] Require_Bundle = { "cheshire.osgi", "org.junit;bundle-version=\"4.11.0\"" };
  
  public final static String Bundle_Activator = "al.franzis.cheshire.test.osgi.activator.ModuleActivatorExample";
  
  public final static String[] Bundle_NativeCode = { "/lib/JNIHelloWorld.dll" };
  
  @PostConstruct
  public void init() {
    String nativeLibs = ".\\lib";
    al.franzis.cheshire.NativeLibHandler.augmentJavaLibraryPath(nativeLibs);
  }
}
