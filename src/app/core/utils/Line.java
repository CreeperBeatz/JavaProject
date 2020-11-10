package app.core.utils;

/**
 * Represents a line.
 */
public final class Line {
	private String line;

	private Word[] words = {};
	private boolean allWordsParsed = false;

	Line() {
		super();

		line = "";
	}
	
	Line(String line) throws IllegalArgumentException {
		super();

		if (line.contains("\n")) {
			throw new IllegalArgumentException("String contains multiple lines!");
		}

		this.line = line;
	}

	public static Line empty() {
		return new Line();
	}

	public boolean isEmpty() {
		return line.isEmpty();
	}

	public String getLine() {
		return line;
	}

	public boolean hasWords() {
		if (!allWordsParsed) {
			if (words.length == 0) {
				words = LineParser.getWords(this, 1);

				if (words.length == 0) {
					allWordsParsed = true;

					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}

		return words.length == 0;
	}

	public int wordCount() {
		if (!allWordsParsed) {
			words = LineParser.getWords(this);

			allWordsParsed = true;
		}

		return words.length;
	}

	public int wordCount(int max) {
		if (max < 0) {
			return 0;
		}

		if (!allWordsParsed) {
			if (words.length < max) {
				words = LineParser.getWords(this, max);

				if (words.length < max) {
					allWordsParsed = true;
				}
			}
		}

		return words.length;
	}

	public boolean isWordInBounds(int word) {
		if (word < 0 || word + 1 < 0) {
			return false;
		}

		if (!allWordsParsed) {
			if (words.length < word + 1) {
				words = LineParser.getWords(this, word + 1);

				if (words.length < word + 1) {
					allWordsParsed = true;

					return false;
				}
			}
		}

		return word < words.length;
	}
	private void swapOwnWords(int leftWordIndex, int rightWordIndex) throws IndexOutOfBoundsException {
		// Check whether the word map is already generated until the right word's index.
		if (!allWordsParsed) {
			if (words.length < rightWordIndex + 1) {
				if (rightWordIndex + 1 < 0) {
					throw new IndexOutOfBoundsException();
				}

				this.words = LineParser.getWords(this, rightWordIndex + 1);

				if (words.length <= rightWordIndex) {
					allWordsParsed = true;
				}
			}
		}

		// Will throw an "IndexOutOfBoundsException" in case the word count is smaller.
		// It is annotated that it can throw it.
		Word leftWord = words[leftWordIndex], rightWord = words[rightWordIndex];

		{
			// Create new string builder with capacity equal to the string's length.
			StringBuilder builder = new StringBuilder(line.length());

			// Build string from parts.
			builder.append(line.substring(0, leftWord.getStartOffset()));
			builder.append(rightWord.getWord(line));
			builder.append(line.substring(leftWord.getEndOffset(), rightWord.getStartOffset()));
			builder.append(leftWord.getWord(line));
			builder.append(line.substring(rightWord.getEndOffset()));

			line = builder.toString();

			// Free the string builder.
		}

		// Calculate the delta of the word lengths.
		final int delta = rightWord.getLength() - leftWord.getLength();

		// Adjust the offset of the right word.
		rightWord.offsetWord(delta);

		// Swap words' lengths.
		leftWord.swapLengths(rightWord);

		// Adjust the offsets of the words between the two, if any.
		// The other words' offsets will stay correct.
		for (int z = leftWordIndex + 1; z < rightWordIndex; ++z) {
			words[z].offsetWord(delta);
		}
	}

	private void swapOwnAndOtherWords(Line otherLine, int ownWordIndex, int otherWordIndex) throws IndexOutOfBoundsException {
		// Check whether the word map is already generated until the word's index.
		if (!allWordsParsed) {
			if (words.length < ownWordIndex + 1) {
				if (ownWordIndex + 1 < 0) {
					throw new IndexOutOfBoundsException();
				}

				words = LineParser.getWords(this, ownWordIndex + 1);

				if (words.length <= ownWordIndex) {
					allWordsParsed = true;
				}
			}
		}

		// Check whether the word map is already generated until the word's index.
		if (!otherLine.allWordsParsed) {
			if (otherLine.words.length < otherWordIndex + 1) {
				if (ownWordIndex + 1 < 0) {
					throw new IndexOutOfBoundsException();
				}

				otherLine.words = LineParser.getWords(otherLine, otherWordIndex + 1);

				if (otherLine.words.length <= otherWordIndex) {
					allWordsParsed = true;
				}
			}
		}

		// Will throw an "IndexOutOfBoundsException" in case the word count is smaller.
		// It is annotated that it can throw it.
		Word ownWord = words[ownWordIndex], otherWord = otherLine.words[otherWordIndex];

		// Calculate the delta of the word lengths.
		final int delta = otherWord.getLength() - ownWord.getLength();

		String ownWordString = ownWord.getWord(line), otherWordString = otherWord.getWord(otherLine.line);

		// When delta is not equal to zero (0), the two words cannot be equal.
		if (delta == 0) {
			if (ownWordString.equals(otherWordString)) {
				return;
			}
		}

		{
			// Create new string builder with capacity equal to the string's length plus the delta.
			StringBuilder ownBuilder = new StringBuilder(line.length() + delta);

			ownBuilder.append(line.substring(0, ownWord.getStartOffset()));

			ownBuilder.append(otherWordString);
			// Not needed anymore.
			otherWordString = null;

			ownBuilder.append(line.substring(ownWord.getEndOffset()));

			line = ownBuilder.toString();

			// Free the string builder.
		}

		// Adjust the offsets of the words after the word, if any.
		// The words' offsets before it will stay correct.
		for (int z = ownWordIndex + 1; z < words.length; ++z) {
			words[z].offsetWord(delta);
		}

		{
			// Create new string builder with capacity equal to the string's length plus the delta.
			StringBuilder otherBuilder = new StringBuilder(otherLine.line.length() - delta);

			otherBuilder.append(otherLine.line.substring(0, otherWord.getStartOffset()));

			otherBuilder.append(ownWordString);
			// Not needed anymore.
			ownWordString = null;

			otherBuilder.append(otherLine.line.substring(otherWord.getEndOffset()));

			otherLine.line = otherBuilder.toString();

			// Free the string builder.
		}

		// Adjust the offsets of the words after the word, if any.
		// The words' offsets before it will stay correct.
		for (int z = otherWordIndex + 1; z < otherLine.words.length; ++z) {
			words[z].offsetWord(-delta);
		}

		ownWord.swapLengths(otherWord);
	}

	public void swapWords(Line otherLine, int ownWordIndex, int otherWordIndex) throws IndexOutOfBoundsException {
		// Compare addresses to check whether it's the same object.
		if (this == otherLine) {
			if (ownWordIndex == otherWordIndex) {
				// No more actions required.
				return;
			}

			// Find which index is for the left word and which for the right one.
			if (ownWordIndex < otherWordIndex) {
				swapOwnWords(ownWordIndex, otherWordIndex);
			} else {
				swapOwnWords(otherWordIndex, ownWordIndex);
			}
		} else {
			swapOwnAndOtherWords(otherLine, ownWordIndex, otherWordIndex);
		}
	}

	@Override
	public String toString() {
		return line;
	}
}
