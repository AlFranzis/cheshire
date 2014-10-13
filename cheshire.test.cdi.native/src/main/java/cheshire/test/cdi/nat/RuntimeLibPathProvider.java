package cheshire.test.cdi.nat;

import java.io.File;

import al.franzis.cheshire.api.nativecode.ICDIRuntimeEnvironment;
import al.franzis.cheshire.api.nativecode.ICDIRuntimeLibPathProvider;
import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;

public class RuntimeLibPathProvider implements ICDIRuntimeLibPathProvider {

	@Override
	public File getEffectiveLibraryDirectoryPath(ICDIModuleManifest moduleManifest, ICDIRuntimeEnvironment environment, String libPath) {
		String userDir = System.getProperty("user.dir");
		return new File(userDir, libPath).getParentFile();
	}

}
