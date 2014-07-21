package al.franzis.cheshire;

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
      Logger _logger = new Logger(loggerFile, cxt);
      _xblockexpression = (_logger);
    }
    return _xblockexpression;
  }
  
  private Logger(final Path loggerFile, final MutableFileSystemSupport cxt) {
    this.loggerFile = loggerFile;
    this.cxt = cxt;
  }
  
  public void info(final String msg) {
    String _readExistingFile = this.readExistingFile();
    StringBuffer _stringBuffer = new StringBuffer(_readExistingFile);
    final StringBuffer buf = _stringBuffer;
    buf.append("\n");
    buf.append(msg);
    String _string = buf.toString();
    this.cxt.setContents(
      this.loggerFile, _string);
  }
  
  public void error(final String msg, final Throwable t) {
    String _readExistingFile = this.readExistingFile();
    StringBuffer _stringBuffer = new StringBuffer(_readExistingFile);
    final StringBuffer buf = _stringBuffer;
    buf.append("\n");
    buf.append(msg);
    buf.append("\n");
    String _stackTrace = this.stackTrace(t);
    buf.append(_stackTrace);
    String _string = buf.toString();
    this.cxt.setContents(
      this.loggerFile, _string);
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
      _xblockexpression = (s);
    }
    return _xblockexpression;
  }
  
  private String read(final InputStream in) {
    try {
      String _xblockexpression = null;
      {
        InputStreamReader _inputStreamReader = new InputStreamReader(in);
        final List<String> lines = CharStreams.readLines(_inputStreamReader);
        String _join = IterableExtensions.join(lines, "\n");
        _xblockexpression = (_join);
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
        ByteArrayOutputStream _byteArrayOutputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream out = _byteArrayOutputStream;
        PrintStream _printStream = new PrintStream(out);
        final PrintStream stream = _printStream;
        t.printStackTrace(stream);
        out.close();
        byte[] _byteArray = out.toByteArray();
        String _string = new String(_byteArray);
        _xblockexpression = (_string);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}