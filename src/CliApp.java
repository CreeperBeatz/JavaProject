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
        System.out.println("Please enter path to a plain text (e.g.: *.txt) file that contains only words");

        try (AppCore appCore = new AppCore("test.txt")) {
            appCore.swapWords(0, 0, 0, 2);
        } catch (FileNotFoundException exception) {

        } catch (SecurityException exception) {
            System.out.println("Cannot operate on file die to security exception!");
        } catch (InvalidContentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
