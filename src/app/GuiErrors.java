package app;

import javax.swing.JOptionPane;

/**
 * A helper class containing methods for generating error message dialogs.
 */
final class GuiErrors {
	private GuiErrors() {
		super();
	}

	/**
	 * Base handler for creating an error dialog window.
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 * @param message Message to be displayed.
	 */
	private static void showErrorMessage(GuiApp app, Object message) {
		JOptionPane.showMessageDialog(app, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorOpenFile(GuiApp app) {
		showErrorMessage(app, "Please open a file first!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorCannotRead(GuiApp app) {
		showErrorMessage(app, "Cannot read the content of the file!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorCannotWrite(GuiApp app) {
		showErrorMessage(app, "Cannot write content to the file!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorFileNotFound(GuiApp app) {
		showErrorMessage(app, "File cannot be found!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorCannotWriteDown(GuiApp app) {
		showErrorMessage(app, "Cannot write down the new content because the file cannot be opened!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorSecurityViolation(GuiApp app) {
		showErrorMessage(app, "Cannot operate due to security violation!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorNoLines(GuiApp app) {
		showErrorMessage(app, "There are no lines!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorLastLineNotEmpty(GuiApp app) {
		showErrorMessage(app, "Last line must be empty to be removed!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorNotEnoughLines(GuiApp app) {
		showErrorMessage(app, "Not enough lines available to perform a swap!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorNotEnoughWords(GuiApp app) {
		showErrorMessage(app, "Not enough words available to perform a swap!");
	}

	/**
	 * @param app A reference to the application's instance to be used as dialog window's parent.
	 */
	public static void errorNotPlainTextFile(GuiApp app) {
		showErrorMessage(app, "Selected file is not plain text file (*.txt)!");
	}
}
