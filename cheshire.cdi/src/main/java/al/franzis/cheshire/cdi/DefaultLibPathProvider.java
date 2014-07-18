package al.franzis.cheshire.cdi;

import java.io.File;

import al.franzis.cheshire.ILibPathProvider;

public class DefaultLibPathProvider implements ILibPathProvider {

	@Override
	public File getEffectivePath(String libPath) {
		return new File("." + libPath);
	}

}
