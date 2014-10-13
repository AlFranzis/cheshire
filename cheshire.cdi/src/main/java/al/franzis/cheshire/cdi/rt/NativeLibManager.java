package al.franzis.cheshire.cdi.rt;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import al.franzis.cheshire.api.nativecode.ICDIRuntimeEnvironment;
import al.franzis.cheshire.api.nativecode.ICDIRuntimeLibPathProvider;

public class NativeLibManager {
	
	public static String effectiveNativeLibsPaths(ICDIModuleManifest moduleManifest, ICDIRuntimeLibPathProvider runtimeLibPathProvider, String[][][] nativeLibPaths) {
		Set<File> effectiveLibDirPaths = new HashSet<>();
		
		for (String[][] envClause : nativeLibPaths) {
			String[] libDirs = envClause[0];
			
			Map<String,List<String>> envProperties = new HashMap<>();
			for ( int i = 1; i < envClause.length; i++) {
				String[] libEnvProp = envClause[i];
				String propName = libEnvProp[0];
				List<String> propValues = new LinkedList<>();
				for ( int j = 1; j < libEnvProp.length; j++)
				{
					propValues.add(libEnvProp[j]);
				}
				
				envProperties.put(propName, propValues);
				
			}
			ICDIRuntimeEnvironment environment = new DefaultEnvironmentImpl(envProperties);
			
			for ( String libDir : libDirs)
			{
				File effectiveLibPath = runtimeLibPathProvider.getEffectiveLibraryDirectoryPath(moduleManifest, environment, libDir);
				if ( effectiveLibPath != null )
					effectiveLibDirPaths.add(effectiveLibPath);
			}
		}
		return convertToLibraryString(effectiveLibDirPaths);
	}
	
	public static void augmentJavaLibraryPath( String libPath ) {
		if (libPath == null || "".equals(libPath))
			return;
		
		try {
			String javaLibPath = System.getProperty("java.library.path");
			javaLibPath += ";" + libPath;
			System.setProperty("java.library.path", javaLibPath);

			// set sys_paths to null
			final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);
		} catch (Exception e) {
			throw new RuntimeException("Error while augmenting java.library.path property", e);
		}
	}
	
	private static String convertToLibraryString(Collection<File> libDirs) {
		StringBuffer javaLibrariesString = new StringBuffer();
		boolean first = true;
		for ( File libDir : libDirs ) {
			if (!first)
				javaLibrariesString.append(";");
			javaLibrariesString.append(libDir.getPath());
			first = false;
	    }
		return javaLibrariesString.toString().replace("\\", "\\\\");
	}
	
	private static class DefaultEnvironmentImpl implements ICDIRuntimeEnvironment {
		private final Map<String, List<String>> envProps;
		
		public DefaultEnvironmentImpl(Map<String, List<String>> envProps) {
			this.envProps = envProps;
		}
		
		@Override
		public Map<String, List<String>> getProperties() {
			return envProps;
		}
		
	}
}
