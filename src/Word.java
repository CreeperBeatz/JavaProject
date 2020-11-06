public final class Word {
	private int offset;
	private int length;

	public Word(int offset, int length) throws IndexOutOfBoundsException {
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

	public int getStartOffset() {
		return offset;
	}

	public int getEndOffset() {
		return offset + length;
	}

	public int getLength() {
		return length;
	}

	public void offsetWord(int offset) throws IndexOutOfBoundsException {
		if (this.offset + offset < 0) {
			throw new IndexOutOfBoundsException(this.offset + offset);
		}
		this.offset += offset;
	}

	public void swapLengths(Word other) throws NullPointerException {
		int temp = length;
		length = other.length;
		other.length = temp;
	}

	public String getWord(String line) throws NullPointerException, IndexOutOfBoundsException {
		return line.substring(getStartOffset(), getEndOffset());
	}

	@Override
	public String toString() {
		return String.format("{ offset: %d; length: %d }", offset, length);
	}
}
