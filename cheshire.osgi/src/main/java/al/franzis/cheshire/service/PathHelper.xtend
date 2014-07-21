package al.franzis.cheshire.service

import org.eclipse.xtend.lib.macro.file.FileSystemSupport
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport
import org.eclipse.xtend.lib.macro.file.Path

class PathHelper {
	
	static def getInstance() {
		if(eclipseEnvironment)
			return new PathHelper
		else
			return new PathHelperMaven
	}
	
	public def setContents( FileSystemSupport fss, Path dirPath, Path filePath, CharSequence cs) {
		val mfss = fss as MutableFileSystemSupport
		if ( dirPath != null )
			mfss.mkdir(dirPath)
		mfss.setContents(filePath, cs)
	}
	
	public static def boolean isEclipseEnvironment() {
		System.getProperty("xtend.annotations.project.dir") == null
	}
}