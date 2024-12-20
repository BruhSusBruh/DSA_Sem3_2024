
public class GridExplorer {

    public static void main(String[] args) {
        // Get input from users
        String path = Utility.promptForValidInput();

        PathFinder pathFinderV2 = new PathFinder(path);

        System.out.println(pathFinderV2);

        long executionTime = Utility.measureExecutionTime(() -> {
            pathFinderV2.explore(0, 0, "");
        });

        //  Output on screen
        System.out.println("Input: " + path);
        System.out.println("Total paths: " + pathFinderV2.getTotalPath());
        System.out.println("Time (ms): " + executionTime);
    }
}