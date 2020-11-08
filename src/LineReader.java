import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LineReader {
	private final FileReader input;
	private boolean finished;
	
	/**
	 * Creates new instance of the class while opening the
	 * specified file for reading.
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 */
	public LineReader(String filename) throws FileNotFoundException, SecurityException {
		super();

		input = new FileReader(filename);
		finished = false;
	}

	/**
	 * Read one line of the file.
	 * @return A line from the file or a null reference when
	 * the file is marked as finished.
	 */
	public Line getLine() throws IOException {
		if (isFinished()) {
			return null;
		} else {
			StringBuilder line = new StringBuilder();

			int c;
			
			while (true) {
				c = input.read();

				if (c == -1) {
					setFinished();
					
					break;
				} else if ((char) c == '\n') {
					break;
				} else {
					if ((char) c == '\r') {
						c = input.read();

						if (c == -1) {
							setFinished();
							
							break;
						} else if ((char) c == '\n') {
							break;
						}

						// '\r' (CR) is a special character so it shouldn't be added to the string.
						continue;
					}

					line.append((char) c);
				}
			};

			return new Line(line.toString());
		}
	}

	/**
	 * Reads the file until End-Of-File (EOF) is encountered.
	 * @return List of the lines from the file.
	 */
	public List<Line> getLines() throws IOException {
		List<Line> list = new ArrayList<Line>();

		while (!isFinished()) {
			list.add(getLine());
		}

		return list;
	}

	public boolean isFinished() {
		return finished;
	}

	// No fields are required because once set as finished, this flag shouldn't be changed.
	private void setFinished() throws IOException {
		finished = true;

		input.close();
	}
}
