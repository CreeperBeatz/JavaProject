package app.core.utils;

final class Word {
	private int offset;
	private int length;

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

	int getStartOffset() {
		return offset;
	}

	int getEndOffset() {
		return offset + length;
	}

	int getLength() {
		return length;
	}

	void offsetWord(int delta) throws IllegalArgumentException {
		int newOffset = this.offset + delta;

		if (newOffset < 0 || newOffset + length < newOffset) {
			throw new IllegalArgumentException();
		}

		this.offset += delta;
	}

	void swapLengths(Word otherWord) throws IllegalArgumentException {
		if (offset + otherWord.length < offset || otherWord.offset + length < otherWord.offset) {
			throw new IllegalArgumentException();
		}

		int temp = length;
		length = otherWord.length;
		otherWord.length = temp;
	}

	String getWord(String line) throws IndexOutOfBoundsException {
		return line.substring(getStartOffset(), getEndOffset());
	}

	@Override
	public String toString() {
		return String.format("{ offset: %d; length: %d }", offset, length);
	}
}
