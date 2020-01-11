package utility;

public class Logger {
	
	public static class INFO {
		
		public static void log(String msg) {
			System.out.println("INFO: " + msg);
		}
	}
	
	public static class ERROR {
		
		public static void log(String msg) {
			System.err.println("ERROR: " + msg);
		}
	}
}
