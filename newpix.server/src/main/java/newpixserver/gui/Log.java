package newpixserver.gui;

import newpixserver.service.listener.LogEvent;

public class Log {
	private static LogEvent logEvent;

	public static void addListener(LogEvent logEvent) {
		Log.logEvent = logEvent;
	}
	
	public static void print(String message) {
		System.out.println(message);
		logEvent.logUpdate(message);
	}
	
	public static void error(String message) {
		System.err.println(message);
		logEvent.logUpdate(message);
	}
}
