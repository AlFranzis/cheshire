package al.franzis.cheshire;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Processor of native library clauses in OSGi style:
 * #[ "/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7",
 *	"/lib/win64/D3DCompiler_43.dll;/lib/win64/D3DX9_43.dll;/lib/win64/VIC.dll;/lib/win64/GPU.dll;processor=x86-64;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista",
 *	"/lib/linux64/libVIC.so;osname=Linux;processor=amd64" ]
 * 
 * @author AXJRD
 *
 */
public class NativeLibHandler {
	private IEnvironmentMatcher envMatcher;
	private List<LibInfo> libs = new LinkedList<>();
	
	public NativeLibHandler(String nativeLibClauses) {
		this.envMatcher = new IEnvironmentMatcher() {
			
			@Override
			public boolean matchesEnvironment(Map<String, List<String>> targetProperties) {
				return true;
			}
		};
		
		parseLibClauses(nativeLibClauses);
	}
	
	public NativeLibHandler(IEnvironmentMatcher envMatcher, String nativeLibClauses) {
		this.envMatcher = envMatcher;
		parseLibClauses(nativeLibClauses);
	}
	
	public String getModuleNativeLibs() throws Exception {
		Set<File> libDirs = new HashSet<>();
		for (LibInfo lib : libs) {
			if (envMatcher.matchesEnvironment(lib.getProperties())) {
				for(String libPath : lib.getLibs().values()) {
					File effectiveLibPath = getEffectivePath(libPath);
					File libDir = effectiveLibPath.getParentFile();
					libDirs.add(libDir);
				}
			}
		}
		
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
	
	private File getEffectivePath(String libPath) {
		return new File(libPath);
	}
	
	public static void augmentJavaLibraryPath( String libPath ) {
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
	
	private void parseLibClauses(String nativeLibClauses) {
		if ( !nativeLibClauses.startsWith("#[") && !nativeLibClauses.endsWith("]") )
			throw new IllegalArgumentException("Invalid format of native clause: " + nativeLibClauses);
		
		String[] libClauses = nativeLibClauses.replace("#[", "").replace("]","").split(",");
		for(String libClause : libClauses) {
			LibInfo info = new LibInfo();
			libClause = libClause.trim();
			libClause = libClause.substring(1,libClause.length()-1);
			String[] clauseParts = libClause.split(";");
			for(String clausePart: clauseParts) {
				if ( isProperty(clausePart) ) {
					String[] p = parseProperty(clausePart);
					info.addProperty(p[0], p[1]);
				} else {
					String[] l = parseLib(clausePart);
					info.addLib(l[0],l[1]);
				}
			}
			libs.add(info);
		}
	}
	
	private boolean isProperty(String clauseAtom) {
		return clauseAtom.contains("=");
	}
	
	private String[] parseProperty(String propertyAtom) {
		return propertyAtom.split("=");
	}
	
	private String[] parseLib(String libAtom) {
		String libName = libAtom.substring(libAtom.lastIndexOf("/") + 1);
		return new String[] { libName, libAtom };
	}
	
	public static class LibInfo {
		private Map<String,String> libs = new HashMap<>();
		private Map<String,List<String>> properties = new HashMap<>();
		
		private void addProperty(String propName, String propValue) {
			List<String> propValues = properties.get(propName);
			if (propValues == null) {
				propValues = new LinkedList<>();
				properties.put(propName, propValues);
			}
			propValues.add(propValue);
		}

		private void addLib(String libName, String libPath) {
			libs.put(libName, libPath);
		}
		
		private Map<String,List<String>> getProperties() {
			return properties;
		}
		
		private Map<String,String> getLibs() {
			return libs;
		}
	}
	
	public List<LibInfo> getLibs() {
		return libs;
	}
	
}
