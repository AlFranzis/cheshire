package al.franzis.cheshire.osgi.proc

import java.io.File
import java.io.FileWriter
import java.nio.file.FileSystems
import java.util.ArrayList
import org.eclipse.xtend.lib.macro.file.FileSystemSupport
import org.eclipse.xtend.lib.macro.file.Path

class PathHelperMaven extends PathHelper {
	
	public override def setContents(FileSystemSupport fss, Path dirPath, Path filePath, CharSequence cs) {
		if (dirPath != null)
			mkdirs(dirPath)
		
		val File f = getFile(filePath)
		
		val FileWriter fw = new FileWriter(f)
		fw.append(cs)
		fw.close()
	}
	
	private def String getProjectDir() {
		System.getProperty("xtend.annotations.project.dir")
	}
	
	private def boolean mkdirs(Path path) {
		val File f = getFile(path)
		val success = f.mkdirs()
		success
	}
	
	private def File getFile(Path path) {
		val pathSegments = new ArrayList( path.segments )
		
		val projectDir = projectDir
		
		pathSegments.remove(0)
		
		val java.nio.file.Path p = FileSystems.getDefault().getPath( projectDir, pathSegments )
		p.toFile
	}
}