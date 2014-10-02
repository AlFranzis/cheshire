package al.franzis.cheshire.cdi.rt;

import java.io.File;

import al.franzis.cheshire.api.nativecode.ILibPathProvider;

public class DefaultLibPathProvider implements ILibPathProvider {

	@Override
	public File getEffectivePath(String libPath) {
		return new File("." + libPath);
	}

}
