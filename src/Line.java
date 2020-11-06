import java.util.regex.Pattern;

/**
 * Represents line that is verified to have valid content.
 */
public final class Line {
	private String line;
	private Word[] words;
	private static final Pattern wordsPattern;

	static {
		wordsPattern = Pattern.compile("^[\\w\\s]*$");
	}
	
	/**
	 * Contructs new instance of the class while verifying the
	 * content of the line.
	 * @param line - String
	 * @throws InvalidContentException This exception is thrown
	 * when the line contains content that is not alphanumeric.
	 */
	public Line(String line) throws InvalidContentException {
		super();

		// Verify contents of the file.
		if (!wordsPattern.matcher(line).matches()) {
			throw new InvalidContentException();
		}

		this.line = line;
		words = new Word[0];
	}

	public String getLine() {
		return line;
	}

	public void swapWords(Line other, int own_word_index, int other_word_index) throws IndexOutOfBoundsException {
		// Compare addresses to check whether it's the same object.
		if (this == other) {
			final int min, max;

			if (own_word_index < other_word_index) {
				min = own_word_index;
				max = other_word_index;
			} else {
				min = other_word_index;
				max = own_word_index;
			}

			if (words.length < max + 1) {
				this.words = LineParser.getWords(this, max + 1);
			}

			Word left_word = words[min], right_word = words[max];

			// Compare addresses to check whether it's the same word.
			if (left_word == right_word) {
				// No more actions required.
				return;
			}

			final int delta = right_word.getLength() - left_word.getLength();

			StringBuilder builder = new StringBuilder(line);
			
			builder.insert(left_word.getEndOffset(), right_word.getWord(line));

			right_word.offsetWord(right_word.getLength());

			builder.replace(right_word.getStartOffset(), right_word.getEndOffset(), left_word.getWord(line));

			builder.replace(left_word.getStartOffset(), left_word.getEndOffset(), "");

			right_word.offsetWord(-left_word.getLength());

			int temp = left_word.getLength();

			left_word.setLength(right_word.getLength());

			right_word.setLength(temp);

			for (int z = min + 1; z < max; ++z) {
				words[z].offsetWord(delta);
			}

			line = builder.toString();
		}
	}

	@Override
	public String toString() {
		return line;
	}
}
