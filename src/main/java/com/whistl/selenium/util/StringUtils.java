package com.whistl.selenium.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * /item/view/10024209 Finds the item id from the url.
	 * 
	 * @param url
	 *            where item id has to be found
	 * @return item id.
	 */
	public static Integer getItemIdFromUrl(String url) {
		Matcher matcher = Pattern.compile("(\\d){2,11}").matcher(url);
		matcher.find();
		return Integer.valueOf(matcher.group());
	}

	/**
	 * Find the no of the item in the url.
	 * 
	 * @param url
	 *            where no of item is present.
	 * @return total of item present in page.
	 */
	public static Integer getItemCount(String url) {
		Matcher matcher = Pattern.compile("(\\d){1,2}").matcher(url);
		matcher.find();
		return Integer.valueOf(matcher.group());
	}

	/**
	 * Finds the Nth occurrences of the digits in the string.
	 * 
	 * @param text
	 *            where nth occurrences of digits has to be found.
	 * @param n
	 *            occurrences of digits.
	 * @return nth digits.
	 */
	public static long getNthDigitsFromString(String text, int n) {
		long results = 0;
		if (!checkIfOnlyNumbers(text)) {
			Matcher matcher = Pattern.compile("(\\d+)").matcher(text);
			for (int i = 1; i <= n; i++) {
				matcher.find();
				results = Integer.valueOf(matcher.group());
			}
		} else {
			results = Long.valueOf(text);
		}
		return results;
	}

	/**
	 * Finds the Nth occurrences of the digits in the string.
	 * 
	 * @param text
	 *            where nth occurrences of digits has to be found.
	 * @param n
	 *            occurrences of digits.
	 * @return nth digits.
	 */
	public static double getNthDoubleDigitsFromString(String text, int n) {
		double results = 0;
		// String regex ="\\d+\\.\\d+";
		String numberRegex = "[\\d]+(?:[\\.]{1}[\\d]+)?";
		Matcher matcher = Pattern.compile(numberRegex).matcher(text);
		for (int i = 1; i <= n; i++) {
			matcher.find();
			results = Double.valueOf(matcher.group());
		}
		return results;
	}

	public static Boolean hasCommonWords(String s1, String s2) {
		List<String> s1List = Arrays.asList(s1.split("\\s+"));
		List<String> s2List = Arrays.asList(s2.split("\\s+"));

		for (String s : s1List)
			if (s2.contains(s))
				return true;
		for (String s : s2List)
			if (s1.contains(s))
				return true;
		return false;
	}

	public static boolean checkIfOnlyNumbers(String text) {
		String regex = "\\d+";
		if (text.matches(regex) && text.length() > 2) {
			return true;
		}
		return false;
	}

	public static String getFirstStringFromWords(String text) {
		String returnString = "";
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isDigit(text.charAt(i))) {
				returnString = returnString + text.charAt(i);
			} else if (text.charAt(i) == '0') {
				returnString = returnString + text.charAt(i);
			} else {
				break;
			}

		}
		return returnString;
	}
}
