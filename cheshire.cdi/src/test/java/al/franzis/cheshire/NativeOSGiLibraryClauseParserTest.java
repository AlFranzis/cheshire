package al.franzis.cheshire;

import junit.framework.Assert;

import org.junit.Test;

import al.franzis.cheshire.cdi.rt.NativeOSGiLibraryClauseParser;

public class NativeOSGiLibraryClauseParserTest {
	
	@Test
	public void testNativeLibHandling() throws Exception {
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeOSGiLibraryClauseParser libHandler = new NativeOSGiLibraryClauseParser(libClause);
		String nativeLibs= libHandler.toStringLiteral();
		System.out.println(nativeLibs);
		Assert.assertNotNull(nativeLibs);
	}
	
	@Test
	public void testNativeLibHandling2() throws Exception {
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeOSGiLibraryClauseParser libHandler = new NativeOSGiLibraryClauseParser( libClause);
		String nativeLibs = libHandler.toStringLiteral();
		System.out.println(nativeLibs);
		Assert.assertNotNull(nativeLibs);
	}
	
	@Test
	public void testNativeLibHandling3() throws Exception {
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		              +    	"\"/lib/win64/D3DCompiler_43.dll;/lib/win64/D3DX9_43.dll;/lib/win64/VIC.dll;/lib/win64/GPU.dll;processor=x86-64;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		                + 	"\"/lib/linux64/libVIC.so;osname=Linux;processor=amd64\"]";
		
		NativeOSGiLibraryClauseParser libHandler = new NativeOSGiLibraryClauseParser( libClause);
		String nativeLibs = libHandler.toStringLiteral();
		System.out.println(nativeLibs);
		Assert.assertNotNull(nativeLibs);
	}

}
