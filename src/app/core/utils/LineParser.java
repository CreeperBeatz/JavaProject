package app.core.utils;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a utility class that parses words and returns the word('s) offset(s) and length(s) from a given string.
 * 
 * This class is not extendable nor instanciatable as it is only a single function utility class.
 */
final class LineParser {
	private static final Pattern wordPattern;
	
	static {
		wordPattern = Pattern.compile("(?!\\d)\\w+");
	}

	/**
	 * Because the constructor is private, this will ensure that the class doesn't get instanciated.
	 * This in combination with "final" on the class makes the class both "final" and "abstract" at the same time.
	 */
	private LineParser() {
		super();
	}

	static Word[] getWords(Line line) {
		Matcher matcher = wordPattern.matcher(line.getLine());

		ArrayList<Word> list = new ArrayList<Word>();

		while (matcher.find()) {
			final int start = matcher.start();

			list.add(new Word(start, matcher.end() - start));
		}

		return list.toArray(Word[]::new);
	}

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
