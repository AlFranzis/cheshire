package al.franzis.cheshire;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PathTest {
	
	@Test
	public void testPathSegments() throws URISyntaxException {
		URI uri = new URI("file://D:/git-repos");
		Path p = Paths.get(uri);
		for ( Path s : p )
		{
			System.out.println(s);
		}
		File f = p.toFile();
		System.out.println(Files.exists(p));
		System.out.println(p);
	}
}
