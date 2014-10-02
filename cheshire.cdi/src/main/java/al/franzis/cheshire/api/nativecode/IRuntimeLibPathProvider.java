package al.franzis.cheshire.api.nativecode;

import java.io.File;

import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;

public interface IRuntimeLibPathProvider {
	
	public File getEffectivePath(ICDIModuleManifest moduleManifest, String libPath);
}
