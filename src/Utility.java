
import java.util.Scanner;

public class Utility {

    public static String getInputFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a 35-character path (only D, U, L, R, *):");
        return scanner.nextLine();
    }

    public static boolean validateInput(String path) {
        if (path.length() != 35 && path.length() != 63) {
            displayErrorMessage("Error: Input must be 35 characters long.");
            return false;
        }
        for (char c : path.toCharArray()) {
            if ("DULR*".indexOf(c) == -1) {
                displayErrorMessage("Error: Invalid character '" + c + "'. Only D, U, L, R, or * are allowed.");
                return false;
            }
        }
        return true;
    }

    public static String promptForValidInput() {
        String input;
        do {
            input = getInputFromUser();
            if (!validateInput(input)) {
                System.out.println("Please try again.");
            }
        } while (!validateInput(input));
        return input;
    }

    public static long measureExecutionTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - startTime;
    }

    public static void displayErrorMessage(String message) {
        System.out.println(message);
    }
}
