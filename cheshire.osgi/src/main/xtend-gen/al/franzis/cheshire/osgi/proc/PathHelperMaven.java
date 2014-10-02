package al.franzis.cheshire.osgi.proc;

import al.franzis.cheshire.osgi.proc.PathHelper;
import com.google.common.base.Objects;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtend.lib.macro.file.FileSystemSupport;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class PathHelperMaven extends PathHelper {
  public void setContents(final FileSystemSupport fss, final Path dirPath, final Path filePath, final CharSequence cs) {
    try {
      boolean _notEquals = (!Objects.equal(dirPath, null));
      if (_notEquals) {
        this.mkdirs(dirPath);
      }
      final File f = this.getFile(filePath);
      final FileWriter fw = new FileWriter(f);
      fw.append(cs);
      fw.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String getProjectDir() {
    return System.getProperty("xtend.annotations.project.dir");
  }
  
  private boolean mkdirs(final Path path) {
    boolean _xblockexpression = false;
    {
      final File f = this.getFile(path);
      final boolean success = f.mkdirs();
      _xblockexpression = success;
    }
    return _xblockexpression;
  }
  
  private File getFile(final Path path) {
    File _xblockexpression = null;
    {
      List<String> _segments = path.getSegments();
      final ArrayList<String> pathSegments = new ArrayList<String>(_segments);
      final String projectDir = this.getProjectDir();
      pathSegments.remove(0);
      FileSystem _default = FileSystems.getDefault();
      final java.nio.file.Path p = _default.getPath(projectDir, ((String[])Conversions.unwrapArray(pathSegments, String.class)));
      _xblockexpression = p.toFile();
    }
    return _xblockexpression;
  }
}
