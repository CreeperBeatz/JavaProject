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
                appCore.swapLines(0, 1);
            } catch (FileNotFoundException exception) {
                if (!continueUsing("Cannot open specified file.\nWould you like to try with another file?")) {
                    break;
                }
            } catch (SecurityException exception) {
                System.out.println("Cannot operate on file die to security exception!");
            }
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
}
