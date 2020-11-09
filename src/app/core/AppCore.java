package app.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import app.core.utils.Line;
import app.core.utils.LineReader;

public final class AppCore implements AutoCloseable {
	private final String filename;
	private List<Line> lines;
	private static final String closedMessage;

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
	public AppCore(String filename) throws FileNotFoundException, SecurityException, IOException {
		super();

		this.filename = filename;
		lines = new LineReader(filename).getLines();
	}

	public String getFilename() {
		return filename;
	}

	public void swapWords(int firstLine, int firstWord, int secondLine, int secondWord) throws IndexOutOfBoundsException, IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		try {
			lines.get(firstLine).swapWords(lines.get(secondLine), firstWord, secondWord);
		} catch (NullPointerException exception) {
			throw new IllegalStateException(closedMessage);
		}
	}

	public void swapLines(int firstLine, int secondLine) throws IndexOutOfBoundsException, IllegalStateException {
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

	public boolean isLineInBounds(int line) throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		if (0 <= line && line < lines.size()) {
			return true;
		}

		return false;
	}

	public boolean isWordInBounds(int line, int word) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		return lines.get(line).isWordInBounds(word);
	}

	public boolean lineHasWords(int line) throws IllegalStateException, IndexOutOfBoundsException {
		return isWordInBounds(line, 0);
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

	public int wordCountOnLine(int line) throws IllegalStateException, IndexOutOfBoundsException {
		if (lines == null) {
			throw new IllegalStateException();
		}
		
		return lines.get(line).wordCount();
	}

	public void addEmptyLine() throws UnsupportedOperationException, ClassCastException, IllegalStateException, NullPointerException, IllegalArgumentException {
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

	public void removeLastLine() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
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
			file = new FileOutputStream(filename);

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
