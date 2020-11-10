package app.core.utils;

/**
 * This class represents a word by storing it's offset into the string and it's length.
 */
final class Word {
	private int offset;
	private int length;

	/**
	 * Constructs a new instance that contains the offset and the length of the word.
	 * @param offset The offset of the word into the string.
	 * @param length Length of the word.
	 * @throws IndexOutOfBoundsException Thrown when the offset is a negative number.
	 * @throws IllegalArgumentException Thrown when the length is a negative number or the sum of the offset and the length overflows.
	 */
	Word(int offset, int length) throws IndexOutOfBoundsException, IllegalArgumentException {
		super();

		if (offset < 0) {
			throw new IndexOutOfBoundsException(offset);
		}
		this.offset = offset;

		if (length < 0 || offset + length < offset) {
			throw new IllegalAccessError("Length of the word cannot be negative number!");
		}
		this.length = length;
	}

	/**
	 * Returns the offset of the word into the string.
	 * @return A positive integer representing the offset into the string.
	 */
	int getStartOffset() {
		return offset;
	}

	/**
	 * Returns the offset just after the word's end.
	 * @return A positive integer represending the offset just after the word's end into the string.
	 */
	int getEndOffset() {
		return offset + length;
	}

	/**
	 * Returns the length of the word.
	 * @return A positive integer representing the length of the word.
	 */
	int getLength() {
		return length;
	}

	/**
	 * Offsets the word by a given delta.
	 * @param delta The delta to apply to the word's offset.
	 * @throws IllegalArgumentException Thrown when the operation would cause an arithmetical overflow.
	 */
	void offsetWord(int delta) throws IllegalArgumentException {
		int newOffset = this.offset + delta;

		if (newOffset < 0 || newOffset + length < newOffset) {
			throw new IllegalArgumentException();
		}

		this.offset += delta;
	}

	/**
	 * Swaps the length of the current word with the one of a given other word.
	 * @param otherWord The word to have lengths swapped with.
	 * @throws IllegalArgumentException Thrown when the operation would cause an arithmetical overflow.
	 */
	void swapLengths(Word otherWord) throws IllegalArgumentException {
		if (offset + otherWord.length < offset || otherWord.offset + length < otherWord.offset) {
			throw new IllegalArgumentException();
		}

		int temp = length;
		length = otherWord.length;
		otherWord.length = temp;
	}

	/**
	 * Extracts the word as a substring of the given line.
	 * @param line The line from which the string is extracted.
	 * @return A non-null reference to a string containing the word.
	 * @throws IndexOutOfBoundsException Thrown when the word exceeds the given line.
	 */
	String getWord(String line) throws IndexOutOfBoundsException {
		return line.substring(getStartOffset(), getEndOffset());
	}

	@Override
	public String toString() {
		return String.format("{ offset: %d; length: %d }", offset, length);
	}
}
