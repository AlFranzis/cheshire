package al.franzis.cheshire.api.nativecode;

import java.io.File;

public interface ILibPathProvider {
	
	public File getEffectivePath(String libPath);
}
