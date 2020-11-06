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
	public AppCore(String filename) throws FileNotFoundException, SecurityException {
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

	@Override
	public void close() throws FileNotFoundException, SecurityException, IOException {
		if (lines == null) {
			return;
		}

		Line[] lines = this.lines;
		this.lines = null;

		OutputStream file = new FileOutputStream(filename);

		for (Line line : lines) {
			file.write(line.getLine().getBytes());
			file.write('\n');
		}

		file.close();
	}
}
