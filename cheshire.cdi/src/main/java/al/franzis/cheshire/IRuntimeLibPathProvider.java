package al.franzis.cheshire;

import java.io.File;

import al.franzis.cheshire.cdi.ICDIModuleManifest;

public interface IRuntimeLibPathProvider {
	
	public File getEffectivePath(ICDIModuleManifest moduleManifest, String libPath);
}
