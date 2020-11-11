package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import app.core.AppCore;

/**
 * Represents the Command Line Interface (CLI) variant of the application.
 * This class is responsible for all the interfaces provided by the application's CLI variant.
 * This is a utility class, therefore uninstantiable (from outside) nor inheritable.
 * To use this class see the {@code run} method.
 */
public final class CliApp {
    private static final Scanner input;

    static {
        input = new Scanner(System.in);
    }

    private CliApp() {
        super();
    }

    /**
     * This method is used to start the application in CLI mode.
     */
    public static void run() {
        try {
            while (true) {
                System.out.print("Please enter path to a plain text file (*.txt): ");

                File filepath = new File(input.nextLine());

                // Make sure the extension is correct.
                if (!filepath.getName().endsWith(".txt")) {
                    if (!continueUsing("Selected a file that is not plain text (*.txt)!")) {
                        break;
                    }

                    continue;
                }

                // Make sure the file can be read.
                if (!filepath.canRead()) {
                    if (!continueUsing("Cannot read the content of the file!")) {
                        break;
                    }

                    continue;
                }

                // Make sure the file can be written.
                if (!filepath.canWrite()) {
                    if (!continueUsing("Cannot write content to the file!")) {
                        break;
                    }

                    continue;
                }

                try (AppCore appCore = new AppCore(filepath.getAbsolutePath())) {
                    System.out.println();

                    boolean loop = true;

                    do {
                        System.out.println("Content:");
                        System.out.println(appCore.getContentString());
                        System.out.println();
                        System.out.println("Please select an option from below:\n(1) Swap two lines\n(2) Swap two words\n(3) Add new empty line\n(4) Remove last line [has to be empty]\n(5) Select another file\n(6) Exit\n");

                        switch (inputNumber()) {
                        case 1:
                            // Swap two lines
                            swapLines(appCore);
                            break;
                        case 2:
                            // Swap two words
                            swapWords(appCore);
                            break;
                        case 3:
                            // Add a new empty line
                            appCore.addEmptyLine();
                            break;
                        case 4:
                            // Remove last line
                            if (appCore.isEmpty()) {
                                System.err.println("There are no lines!");
                            } else if (!appCore.isLastLineEmpty()) {
                                System.err.println("Last line is not empty!");
                            } else {
                                appCore.removeLastLine();
                            }
                            break;
                        case 5:
                            // Select another file
                            loop = false;
                            break;
                        case 6:
                            // Exit
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
                }
            }
        } catch (SecurityException exception) {
            System.err.println("Cannot operate on file die to security exception!");

            return;
        }
    }

    /**
     * Performs the line swapping operation.
     * @param appCore The AppCore's instance holding the current session.
     */
    private static void swapLines(AppCore appCore) {
        if (appCore.lineCount() < 2) {
            System.err.println("Not enough lines to perform a swap!");

            return;
        }

        int firstLine = inputLineNumber(appCore, "first");

        int secondLine = inputLineNumber(appCore, "second");

        appCore.swapLines(firstLine, secondLine);
    }

    /**
     * Performs the word swapping operation.
     * @param appCore The AppCore's instance holding the current session.
     */
    private static void swapWords(AppCore appCore) {
        {
            int totalWordCount = 0;

            for (int z = 0; z < appCore.lineCount(); ++z) {
                totalWordCount += appCore.wordCountOnLine(z, 2 - totalWordCount);

                if (totalWordCount > 1) {
                    break;
                }
            }

            if (totalWordCount < 2) {
                System.err.println("Not enough words to perform a swap!");

                return;
            }
        }

        int firstLine = inputLineNumber(appCore, "first");

        int firstWord = inputWordNumber(appCore, firstLine, "first");

        int secondLine = inputLineNumber(appCore, "second");

        int secondWord = inputWordNumber(appCore, secondLine, "second");

        appCore.swapWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);
    }

    /**
     * Makes Console Line Interface (CLI) dialog asking whether the user wishes to continue using the program with another file.
     * @param message A reference to a string containing an additional message to be printed.
     * If it's a null reference, then no additional messages are printed.
     * @return True when the user wishes to continue. Otherwise, false.
     */
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
     * Makes Console Line Interface (CLI) dialog with "Yes" and "No" answers.
     * @param message A reference to a string containing a message to be printed.
     * If it's a null reference, then no messages are printed.
     * @return Returns true when "Yes" is selected and false when "No" is selected.
     */
    public static boolean dialog(String message) {
        if (message != null) {
            System.out.print(message);

            // Not needed anymore.
            message = null;

            System.out.print(" ");
        }

        System.out.println("(Y)es / (N)o");

        while (true) {
            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            }
        }
    }

    /**
     * A helper function for number input. It requires the user to enter a positive integer.
     * @return The parsed positive integer.
     */
    private static int inputNumber() {
        do {
            try {
                return Integer.parseUnsignedInt(input.nextLine());
            } catch (NumberFormatException exception) {
                System.err.println("Please enter valid whole number!");
            }
        } while (true);
    }

    /**
     * A helper function for for line number input.
     * @param appCore The AppCore's instance holding the current session.
     * @param line A reference to a string containing the requested line's name in ordering (e.g.: first, second, etc.).
     * @return The parsed, bound checked, line number.
     */
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

    /**
     * A helper function for for line number input.
     * @param appCore The AppCore's instance holding the current session.
     * @param line A line number, that is assumed to be bound checked, for which the word number is inputted.
     * @param word A reference to a string containing the requested line's name in ordering (e.g.: first, second, etc.).
     * @return The parsed, bound checked, word number.
     */
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
