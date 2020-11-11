package app;

import javax.swing.JOptionPane;

import app.core.AppCore;

/**
 * A helper class containing methods for creating dialog windows requesting user input and perform word swapping.
 */
final class GuiWordInput {
	private GuiWordInput() {
		super();
	}

	/**
	 * Base handler for creating an input dialog window.
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 * @param message Message to be displayed.
	 * @param options Array of the available options for the dialog window.
	 * @return An {@link java.lang.Integer} object holding the value of the selected option.
	 */
	private static Integer showDialog(GuiApp app, String message, Integer[] options) {
		return (Integer) JOptionPane.showInputDialog(
			app,
			message,
			"Word Number Input",
			JOptionPane.PLAIN_MESSAGE,
			null,
			options,
			options[0]
		);
	}

	/**
	 * This method generates an array of all the available words an the given line that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the word numbers.
	 */
	private static Integer[] inputWordOptions(AppCore appCore, int line) {
		Integer[] options = new Integer[appCore.wordCountOnLine(line)];

		for (int z = 0; z < options.length; ++z) {
			options[z] = Integer.valueOf(z + 1);
		}

		return options;
	}

	/**
	 * This method generates an array of all the available words (excluding the one given as an argument)
	 * on the given line that can be selected within the dialog window.
	 * @param appCore An assumed to be non-null reference to the application's current session.
	 * @return An array containing of {@link java.lang.Integer} objects holding the word numbers.
	 */
	private static Integer[] inputWordOptionsExcluding(AppCore appCore, int line, int word) {
		Integer[] options = new Integer[appCore.wordCountOnLine(line) - 1];

		int wordNumber = 1;

		for (int z = 0; z < options.length; ++z) {
			if (wordNumber == word + 1) {
				wordNumber += 1;
			}

			options[z] = Integer.valueOf(wordNumber++);
		}

		return options;
	}

	private static Integer inputWordWithOptions(GuiApp app, String message, Integer[] options) {
		Integer input = showDialog(app, message, options);
		
		if (input == null) {
			return null;
		}

		return Integer.valueOf(input.intValue() - 1);
	}

	private static Integer inputWord(GuiApp app, AppCore appCore, String message, int line) {
		return inputWordWithOptions(app, message, inputWordOptions(appCore, line));
	}

	private static Integer inputWordExcluding(GuiApp app, AppCore appCore, String message, int line, int word) {
		return inputWordWithOptions(app, message, inputWordOptionsExcluding(appCore, line, word));
	}

	static Integer inputFirstWord(GuiApp app, AppCore appCore, int line) {
		return inputWord(app, appCore, "Please enter first word's number:", line);
	}

	static Integer inputSecondWord(GuiApp app, AppCore appCore, int line) {
		return inputWord(app, appCore, "Please enter second word's number:", line);
	}

	static Integer inputSecondWordExcluding(GuiApp app, AppCore appCore, int line, int word) {
		return inputWordExcluding(app, appCore, "Please enter second word's number:", line, word);
	}
}
