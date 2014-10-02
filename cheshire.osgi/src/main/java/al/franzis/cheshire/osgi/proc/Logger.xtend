package al.franzis.cheshire.osgi.proc

import java.io.InputStream
import java.io.InputStreamReader
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport
import org.eclipse.xtend.lib.macro.file.Path

import static extension com.google.common.io.CharStreams.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class Logger {
	static boolean ENABLED = false
	Path loggerFile
	extension MutableFileSystemSupport cxt
	
	static def Logger getLogger( ClassDeclaration clazz, MutableFileSystemSupport cxt ) {
		val filePath = clazz.compilationUnit.filePath
		val loggerFile = filePath.parent.append("processor.log")
		
		new Logger(loggerFile, cxt)
	}
	
	private new(Path loggerFile, MutableFileSystemSupport cxt) {
		this.loggerFile = loggerFile
		this.cxt = cxt
	}
	
	def info(String msg) {
		if (!ENABLED) return;
			
		val StringBuffer buf = new StringBuffer(readExistingFile())
		buf.append("\n")
		buf.append(msg)
		loggerFile.contents = buf.toString
	}
	
	def error(String msg, Throwable t) {
		if (!ENABLED) return;
		
		val StringBuffer buf = new StringBuffer(readExistingFile())
		buf.append("\n")
		buf.append(msg)
		buf.append("\n")
		buf.append(stackTrace(t))
		loggerFile.contents = buf.toString
	}
	
	private def String readExistingFile() {
		var String s = ""
		if ( loggerFile.exists)
			s = read(loggerFile.contentsAsStream)
		s
	}
	
	private def String read( InputStream in ) {
		val lines = new InputStreamReader(in).readLines
		lines.join("\n")
	}
	
	private def String stackTrace(Throwable t) {
		val ByteArrayOutputStream out = new ByteArrayOutputStream()
		val PrintStream stream = new PrintStream(out)
		t.printStackTrace(stream)
		out.close
		new String(out.toByteArray)
			
		
	}
	
}