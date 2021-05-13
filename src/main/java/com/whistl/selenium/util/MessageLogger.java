package com.whistl.selenium.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.testng.Reporter;

/**
 * Use this enum to log messages. An enum is used for implementation in order to
 * make it a threadsafe and serializable singleton.
 */
public enum MessageLogger {
	INSTANCE;

	// /** Time to sleep after a log entry. In milliseconds. */
	// private static final Long TIME_TO_SLEEP = 1000L;

	/** format to print out the time. */
	private static String timeFormat = "%02d:%02d:%02d:%03d";
	/** start time of the test. */
	private static long startTimeMillis;

	/**
	 * Reset the logger to start from scratch (e.g. message counting etc.)
	 */
	public static void reset() {
		startTimeMillis = System.currentTimeMillis();
	}

	private static void logMessage(final String message) {
		// log message
		String messagePrefix = "STEP_DOC [" + getSystemTime() + "] ["
				+ getRelativeTime() + "]:";
		Reporter.log(messagePrefix + message);
		System.out.println(messagePrefix + message);
	}

	private static String getRelativeTime() {
		long timeDifference = System.currentTimeMillis() - startTimeMillis;
		long hours, minutes, seconds, millis, rest;
		hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
		rest = timeDifference - TimeUnit.HOURS.toMillis(hours);
		minutes = TimeUnit.MILLISECONDS.toMinutes(rest);
		rest -= TimeUnit.MINUTES.toMillis(minutes);
		seconds = TimeUnit.MILLISECONDS.toSeconds(rest);
		millis = rest - TimeUnit.SECONDS.toMillis(seconds);
		return String.format(timeFormat, hours, minutes, seconds, millis);
	}

	private static String getSystemTime() {
		Calendar now = Calendar.getInstance();
		Integer hours = Integer.valueOf(now.get(Calendar.HOUR_OF_DAY));
		Integer minutes = Integer.valueOf(now.get(Calendar.MINUTE));
		Integer seconds = Integer.valueOf(now.get(Calendar.SECOND));
		Integer millis = Integer.valueOf(now.get(Calendar.MILLISECOND));
		return String.format(timeFormat, hours, minutes, seconds, millis);
	}

	/**
	 * 
	 * @param message
	 *            used for log or as file suffix
	 * 
	 */
	public static void logStepResult(final String message) {
		String cleanMessage = message;
		// remove tailing '.' if present
		if (cleanMessage.charAt(cleanMessage.length() - 1) == '.') {
			cleanMessage = cleanMessage.substring(0, cleanMessage.length() - 2);
		}
		logMessage(message);
	}
}
