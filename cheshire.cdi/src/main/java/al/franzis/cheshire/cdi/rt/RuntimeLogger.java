package al.franzis.cheshire.cdi.rt;

public class RuntimeLogger {
	private static RuntimeLogger INSTANCE = new RuntimeLogger();
	
	private static boolean ENABLED = true;
	
	public static RuntimeLogger getInstance() {
		return INSTANCE;
	}
	
	private RuntimeLogger() {}
	
	
	public void info(String msg) {
		if(!ENABLED) return;
		
		System.out.println("[INFO] " + msg);
	}
	
	public void error(String msg) {
		if(!ENABLED) return;
		
		System.err.println("[ERROR] " + msg);
	}
}
