package app.core.utils;

final class Word {
	private int offset;
	private int length;

	Word(int offset, int length) throws IndexOutOfBoundsException {
		super();

		if (offset < 0) {
			throw new IndexOutOfBoundsException(offset);
		}
		this.offset = offset;

		if (length < 0) {
			throw new IndexOutOfBoundsException(length);
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

	void offsetWord(int offset) throws IndexOutOfBoundsException {
		if (this.offset + offset < 0) {
			throw new IndexOutOfBoundsException(this.offset + offset);
		}
		this.offset += offset;
	}

	void swapLengths(Word other) throws NullPointerException {
		int temp = length;
		length = other.length;
		other.length = temp;
	}

	String getWord(String line) throws NullPointerException, IndexOutOfBoundsException {
		return line.substring(getStartOffset(), getEndOffset());
	}

	@Override
	public String toString() {
		return String.format("{ offset: %d; length: %d }", offset, length);
	}
}
