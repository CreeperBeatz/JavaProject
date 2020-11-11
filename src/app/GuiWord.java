package app;

/**
 * Represents a formatted word to be used in an input dialog window.
 */
final class GuiWord {
	private final int wordIndex;
	private final String formattedWord;

	/**
	 * Creates new instance of the class, saving the word index and saving a new formatted
	 * string that will be used in {@code toString}.
	 * @param wordIndex The index of the word which will be saved.
	 * @param word The word which will be used to generate the new formatted string.
	 */
	GuiWord(int wordIndex, String word) {
		super();

		this.wordIndex = wordIndex;
		this.formattedWord = String.format("%d. %s", wordIndex, word);
	}

	/**
	 * @return The saved word index passed as an argument to the constructor.
	 */
	int getWordIndex() {
		return wordIndex;
	}

	@Override
	public String toString() {
		return formattedWord;
	}
}
