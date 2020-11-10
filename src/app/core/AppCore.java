package app.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import app.core.utils.Line;
import app.core.utils.LineReader;

public final class AppCore implements AutoCloseable {
	private static final String closedMessage;

	private final String filePath;
	private ArrayList<Line> lines;

	static {
		closedMessage = "Content already written down and closed!";
	}

	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws InvalidContentException
	 */
	public AppCore(String filePath) throws FileNotFoundException, SecurityException, IOException {
		super();

		this.filePath = filePath;
		lines = new LineReader(filePath).getLines();
	}

	public String getFilePath() {
		return filePath;
	}

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

	public boolean isLineInBounds(int lineIndex) throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (0 <= lineIndex && lineIndex < lines.size()) {
			return true;
		}

		return false;
	}

	public boolean isWordInBounds(int lineIndex, int wordIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).isWordInBounds(wordIndex);
	}

	public boolean lineHasWords(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).hasWords();
	}

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

	public boolean isEmpty() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.isEmpty();
	}

	public int lineCount() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.size();
	}

	public boolean hasWordsOnLine(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).hasWords();
	}

	public int wordCountOnLine(int lineIndex) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).wordCount();
	}

	public int wordCountOnLine(int lineIndex, int maxCount) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(lineIndex).wordCount(maxCount);
	}

	public void addEmptyLine() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		lines.add(Line.empty());
	}

	public boolean isLastLineEmpty() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (lines.size() == 0) {
			return false;
		}

		return lines.get(lines.size() - 1).isEmpty();
	}

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
