package al.franzis.cheshire.osgi.proc;

import al.franzis.cheshire.osgi.proc.PathHelperMaven;
import com.google.common.base.Objects;
import org.eclipse.xtend.lib.macro.file.FileSystemSupport;
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport;
import org.eclipse.xtend.lib.macro.file.Path;

@SuppressWarnings("all")
public class PathHelper {
  public static PathHelper getInstance() {
    boolean _isEclipseEnvironment = PathHelper.isEclipseEnvironment();
    if (_isEclipseEnvironment) {
      return new PathHelper();
    } else {
      return new PathHelperMaven();
    }
  }
  
  public void setContents(final FileSystemSupport fss, final Path dirPath, final Path filePath, final CharSequence cs) {
    final MutableFileSystemSupport mfss = ((MutableFileSystemSupport) fss);
    boolean _notEquals = (!Objects.equal(dirPath, null));
    if (_notEquals) {
      mfss.mkdir(dirPath);
    }
    mfss.setContents(filePath, cs);
  }
  
  public static boolean isEclipseEnvironment() {
    String _property = System.getProperty("xtend.annotations.project.dir");
    return Objects.equal(_property, null);
  }
}
