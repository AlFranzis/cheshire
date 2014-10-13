package al.franzis.cheshire;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import al.franzis.cheshire.api.nativecode.ICDIRuntimeEnvironment;
import al.franzis.cheshire.api.nativecode.ICDIRuntimeLibPathProvider;
import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;
import al.franzis.cheshire.cdi.rt.NativeLibManager;

public class NativeLibManagerTest {
	
	@Test
	public void testNativeLibManager() throws Exception {
		String[][][] libString = new String[][][] { 
				{ { "lib32", "lib" }, { "osnname", "windows xp", "windows 7"} },
				{ { "linux", "lib" }, { "osnname", "Linux"}, {"processor", "x86" } }	
		};

		ICDIModuleManifest moduleManifest = new ICDIModuleManifest() {
			
			@Override
			public String getBundleName() {
				return "module";
			}
		};
		
		ICDIRuntimeLibPathProvider runtimeLibPathProvider = new ICDIRuntimeLibPathProvider() {
			
			@Override
			public File getEffectiveLibraryDirectoryPath(ICDIModuleManifest moduleManifest,
					ICDIRuntimeEnvironment libEnvironment, String libPath) {
				return null;
			}
		};
		
		String effectiveLibDirs = NativeLibManager.effectiveNativeLibsPaths(moduleManifest, runtimeLibPathProvider, libString);
		
		Assert.assertNotNull(effectiveLibDirs);
		
	}

}
