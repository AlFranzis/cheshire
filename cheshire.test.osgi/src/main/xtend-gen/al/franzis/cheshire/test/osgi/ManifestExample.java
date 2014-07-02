package al.franzis.cheshire.test.osgi;

import al.franzis.cheshire.Module;
import java.util.Collections;
import java.util.List;

@Module
@SuppressWarnings("all")
public class ManifestExample {
  private final static String bundleName = "busundleABC";
  
  private final static List<String> exportedPackages = Collections.<String>unmodifiableList(com.google.common.collect.Lists.<String>newArrayList("com", "a"));
}
