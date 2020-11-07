import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public final class FileReader {
	private final Scanner input;
	private boolean finished;
	
	/**
	 * Creates new instance of the class while opening the
	 * specified file for reading.
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 */
	public FileReader(String filename) throws FileNotFoundException, SecurityException {
		super();

		input = new Scanner(new FileInputStream(filename));

		if (!input.hasNextLine()) {
			setFinished();
		}
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
			String line = input.nextLine();

			if (!input.hasNextLine()) {
				setFinished();
			}

			return new Line(line);
		}
	}

	/**
	 * Reads the file until End-Of-File (EOF) is encountered.
	 * @return List of the lines from the file.
	 */
	public Line[] getLines() throws IOException {
		ArrayList<Line> list = new ArrayList<Line>();

		while (!isFinished()) {
			list.add(getLine());
		}

		return list.toArray(Line[]::new);
	}

	public boolean isFinished() {
		return finished;
	}

	// No fields are required because once set as finished, this flag shouldn't be changed.
	private void setFinished() {
		finished = true;

		input.close();
	}
}
