package cheshire.test.cdi.nat;

import al.franzis.cheshire.api.Module;
import al.franzis.cheshire.api.nativecode.IRuntimeLibPathProvider;
import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Module
@SuppressWarnings("all")
public class ManifestExample implements ICDIModuleManifest {
  public final static String Bundle_Name = "cheshire.test.cdi.native";
  
  public final static String Bundle_Version = "0.0.1.qualifier";
  
  public final static String[] Bundle_NativeCode = { "/lib/JNIHelloWorld.dll" };
  
  public String getBundleName() {
    return Bundle_Name;
  }
  
  @Inject
  private IRuntimeLibPathProvider libPathProvider;
  
  @PostConstruct
  public void init() {
    String[] nativeLibsPaths = new String[] {".\\lib"};
    String effectiveNativeLibsPaths = al.franzis.cheshire.cdi.rt.NativeLibHandler.effectiveNativeLibsPaths(this, libPathProvider, nativeLibsPaths);
    al.franzis.cheshire.cdi.rt.NativeLibHandler.augmentJavaLibraryPath(effectiveNativeLibsPaths);
  }
}
