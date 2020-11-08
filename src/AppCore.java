import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class AppCore implements AutoCloseable {
	private final String filename;
	private Line[] lines;
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
		lines = new FileReader(filename).getLines();
	}

	public String getFilename() {
		return filename;
	}

	public void swapWords(int firstLine, int firstWord, int secondLine, int secondWord) throws IndexOutOfBoundsException, IllegalStateException {
		try {
			lines[firstLine].swapWords(lines[secondLine], firstWord, secondWord);
		} catch (NullPointerException exception) {
			throw new IllegalStateException(closedMessage);
		}
	}

	public void swapLines(int firstLine, int secondLine) throws IndexOutOfBoundsException, IllegalStateException {
		if (firstLine == secondLine) {
			return;
		}

		try {
			Line temp = lines[firstLine];
			lines[firstLine] = lines[secondLine];
			lines[secondLine] = temp;
		} catch (NullPointerException exception) {
			throw new IllegalStateException(closedMessage);
		}
	}

	public boolean isLineInBounds(int line) {
		if (0 <= line && line < lines.length) {
			return true;
		}
		return false;
	}

	public boolean isWordInBounds(int line, int word) throws IndexOutOfBoundsException {
		return lines[line].isWordInBounds(word);
	}

	public String getContentString() throws IllegalStateException {
		if (lines == null) {
			throw new IllegalStateException();
		}

		StringBuilder content = new StringBuilder();

		for (Line line : lines) {
			content.append(line.getLine());
			content.append('\n');
		}

		if (!content.isEmpty()) {
			content.deleteCharAt(content.length() - 1);
		}

		return content.toString();
	}

	@Override
	public void close() throws FileNotFoundException, SecurityException, IOException {
		if (lines == null) {
			return;
		}
		
		OutputStream file = null;
		
		try {
			file = new FileOutputStream(filename);

			if (lines.length != 0) {
				file.write(lines[0].getLine().getBytes());
			}

			for (int z = 1; z < lines.length; ++z) {
				file.write('\n');
				file.write(lines[z].getLine().getBytes());
			}
		} finally {
			this.lines = null;

			if (file != null) {
				file.close();
			}
		}
	}
}
