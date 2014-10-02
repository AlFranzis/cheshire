package al.franzis.cheshire.cdi.rt;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import al.franzis.cheshire.api.nativecode.IEnvironmentMatcher;
import al.franzis.cheshire.api.nativecode.ILibPathProvider;
import al.franzis.cheshire.api.nativecode.IRuntimeLibPathProvider;

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
	private ILibPathProvider libPathProvider;
	private List<LibInfo> libs = new LinkedList<>();
	
	public NativeLibHandler(String nativeLibClauses) {
		this.envMatcher = new DefaultEnvironmentMatcher();
		this.libPathProvider = new DefaultLibPathProvider();
		
		parseLibClauses(nativeLibClauses);
	}
	
	public NativeLibHandler(IEnvironmentMatcher envMatcher, ILibPathProvider libPathProvider, String nativeLibClauses) {
		this.envMatcher = envMatcher;
		this.libPathProvider = libPathProvider;
		parseLibClauses(nativeLibClauses);
	}
	
	public String getModuleNativeLibs() throws Exception {
		Set<String> libDirs = new HashSet<>();
		for (LibInfo lib : libs) {
			if (envMatcher.matchesEnvironment(lib.getProperties())) {
				for(String libPath : lib.getLibs()) {
					File effectiveLibPath = getEffectivePath(libPath);
					File libDir = effectiveLibPath.getParentFile();
					libDirs.add(libDir.getPath());
				}
			}
		}
		
		return toStringLiteral(libDirs);
	}
	
	public static String effectiveNativeLibsPaths(ICDIModuleManifest moduleManifest, IRuntimeLibPathProvider runtimeLibPathProvider, String[] nativeLibPaths) {
		Set<File> effectiveLibDirPaths = new HashSet<>();
		for (String nativeLibPath : nativeLibPaths) {
			File effectiveLibPath = runtimeLibPathProvider.getEffectivePath(moduleManifest, nativeLibPath);
			effectiveLibDirPaths.add(effectiveLibPath);
		}
		return convertToLibraryString(effectiveLibDirPaths);
	}
	
	private static String toStringLiteral( Collection<String> ss) {
		StringBuffer buf = new StringBuffer();
		buf.append("new String[] {");
		boolean first = true;
		for(String s : ss) {
			if(!first)
				buf.append(", ");
			buf.append("\"");
			buf.append(s);
			buf.append("\"");
			first = false;
		}
		
		buf.append("}");
		return buf.toString().replace("\\", "\\\\");
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
	
	private File getEffectivePath(String libPath) {
		return libPathProvider.getEffectivePath(libPath);
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
	
	private void parseLibClauses(String nativeLibClauses) {
		if ( nativeLibClauses == null)
			return;
		
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
					info.addLib(clausePart);
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
	
	public static class LibInfo {
		private List<String> libs = new ArrayList<>();
		private Map<String,List<String>> properties = new HashMap<>();
		
		private void addProperty(String propName, String propValue) {
			List<String> propValues = properties.get(propName);
			if (propValues == null) {
				propValues = new LinkedList<>();
				properties.put(propName, propValues);
			}
			propValues.add(propValue);
		}

		private void addLib(String libPath) {
			libs.add(libPath);
		}
		
		private Map<String,List<String>> getProperties() {
			return properties;
		}
		
		private List<String> getLibs() {
			return libs;
		}
	}
	
	public List<LibInfo> getLibs() {
		return libs;
	}
	
}
