import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public final class CliApp {
    private static final Scanner input;

    static {
        input = new Scanner(System.in);
    }

    private CliApp() {
        super();
    }

    public static void main(String[] args) {
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
                if (!continueUsing("Cannot open specified file!")) {
                    break;
                }
            } catch (IOException exception) {
                System.err.println("Cannot write down the new content to the file!");

                return;
            } catch (SecurityException exception) {
                System.err.println("Cannot operate on file die to security exception!");

                return;
            }
        }
    }

    private static void swapLines(AppCore appCore) {
        int firstLine, secondLine;

        do {
            System.out.print("Enter first line's number: ");

            firstLine = inputNumber() - 1;

            if (appCore.isLineInBounds(firstLine)) {
                break;
            } else {
                System.err.println("Please enter a line number between 1 and the number of lines!");
            }
        } while (true);

        do {
            System.out.print("Enter second line's number: ");

            secondLine = inputNumber() - 1;

            if (appCore.isLineInBounds(secondLine)) {
                break;
            } else {
                System.err.println("Please enter a line number between 1 and the number of lines!");
            }
        } while (true);

        try {
            appCore.swapLines(firstLine, secondLine);
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("No such line(s)!");
        }
    }

    private static void swapWords(AppCore appCore) {
        int firstLine, firstWord, secondLine, secondWord;

        firstLine = inputLineNumber(appCore, "first");

        firstWord = inputWordNumber(appCore, firstLine, "first");

        secondLine = inputLineNumber(appCore, "second");

        secondWord = inputWordNumber(appCore, secondLine, "second");

        try {
            appCore.swapWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);
        } catch (IndexOutOfBoundsException exception) {
            // All indexes are bound checked.
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
            } catch (NumberFormatException exception) {
                System.err.println("Please enter valid whole number!");
            }
        } while (true);
    }

    private static int inputLineNumber(AppCore appCore, String line) {
        int index;

        do {
            System.out.print("Enter ");
            System.out.print(line);
            System.out.print(" line's number: ");

            index = inputNumber() - 1;

            if (appCore.isLineInBounds(index)) {
                if (appCore.lineHasWords(index)) {
                    break;
                } else {
                    System.err.println("Please select a line that has words!");
                }
            } else {
                System.err.println("Please enter a line number between 1 and the number of lines!");
            }
        } while (true);

        return index;
    }

    private static int inputWordNumber(AppCore appCore, int line, String word) {
        int index;

        do {
            System.out.print("Enter ");
            System.out.print(word);
            System.out.print(" word's number: ");

            index = inputNumber() - 1;

            if (appCore.isWordInBounds(line, index)) {
                break;
            } else {
                System.err.println("Please enter a word number between 1 and the number of words on that line!");
            }
        } while (true);

        return index;
    }
}
