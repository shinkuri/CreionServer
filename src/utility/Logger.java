package utility;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class Logger {
	
	// oof
	private static String getTimeStamp() {
		final Timestamp stamp = new Timestamp(new Date().getTime());
		final LocalDateTime ldt = stamp.toLocalDateTime();
		return new StringBuilder().append("[")
				.append(ldt.getHour()).append(":")
				.append(ldt.getMinute()).append(":")
				.append(ldt.getSecond()).append("]")
				.toString();
	}
	
	public static class INFO {
		
		public static void log(String msg) {
			
			System.out.println(getTimeStamp() + " INFO: " + msg);
		}
		
		public static PrintStream getPrintStream() {
			return System.out;
		}
	}
	
	public static class ERROR {
		
		public static void log(String msg) {
			System.err.println(getTimeStamp() + " ERROR: " + msg);
		}
		
		public static PrintStream getPrintStream() {
			return System.err;
		}
	}
}
