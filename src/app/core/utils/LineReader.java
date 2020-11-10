package app.core.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class for reading files line by line.
 * This is more reliable for line-by-line reading than {@link java.util.Scanner} because it doesn't ignore the last line.
 */
public final class LineReader {
	private final FileReader input;
	private boolean finished;
	
	/**
	 * Creates new instance of the class while opening the specified file for reading.
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
	 * @return A line from the file or a null reference when the file is marked as finished.
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

			if (line.isEmpty()) {
				return Line.empty();
			}

			// Will not throw "IllegalArgumentException" as this class parses until the end of the line.
			return new Line(line.toString());
		}
	}

	/**
	 * Reads the file until End-Of-File (EOF) is encountered.
	 * @return List of the lines from the file.
	 */
	public ArrayList<Line> getLines() throws IOException {
		ArrayList<Line> list = new ArrayList<Line>();

		while (!isFinished()) {
			list.add(getLine());
		}

		return list;
	}

	/**
	 * Checks the state of the reader.
	 * @return True when reader has not encountered End-Of-File (EOF). Otherwise, false.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Marks the reader's state as "finished" (see {@code isFinished}) and closes the file.
	 * @throws IOException Thrown when the file couldn't be closed properly.
	 */
	private void setFinished() throws IOException {
		finished = true;

		input.close();
	}
}
