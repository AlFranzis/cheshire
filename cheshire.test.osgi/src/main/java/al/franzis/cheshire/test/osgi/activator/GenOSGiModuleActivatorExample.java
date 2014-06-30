package al.franzis.cheshire.test.activator;

import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import al.franzis.cheshire.IModuleContext;
import al.franzis.cheshire.ModuleContextMethod;
import al.franzis.cheshire.ModuleStartMethod;
import al.franzis.cheshire.ModuleStopMethod;
import al.franzis.cheshire.osgi.OSGiModuleContext;

public class GenOSGiModuleActivatorExample implements BundleActivator {
	private IModuleContext moduleContext;
	
	public void start( BundleContext context ) {
		setModuleContext( new OSGiModuleContext( context ) );
		start();
	}
	
	public void stop( BundleContext context ) {
		stop();
	}
	
	@ModuleStartMethod
	public void start() {
		URL resourceUrl = moduleContext.getModule().getResource( "someResource" );
		System.out.println( resourceUrl );
	}
	
	@ModuleStopMethod
	public void stop() {
	}
	
	@ModuleContextMethod
	public void setModuleContext( IModuleContext moduleContext ) {
		this.moduleContext = moduleContext;
	}
	
}
