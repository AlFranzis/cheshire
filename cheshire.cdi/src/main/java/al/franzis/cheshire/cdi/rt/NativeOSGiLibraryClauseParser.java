package al.franzis.cheshire.cdi.rt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
public class NativeOSGiLibraryClauseParser {
	private List<LibInfo> libs = new LinkedList<>();
	
	public NativeOSGiLibraryClauseParser(String nativeLibClauses) {
		parseLibClauses(nativeLibClauses);
	}
	
	public String toStringLiteral() {
		StringBuffer buf = new StringBuffer();
		buf.append("new String[][][] {");
		boolean first = true;
		for (LibInfo lib : libs) {
			if (!first)
				buf.append(", ");
			buf.append("{");

			buf.append("{");
			boolean firstLibPath = true;
			for (String libPath : lib.getLibs()) {
				if (!firstLibPath)
					buf.append(", ");

				buf.append("\"");
				buf.append(libPath);
				buf.append("\"");

				firstLibPath = false;
			}
			buf.append("}");

			for (Entry<String, List<String>> propEntry : lib.getProperties().entrySet()) {
				buf.append(", ");
				buf.append("{");
				
				buf.append("\"");
				buf.append(propEntry.getKey());
				buf.append("\"");
				
				buf.append(", ");
				
				boolean firstPropValue = true;
				for( String propValue : propEntry.getValue()) {
					if(!firstPropValue)
						buf.append(", ");
					
					buf.append("\"");
					buf.append(propValue);
					buf.append("\"");
					
					firstPropValue = false;
				}
				buf.append("}");
			}

			buf.append("}");
			first = false;
		}

		buf.append("}");
		return buf.toString().replace("\\", "\\\\");
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
		private Set<String> libDirs = new HashSet<>();
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
			libDirs.add( getLibDirectory(libPath) );
		}
		
		private String getLibDirectory(String libPath) {
			File effectiveLibPath = new File("." + libPath);
			File libDir = effectiveLibPath.getParentFile();
			return libDir.getPath();
		}
		
		private Map<String,List<String>> getProperties() {
			return properties;
		}
		
		private List<String> getLibs() {
			return libs;
		}
		
//		private Set<String> getLibDirs() {
//			return libDirs;
//		}
	}
	
	public List<LibInfo> getLibs() {
		return libs;
	}
	
}
