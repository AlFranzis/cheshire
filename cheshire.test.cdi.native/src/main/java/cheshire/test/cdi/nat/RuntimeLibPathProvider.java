package cheshire.test.cdi.nat;

import java.io.File;

import al.franzis.cheshire.IRuntimeLibPathProvider;
import al.franzis.cheshire.cdi.ICDIModuleManifest;

public class RuntimeLibPathProvider implements IRuntimeLibPathProvider {

	@Override
	public File getEffectivePath(ICDIModuleManifest moduleManifest, String libPath) {
		String userDir = System.getProperty("user.dir");
		return new File(userDir, libPath);
	}

}
