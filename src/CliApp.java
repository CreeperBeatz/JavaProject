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
        while (true) {
            System.out.print("Please enter path to a plain text (e.g.: *.txt) file: ");

            try (AppCore appCore = new AppCore(input.nextLine())) {
                boolean loop = true;

                do {
                    System.out.println("Please select an option from below:\n(1) Swap two lines\n(2) Swap two words\n(3) Select another file\n(4) Exit\n");

                    switch (inputNumber()) {
                    case 1:
                        swapLines(appCore);
                        break;
                    case 2:
                        swapWords(appCore);
                        break;
                    case 3:
                        loop = false;
                        break;
                    case 4:
                        // Writes down the new content and closes the file.
                        appCore.close();
                        return;
                    default:
                        break;
                    }
                } while (loop);
            } catch (FileNotFoundException exception) {
                if (!continueUsing("Cannot open specified file.\nWould you like to try with another file?")) {
                    break;
                }
            } catch (SecurityException exception) {
                System.out.println("Cannot operate on file die to security exception!");

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
            System.out.println("No such line(s) and/or word(s)!");
        }
    }

    private static boolean continueUsing(String message) {
        System.out.print(message);
        // Not needed anymore.
        message = null;

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
