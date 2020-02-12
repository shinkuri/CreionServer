package utility;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	private static String getTimeStamp() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		return new StringBuilder()
				.append("[").append(dtf.format(now)).append("]").toString();
	}
	
	public static class INFO {
		
		public static void log(String msg) {
			System.out.println(
					new StringBuilder(getTimeStamp()).append(" INFO: ").append(msg)
					.toString());
		}
		
		public static PrintStream getPrintStream() {
			return System.out;
		}
	}
	
	public static class ERROR {
		
		public static void log(String msg) {
			System.err.println(
					new StringBuilder(getTimeStamp()).append(" ERROR: ").append(msg)
					.toString());
		}
		
		public static PrintStream getPrintStream() {
			return System.err;
		}
	}
}
