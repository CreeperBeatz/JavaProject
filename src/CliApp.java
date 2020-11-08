import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class CliApp {
    private static final Scanner input;

    static {
        input = new Scanner(System.in);
    }

    private CliApp() {
        super();
    }

    public static void main(String[] args) throws Throwable {
        if (dialog("Do you want to use the GUI (Graphical User Interface) instead?")) {
            // Start the GUI app
            try {
                new GuiApp();
            } catch (HeadlessException exception) {
                System.err.println("Cannot operate in graphical mode when there is no graphics environment available!");
            }

            return;
        }

        // Fallback to CLI app

        while (true) {
            System.out.print("Please enter path to a plain text (e.g.: *.txt) file: ");

            try (AppCore appCore = new AppCore(input.nextLine())) {
                System.out.println();

                boolean loop = true;

                do {
                    System.out.println("Content:");
                    System.out.println(appCore.getContentString());
                    System.out.println();
                    System.out.println("Please select an option from below:\n(1) Swap two lines\n(2) Swap two words\n(3) Add new line\n(4) Remove last line [has to be empty]\n(5) Select another file\n(6) Exit\n");

                    switch (inputNumber()) {
                    case 1:
                        swapLines(appCore);
                        break;
                    case 2:
                        swapWords(appCore);
                        break;
                    case 3:
                        appCore.addEmptyLine();
                        break;
                    case 4:
                        if (appCore.isEmpty()) {
                            System.err.println("There are no lines!");
                        } else if (!appCore.isLastLineEmpty()) {
                            System.err.println("Last line is not empty!");
                        } else {
                            appCore.removeLastLine();
                        }
                    case 5:
                        loop = false;
                        break;
                    case 6:
                        // Writes down the new content and closes the file.
                        appCore.close();
                        return;
                    default:
                        break;
                    }
                } while (loop);
            } catch (FileNotFoundException exception) {
                if (!continueUsing("Cannot open specified file.")) {
                    break;
                }
            } catch (SecurityException exception) {
                System.err.println("Cannot operate on file die to security exception!");

                return;
            }
        }
    }

    private static void swapLines(AppCore appCore) {
        int firstLine, secondLine;

        System.out.print("Enter first line's number: (1, 2, ...) ");

        firstLine = inputNumber();

        System.out.print("Enter second line's number: (1, 2, ...) ");

        secondLine = inputNumber();

        try {
            appCore.swapLines(firstLine - 1, secondLine - 1);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("No such line(s)!");
        }
    }

    private static void swapWords(AppCore appCore) {
        int firstLine, firstWord, secondLine, secondWord;

        System.out.print("Enter first line's number: (1, 2, ...) ");

        firstLine = inputNumber();

        System.out.print("Enter first word's number: (1, 2, ...) ");

        firstWord = inputNumber();

        System.out.print("Enter second line's number: (1, 2, ...) ");

        secondLine = inputNumber();

        System.out.print("Enter second word's number: (1, 2, ...) ");

        secondWord = inputNumber();

        try {
            appCore.swapWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);
        } catch (IndexOutOfBoundsException exception) {
            System.err.println("No such line(s) and/or word(s)!");
        }
    }

    private static boolean continueUsing(String message) {
        if (message != null) {
            if (!message.isEmpty()) {
                System.out.println(message);

                // Not needed anymore.
                message = null;
            }
        }
        
        return dialog("Would you like to try with another file?");
    }

    /**
     * @param message
     * @return Returns true when "Yes" is selected and false when "No" is selected.
     */
    private static boolean dialog(String message) {
        if (message != null) {
            System.out.print(message);

            // Not needed anymore.
            message = null;
        }

        System.out.println(" (Y)es / (N)o");

        while (true) {
            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
        }
    }

    private static int inputNumber() {
        do {
            try {
                return Integer.parseUnsignedInt(input.nextLine());
            } catch (NumberFormatException exception) {}
        } while (true);
    }
}
