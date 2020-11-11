package app.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import app.core.utils.Line;
import app.core.utils.LineReader;

/**
 * Represents application's core logic.
 * This class is responsible for all operations supported by the application.
 */
public final class AppCore implements AutoCloseable {
	private static final String closedMessage;

	private final String filePath;
	private ArrayList<Line> lines;

	// Using static initializer block as it is cleaner and organized.
	static {
		closedMessage = "Content already written down and closed!";
	}

	/**
	 * Creates a new instance that loads up the content of the file.
	 * @param filePath Absolute or relative path to the file on which the application will operate on.
	 * @throws FileNotFoundException Thrown when the file couldn't be found and/or opened.
	 * @throws SecurityException Thrown when there is a security violation.
	 * @throws IOException Thrown when there was an error while loading the file's content.
	 */
	public AppCore(String filePath) throws FileNotFoundException, SecurityException, IOException {
		super();

		this.filePath = filePath;
		lines = new LineReader(filePath).getLines();
	}

	/**
	 * Get the path to the file used to create the current instance.
	 * @return A non-null string representing the absolute or relative path of the file.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Performs a swap of two words on the same line or two different lines.
	 * @param firstLineIndex The index of the first line.
	 * @param firstWordIndex The index of the word on the first line.
	 * @param secondLineIndex The index of the second line.
	 * @param secondWordIndex The index of the word on the second line.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when any of the indexes are out of bounds.
	 */
	public void swapWords(int firstLineIndex, int firstWordIndex, int secondLineIndex, int secondWordIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		try {
			lines.get(firstLineIndex).swapWords(lines.get(secondLineIndex), firstWordIndex, secondWordIndex);
		} catch (NullPointerException exception) {
			throw new IllegalStateException(closedMessage);
		}
	}

	/**
	 * Performs a swap of two lines.
	 * @param firstLineIndex The index of the first line.
	 * @param secondLineIndex The index of the second line.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the indexes of the lines are out of bounds.
	 */
	public void swapLines(int firstLine, int secondLine) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (firstLine == secondLine) {
			return;
		}

		try {
			Line temp = lines.set(firstLine, lines.get(secondLine));
			lines.set(secondLine, temp);
		} catch (NullPointerException exception) {
			throw new IllegalStateException(closedMessage);
		}
	}

	/**
	 * Extracts and returns a given word from a given line.
	 * @param line The index of the line on which the operation will be performed.
	 * @param word The index of the word which will be extracted and returned.
	 * @return A non-null reference to a string containing the word at the given index.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the indexes are out of bounds.
	 */
	public String getWord(int line, int word) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(line).getWord(word);
	}

	/**
	 * Checks whether the given line index is in bounds.
	 * @param lineIndex The line index to be checked.
	 * @return True when "lineIndex" is in bounds. Otherwise, false.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 */
	public boolean isLineInBounds(int lineIndex) throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (0 <= lineIndex && lineIndex < lines.size()) {
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the given word index is in bounds.
	 * @param lineIndex Index of the line on which the word index will be checked.
	 * @param wordIndex Index of the word that to be checked.
	 * @return True when the word is in bounds. Otherwise, false.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the line index is out of bounds.
	 */
	public boolean isWordInBounds(int lineIndex, int wordIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).isWordInBounds(wordIndex);
	}

	/**
	 * Checks whether there are any words on the given line.
	 * @param lineIndex Index of the line to be checked.
	 * @return True when there is at least one word on the line.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the line index is out of bounds.
	 */
	public boolean lineHasWords(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).hasWords();
	}

	/**
	 * Creates a new formatted string containing a snapshot of the current content of the session.
	 * @return A non-null string containing a snapshot of the current content.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 */
	public String getContentString() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		StringBuilder content = new StringBuilder();

		if (lines.size() != 0) {
			content.append("1 || ");
			content.append(lines.get(0).getLine());
		}

		for (int z = 1; z < lines.size(); ++z) {
			content.append('\n');
			content.append(z + 1);
			content.append(" || ");
			content.append(lines.get(z).getLine());
		}

		return content.toString();
	}

	/**
	 * Checks whether there are any lines. This is equivalent to {@code lineCount() == 0}.
	 * @return True when there is at least one line. Otherwise, false.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 */
	public boolean isEmpty() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.isEmpty();
	}

	/**
	 * Gets the line count of the current content.
	 * @return A positive integer representing the line count.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 */
	public int lineCount() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.size();
	}

	/**
	 * Checks whether there any words on the given line.
	 * @param lineIndex Index of the line to be checked.
	 * @return True when there is at least one word. Otherwise, false.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the line index is out of bounds.
	 */
	public boolean hasWordsOnLine(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).hasWords();
	}

	/**
	 * Gets the word count of the given line.
	 * @param lineIndex Index of the line to be checked.
	 * @return A positive integer representing the word count.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the line index is out of bounds.
	 */
	public int wordCountOnLine(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).wordCount();
	}

	/**
	 * Gets the word count of the given line while parsing at most {@code maxCount} words, if not already done.
	 * @param lineIndex Index of the line to be checked.
	 * @param maxCount The maximum count of words to be parsed, if not already done.
	 * @return A positive integer representing the word count.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws IndexOutOfBoundsException Thrown when the line index is out of bounds.
	 */
	public int wordCountOnLine(int lineIndex, int maxCount) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).wordCount(maxCount);
	}

	/**
	 * Adds an empty line to the end of the current content.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 */
	public void addEmptyLine() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		lines.add(Line.empty());
	}

	/**
	 * Checks whether the last line of the current content is empty.
	 * @return True then the last line is empty. Otherwise, false.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @apiNote It treats the case where there are no lines as if there was and it was not empty, therefore returning false.
	 */
	public boolean isLastLineEmpty() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (lines.size() == 0) {
			return false;
		}

		return lines.get(lines.size() - 1).isEmpty();
	}

	/**
	 * Removes the last line of the current content.
	 * @throws IllegalStateException Thrown when the session is closed (through {@code close}) and the new content is written down.
	 * @throws NoSuchElementException Thrown when the content is already empty.
	 * @apiNote It will remove the last line no matter whether it is empty or not so it is recommended to first check via {@code isLastLineEmpty}.
	 */
	public void removeLastLine() throws IllegalStateException, NoSuchElementException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (lines.isEmpty()) {
			throw new NoSuchElementException("Cannot remove any lines because there aren't any.");
		}

		lines.remove(lines.size() - 1);
	}

	@Override
	public void close() throws FileNotFoundException, SecurityException, IOException {
		if (lines == null) {
			return;
		}

		OutputStream file = null;

		try {
			file = new FileOutputStream(filePath);

			if (lines.size() != 0) {
				file.write(lines.get(0).getLine().getBytes());
			}

			for (int z = 1; z < lines.size(); ++z) {
				file.write('\n');
				file.write(lines.get(z).getLine().getBytes());
			}
		} finally {
			this.lines = null;

			if (file != null) {
				file.close();
			}
		}
	}
}
