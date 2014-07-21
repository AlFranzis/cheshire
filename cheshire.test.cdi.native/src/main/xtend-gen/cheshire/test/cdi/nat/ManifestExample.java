package cheshire.test.cdi.nat;

import al.franzis.cheshire.Module;
import al.franzis.cheshire.cdi.ICDIModuleManifest;
import javax.annotation.PostConstruct;

@Module
@SuppressWarnings("all")
public class ManifestExample implements ICDIModuleManifest {
  public final static String Bundle_Name = "cheshire.test.cdi.native";
  
  public final static String Bundle_Version = "0.0.1.qualifier";
  
  public final static String[] Bundle_NativeCode = { "/lib/JNIHelloWorld.dll" };
  
  @PostConstruct
  public void init() {
    String nativeLibs = ".\\lib";
    al.franzis.cheshire.NativeLibHandler.augmentJavaLibraryPath(nativeLibs);
  }
}
