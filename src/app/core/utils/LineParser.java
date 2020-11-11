package app.core.utils;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class that parses words from a given string and returns an array of their representations.
 * 
 * This class is not extendable nor instanciatable as it is only a single function utility class.
 */
final class LineParser {
	private static final Pattern wordPattern;
	
	static {
		wordPattern = Pattern.compile("\\d*(?!\\d)\\w+", Pattern.UNICODE_CASE);
	}

	/**
	 * Because the constructor is private, this will ensure that the class doesn't get instanciated.
	 * This in combination with {@code final} on the class makes the class both {@code final} and {@code abstract} at the same time.
	 */
	private LineParser() {
		super();
	}

	/**
	 * Generates an array of all words, if any.
	 * @param line The line which will be used to generate the words array.
	 * @return A non-null reference to the array.
	 */
	static Word[] getWords(Line line) {
		Matcher matcher = wordPattern.matcher(line.getLine());

		ArrayList<Word> list = new ArrayList<Word>();

		while (matcher.find()) {
			final int start = matcher.start();

			list.add(new Word(start, matcher.end() - start));
		}

		return list.toArray(Word[]::new);
	}

	/**
	 * Generates an array of at most {@code maxCount} number of words, if any.
	 * @param line The line which will be used to generate the words array.
	 * @param maxCount The maximum amount of words to be parsed.
	 * @return A non-null reference to the array.
	 * @throws IllegalArgumentException Thrown when {@code maxCount} is a negative number.
	 */
	static Word[] getWords(Line line, int maxCount) throws IllegalArgumentException {
		Matcher matcher = wordPattern.matcher(line.getLine());

		ArrayList<Word> list = new ArrayList<Word>();

		matcher.results().limit(maxCount).forEach(
			(MatchResult result) -> {
				final int start = matcher.start();

				list.add(new Word(start, matcher.end() - start));
			}
		);

		return list.toArray(Word[]::new);
	}
}
