package al.franzis.cheshire.test

import al.franzis.cheshire.Module

@Module
class ManifestExample {
	static val bundleName = "busundleABC"
	static val exportedPackages = #[ "com", "a"]
	
}