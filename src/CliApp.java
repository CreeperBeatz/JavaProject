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
                continue;
            } catch (SecurityException exception) {
                System.out.println("Cannot operate on file die to security exception!");
            }
            
            break;
        }
    }
}
