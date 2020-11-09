package app;

import javax.swing.JOptionPane;

final class GuiErrors {
	private GuiErrors() {
		super();
	}

	private static void showErrorMessage(GuiApp app, String message) {
		JOptionPane.showMessageDialog(app, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void errorOpenFile(GuiApp app) {
		showErrorMessage(app, "Please open a file first!");
	}

	public static void errorCannotRead(GuiApp app) {
		showErrorMessage(app, "Cannot read content of the file!");
	}

	public static void errorFileNotFound(GuiApp app) {
		showErrorMessage(app, "File cannot be found!");
	}

	public static void errorCannotWriteDown(GuiApp app) {
		showErrorMessage(app, "Cannot write down the new content because the file cannot be opened!");
	}

	public static void errorSecurityViolation(GuiApp app) {
		showErrorMessage(app, "Cannot operate due to security violation!");
	}

	public static void errorNoLines(GuiApp app) {
		showErrorMessage(app, "There are no lines!");
	}

	public static void errorLastLineNotEmpty(GuiApp app) {
		showErrorMessage(app, "Last line must be empty to be removed!");
	}

	public static void errorNotEnoughLines(GuiApp app) {
		showErrorMessage(app, "Not enough lines available to perform a swap!");
	}

	public static void errorNotEnoughWords(GuiApp app) {
		showErrorMessage(app, "Not enough words available to perform a swap!");
	}
}
