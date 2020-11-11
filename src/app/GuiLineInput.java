package app;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import app.core.AppCore;

/**
 * A helper class containing methods for creating dialog windows requesting user input and perform line swapping.
 */
final class GuiLineInput {
	private GuiLineInput() {
		super();
	}

	/**
	 * Base handler for creating an input dialog window.
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 * @param message Message to be displayed.
	 * @param options Array of the available options for the dialog window.
	 * @return An {@link java.lang.Integer} object holding the value of the selected option.
	 */
	private static Integer showDialog(GuiApp app, Object message, Integer[] options) {
		return (Integer) JOptionPane.showInputDialog(
			app,
			message,
			"Line Number Input",
			JOptionPane.PLAIN_MESSAGE,
			null,
			options,
			options[0]
		);
	}

	/**
	 * This method generates an array of all the available lines that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the line numbers.
	 */
	private static Integer[] inputLineOptions(AppCore appCore) {
		Integer[] options = new Integer[appCore.lineCount()];

		for (int z = 0; z < options.length; ++z) {
			options[z] = Integer.valueOf(z + 1);
		}

		return options;
	}

	/**
	 * This method generates an array of all the available lines (excluding the one given as an argument)
	 * that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the line numbers.
	 */
	private static Integer[] inputLineOptionsExcluding(AppCore appCore, int line) {
		Integer[] options = new Integer[appCore.lineCount() - 1];

		int lineNumber = 1;

		for (int z = 0; z < options.length; ++z) {
			if (lineNumber == line + 1) {
				lineNumber += 1;
			}

			options[z] = Integer.valueOf(lineNumber++);
		}

		return options;
	}

	/**
	 * This method generates an array of all the available lines which contain words that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the word numbers.
	 */
	private static Integer[] inputLineOptionsForWords(AppCore appCore) {
		ArrayList<Integer> options = new ArrayList<Integer>();

		int lineCount = appCore.lineCount();

		for (int z = 0; z < lineCount; ++z) {
			if (appCore.hasWordsOnLine(z)) {
				options.add(Integer.valueOf(z + 1));
			}
		}

		return options.toArray(Integer[]::new);
	}

	/**
	 * This method generates an array of all the available lines which contain words (excluding the one given as an argument)
	 * that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the word numbers.
	 */
	private static Integer[] inputLineOptionsForWordsExcluding(AppCore appCore, int line) {
		ArrayList<Integer> options = new ArrayList<Integer>();

		for (int z = 0; z < appCore.lineCount(); ++z) {
			int tempResult = line == z ? 1 : 0;

			if (appCore.wordCountOnLine(z, 1 + tempResult) > tempResult) {
				options.add(Integer.valueOf(z + 1));
			}
		}

		return options.toArray(Integer[]::new);
	}

	private static Integer inputLineWithOptions(GuiApp app, String message, Integer[] options) {
		Integer input = showDialog(app, message, options);
		
		if (input == null) {
			return null;
		}

		return Integer.valueOf(input.intValue() - 1);
	}

	private static Integer inputLine(GuiApp app, AppCore appCore, String message) {
		return inputLineWithOptions(app, message, inputLineOptions(appCore));
	}

	private static Integer inputLineExcluding(GuiApp app, AppCore appCore, String message, int line) {
		return inputLineWithOptions(app, message, inputLineOptionsExcluding(appCore, line));
	}

	private static Integer inputLineForWords(GuiApp app, AppCore appCore, String message) {
		return inputLineWithOptions(app, message, inputLineOptionsForWords(appCore));
	}

	private static Integer inputLineForWordsExcluding(GuiApp app, AppCore appCore, String message, int line) {
		return inputLineWithOptions(app, message, inputLineOptionsForWordsExcluding(appCore, line));
	}

	static Integer inputFirstLine(GuiApp app, AppCore appCore) {
		return inputLine(app, appCore, "Please enter first line's number:");
	}

	static Integer inputFirstLineForWords(GuiApp app, AppCore appCore) {
		return inputLineForWords(app, appCore, "Please enter first line's number:");
	}

	static Integer inputSecondLineExcluding(GuiApp app, AppCore appCore, int line) {
		return inputLineExcluding(app, appCore, "Please enter second line's number:", line);
	}

	static Integer inputSecondLineForWordsExcluding(GuiApp app, AppCore appCore, int line) {
		return inputLineForWordsExcluding(app, appCore, "Please enter second line's number:", line);
	}
}
