package app.core.utils;

/**
 * Represents a line.
 */
public final class Line {
	private String line;
	private Word[] words = {};
	private boolean allWordsParsed;

	Line() {
		super();

		line = "";
	}
	
	Line(String line) {
		super();

		this.line = line;
		allWordsParsed = false;
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

	public int wordCount() {
		if (!allWordsParsed) {
			words = LineParser.getWords(this);
			
			allWordsParsed = true;
		}

		return words.length;
	}

	public boolean isWordInBounds(int word) {
		if (0 <= word) {
			if (!allWordsParsed) {
				if (words.length <= word) {
					words = LineParser.getWords(this, word + 1);

					if (words.length <= word) {
						allWordsParsed = true;

						return false;
					}
				}
			}

			if (word < words.length) {
				return true;
			}
		}

		return false;
	}

	public void swapWords(Line other, int ownWordIndex, int otherWordIndex) throws IndexOutOfBoundsException {
		// Compare addresses to check whether it's the same object.
		if (this == other) {
			if (ownWordIndex == otherWordIndex) {
				// No more actions required.
				return;
			}

			final int leftIndex, rightIndex;

			// Find which index is for the left word and which for the right one.
			if (ownWordIndex < otherWordIndex) {
				leftIndex = ownWordIndex;
				rightIndex = otherWordIndex;
			} else {
				leftIndex = otherWordIndex;
				rightIndex = ownWordIndex;
			}

			// Check whether the word map is already generated until the right word's index.
			if (!allWordsParsed) {
				if (words.length < rightIndex + 1) {
					this.words = LineParser.getWords(this, rightIndex + 1);

					if (words.length <= rightIndex) {
						allWordsParsed = true;
					}
				}
			}

			// Will throw an "IndexOutOfBoundsException" in case the word count is smaller.
			// It is annotated that it can throw it.
			Word leftWord = words[leftIndex], rightWord = words[rightIndex];

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
			for (int z = leftIndex + 1; z < rightIndex; ++z) {
				words[z].offsetWord(delta);
			}
		} else {
			// Check whether the word map is already generated until the word's index.
			if (!allWordsParsed) {
				if (words.length < ownWordIndex + 1) {
					words = LineParser.getWords(this, ownWordIndex + 1);

					if (words.length <= ownWordIndex) {
						allWordsParsed = true;
					}
				}
			}

			// Check whether the word map is already generated until the word's index.
			if (!other.allWordsParsed) {
				if (other.words.length < otherWordIndex + 1) {
					other.words = LineParser.getWords(other, otherWordIndex + 1);

					if (other.words.length <= otherWordIndex) {
						allWordsParsed = true;
					}
				}
			}

			// Will throw an "IndexOutOfBoundsException" in case the word count is smaller.
			// It is annotated that it can throw it.
			Word ownWord = words[ownWordIndex], otherWord = other.words[otherWordIndex];

			// Calculate the delta of the word lengths.
			final int delta = otherWord.getLength() - ownWord.getLength();

			String ownWordString = ownWord.getWord(line), otherWordString = otherWord.getWord(other.line);

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
				StringBuilder otherBuilder = new StringBuilder(other.line.length() - delta);

				otherBuilder.append(other.line.substring(0, otherWord.getStartOffset()));

				otherBuilder.append(ownWordString);
				// Not needed anymore.
				ownWordString = null;

				otherBuilder.append(other.line.substring(otherWord.getEndOffset()));

				other.line = otherBuilder.toString();

				// Free the string builder.
			}

			// Adjust the offsets of the words after the word, if any.
			// The words' offsets before it will stay correct.
			for (int z = otherWordIndex + 1; z < other.words.length; ++z) {
				words[z].offsetWord(-delta);
			}

			ownWord.swapLengths(otherWord);
		}
	}

	@Override
	public String toString() {
		return line;
	}
}
