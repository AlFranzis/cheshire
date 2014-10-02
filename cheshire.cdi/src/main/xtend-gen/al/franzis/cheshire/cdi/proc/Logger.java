package al.franzis.cheshire.cdi.proc;

import com.google.common.io.CharStreams;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Logger {
  private static boolean ENABLED = false;
  
  private Path loggerFile;
  
  @Extension
  private MutableFileSystemSupport cxt;
  
  public static Logger getLogger(final ClassDeclaration clazz, final MutableFileSystemSupport cxt) {
    Logger _xblockexpression = null;
    {
      CompilationUnit _compilationUnit = clazz.getCompilationUnit();
      final Path filePath = _compilationUnit.getFilePath();
      Path _parent = filePath.getParent();
      final Path loggerFile = _parent.append("processor.log");
      _xblockexpression = new Logger(loggerFile, cxt);
    }
    return _xblockexpression;
  }
  
  private Logger(final Path loggerFile, final MutableFileSystemSupport cxt) {
    this.loggerFile = loggerFile;
    this.cxt = cxt;
  }
  
  public void info(final String msg) {
    if ((!Logger.ENABLED)) {
      return;
    }
    String _readExistingFile = this.readExistingFile();
    final StringBuffer buf = new StringBuffer(_readExistingFile);
    buf.append("\n");
    buf.append(msg);
    String _string = buf.toString();
    this.cxt.setContents(this.loggerFile, _string);
  }
  
  public void error(final String msg, final Throwable t) {
    if ((!Logger.ENABLED)) {
      return;
    }
    String _readExistingFile = this.readExistingFile();
    final StringBuffer buf = new StringBuffer(_readExistingFile);
    buf.append("\n");
    buf.append(msg);
    buf.append("\n");
    String _stackTrace = this.stackTrace(t);
    buf.append(_stackTrace);
    String _string = buf.toString();
    this.cxt.setContents(this.loggerFile, _string);
  }
  
  private String readExistingFile() {
    String _xblockexpression = null;
    {
      String s = "";
      boolean _exists = this.cxt.exists(this.loggerFile);
      if (_exists) {
        InputStream _contentsAsStream = this.cxt.getContentsAsStream(this.loggerFile);
        String _read = this.read(_contentsAsStream);
        s = _read;
      }
      _xblockexpression = s;
    }
    return _xblockexpression;
  }
  
  private String read(final InputStream in) {
    try {
      String _xblockexpression = null;
      {
        InputStreamReader _inputStreamReader = new InputStreamReader(in);
        final List<String> lines = CharStreams.readLines(_inputStreamReader);
        _xblockexpression = IterableExtensions.join(lines, "\n");
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String stackTrace(final Throwable t) {
    try {
      String _xblockexpression = null;
      {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream stream = new PrintStream(out);
        t.printStackTrace(stream);
        out.close();
        byte[] _byteArray = out.toByteArray();
        _xblockexpression = new String(_byteArray);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
