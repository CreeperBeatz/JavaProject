import app.CliApp;
import app.GuiApp;

public final class AppMain {
	public static void main(String[] args) {
        if (CliApp.dialog("Do you want to use the graphical variant instead of the console one?")) {
            // Start the GUI app
			GuiApp.run();
        } else {
			// Fallback to CLI app
			CliApp.run();
		}
	}
}
