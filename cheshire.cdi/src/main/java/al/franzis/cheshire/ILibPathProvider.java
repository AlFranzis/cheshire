package al.franzis.cheshire;

import java.io.File;

public interface ILibPathProvider {
	
	public File getEffectivePath(String libPath);
}
