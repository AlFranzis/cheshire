package al.franzis.cheshire;

import java.util.List;
import java.util.Map;

import al.franzis.cheshire.NativeLibHandler.LibInfo;

import org.junit.Test;

public class NativeLibTest {
	
	@Test
	public void testNativeLibHandling() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libClause);
		List<LibInfo> libs = libHandler.getLibs();
		System.out.println(libs);
	}
	
	@Test
	public void testNativeLibHandling2() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libClause);
		String nativeLibs = libHandler.getModuleNativeLibs();
		System.out.println(nativeLibs);
	}
	
	@Test
	public void testNativeLibHandling3() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		              +    	"\"/lib/win64/D3DCompiler_43.dll;/lib/win64/D3DX9_43.dll;/lib/win64/VIC.dll;/lib/win64/GPU.dll;processor=x86-64;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		                + 	"\"/lib/linux64/libVIC.so;osname=Linux;processor=amd64\"]";
		
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libClause);
		String nativeLibs = libHandler.getModuleNativeLibs();
		System.out.println(nativeLibs);
	}
	
	private IEnvironmentMatcher getDummyMatcher() {
		IEnvironmentMatcher envMatcher = new IEnvironmentMatcher() {
			
			@Override
			public boolean matchesEnvironment(Map<String, List<String>> targetProperties) {
				return true;
			}
		};
		
		return envMatcher;
	}
}
