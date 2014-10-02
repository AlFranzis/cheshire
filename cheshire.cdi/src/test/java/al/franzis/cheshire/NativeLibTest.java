package al.franzis.cheshire;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import al.franzis.cheshire.api.nativecode.IEnvironmentMatcher;
import al.franzis.cheshire.api.nativecode.ILibPathProvider;
import al.franzis.cheshire.cdi.rt.DefaultEnvironmentMatcher;
import al.franzis.cheshire.cdi.rt.DefaultLibPathProvider;
import al.franzis.cheshire.cdi.rt.NativeLibHandler;
import al.franzis.cheshire.cdi.rt.NativeLibHandler.LibInfo;

public class NativeLibTest {
	
	@Test
	public void testNativeLibHandling() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		ILibPathProvider libPathProvider = getDummyLibPathProvider();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libPathProvider, libClause);
		List<LibInfo> libs = libHandler.getLibs();
		Assert.assertEquals(1, libs.size());
	}
	
	@Test
	public void testNativeLibHandling2() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		ILibPathProvider libPathProvider = getDummyLibPathProvider();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osname=WindowsXP;osname=Windows 2003\"]";
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libPathProvider, libClause);
		String nativeLibs = libHandler.getModuleNativeLibs();
		Assert.assertEquals(".\\\\lib\\\\win32", nativeLibs);
	}
	
	@Test
	public void testNativeLibHandling3() throws Exception {
		IEnvironmentMatcher envMatcher = getDummyMatcher();
		ILibPathProvider libPathProvider = getDummyLibPathProvider();
		
		String libClause = "#[\"/lib/win32/D3DCompiler_43.dll;/lib/win32/D3DX9_43.dll;/lib/win32/VIC.dll;/lib/win32/GPU.dll;processor=x86;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		              +    	"\"/lib/win64/D3DCompiler_43.dll;/lib/win64/D3DX9_43.dll;/lib/win64/VIC.dll;/lib/win64/GPU.dll;processor=x86-64;osversion=5.1;osversion=5.2;osversion=6.0;osversion=6.1;osversion=6.2;osname=WindowsXP;osname=Windows 2003;osname=Windows Vista;osname=Windows 7;osname=Windows NT (unknown);osname=\"windows server 2008 r2\";osname=windows server 2008;osname=Windows 8;osname=Windows 8.1\","
		                + 	"\"/lib/linux64/libVIC.so;osname=Linux;processor=amd64\"]";
		
		NativeLibHandler libHandler = new NativeLibHandler(envMatcher, libPathProvider, libClause);
		String nativeLibs = libHandler.getModuleNativeLibs();
		Assert.assertEquals(".\\\\lib\\\\win64;.\\\\lib\\\\win32;.\\\\lib\\\\linux64", nativeLibs);
	}
	
	private IEnvironmentMatcher getDummyMatcher() {
		return new DefaultEnvironmentMatcher();
	}
	
	private ILibPathProvider getDummyLibPathProvider() {
		return new DefaultLibPathProvider();
	}
}
