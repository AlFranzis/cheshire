package al.franzis.cheshire.api.nativecode;

import java.io.File;

import al.franzis.cheshire.cdi.rt.ICDIModuleManifest;

public interface ICDIRuntimeLibPathProvider {
	
	/**
	 * Returns the effective file-system directory that contains a given native library. The directory will be added to the Java Library Path
	 * (System property <code>java.library.path</code>)
	 * @param moduleManifest Manifest of the Module to which the native library belongs to
	 * @param libEnvironment The library environment properties
	 * @param libPath The library path as specified in the Module-Manifest
	 * @return Returns the directory containing the native library, returns <code>null</code> if the native library should not be available.
	 */
	public File getEffectiveLibraryDirectoryPath(ICDIModuleManifest moduleManifest, ICDIRuntimeEnvironment libEnvironment, String libPath);
	
}
