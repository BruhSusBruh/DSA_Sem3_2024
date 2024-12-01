public class GridExplorer {
    public static final int GRID_SIZE = 8;

    public static void main(String[] args) {
        // Get input from users
        String path = Utility.promptForValidInput();

        //
        PathFinder pathFinder = new PathFinder(GRID_SIZE);
        long executionTime = Utility.measureExecutionTime(() -> {
            boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
            visited[0][0] = true;
            pathFinder.explore(0, 0, 0, path, visited);
        });

        // Output on screen
        System.out.println("Total paths: " + pathFinder.getTotalPaths());
        System.out.println("Time (ms): " + executionTime);
    }
}
