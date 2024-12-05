import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class PathFinder {
    private static final int GRID_SIZE = 8;
    private static final int TOTAL_MOVES = 63;

    // Directions: Down, Up, Left, Right
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
    private static final char[] DIR_CHARS = {'D', 'U', 'L', 'R'};

    public static void main(String[] args) {
        // Increase heap size to handle complex computations
        // Recommended to run with: java -Xmx4g -XX:+UseParallelGC -XX:+UseParallelOldGC 'PathFinder'
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Forest Path Finder");
        System.out.println("Enter a path description of exactly 63 characters (U, D, L, R, *):");
        
        String input = scanner.nextLine().trim();
        
        // Validate input
        if (input.length() != TOTAL_MOVES || !isValidInput(input)) {
            System.out.println("Invalid input. Must be 63 characters long with only U, D, L, R, *");
            return;
        }

        long startTime = System.currentTimeMillis();
        
        // Use AtomicLong to handle thread-safe counting
        AtomicLong totalPathsCounter = new AtomicLong(0);
        
        // Parallel exploration with bit manipulation for visited states
        explorePathsParallel(input, totalPathsCounter);
        
        long endTime = System.currentTimeMillis();

        System.out.println("Input: " + input);
        System.out.println("Total paths: " + totalPathsCounter.get());
        System.out.println("Time (ms): " + (endTime - startTime));
        
        scanner.close();
    }

    // Input validation method
    private static boolean isValidInput(String input) {
        for (char c : input.toCharArray()) {
            if (c != 'U' && c != 'D' && c != 'L' && c != 'R' && c != '*') {
                return false;
            }
        }
        return true;
    }

    // Parallel path exploration using bit manipulation
    private static void explorePathsParallel(String pathDescription, AtomicLong totalPathsCounter) {
        // Use parallel stream to distribute initial direction exploration
        java.util.stream.IntStream.range(0, DIRECTIONS.length)
            .parallel()
            .forEach(initialDirIndex -> {
                // Initial move for each direction
                int startRow = DIRECTIONS[initialDirIndex][0];
                int startCol = DIRECTIONS[initialDirIndex][1];

                // Check if initial move is valid
                if (isValidInitialMove(startRow, startCol)) {
                    // Use bit manipulation for visited state
                    long initialVisitedState = (1L << (startRow * GRID_SIZE + startCol));
                    
                    // Recursive exploration with bit-based visited tracking
                    long pathCount = explorePathWithBitmap(
                        startRow, 
                        startCol, 
                        1, 
                        pathDescription, 
                        initialVisitedState
                    );
                    
                    // Thread-safe addition to total paths
                    totalPathsCounter.addAndGet(pathCount);
                }
            });
    }

    // Recursive path exploration using bit manipulation
    private static long explorePathWithBitmap(
        int row, 
        int col, 
        int moveIndex, 
        String pathDescription, 
        long visitedState
    ) {
        // Base case: completed all moves
        if (moveIndex == TOTAL_MOVES) {
            return (row == GRID_SIZE - 1 && col == 0) ? 1 : 0;
        }

        long pathCount = 0;
        char currentMove = pathDescription.charAt(moveIndex);

        // If current move is a wildcard, try all directions
        if (currentMove == '*') {
            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];
                
                // Check if the new position is valid and not visited
                long cellBit = 1L << (newRow * GRID_SIZE + newCol);
                if (isValidMove(newRow, newCol, visitedState)) {
                    pathCount += explorePathWithBitmap(
                        newRow, 
                        newCol, 
                        moveIndex + 1, 
                        pathDescription, 
                        visitedState | cellBit
                    );
                }
            }
        } else {
            // Find the index of the current move's direction
            int dirIndex = -1;
            for (int i = 0; i < DIR_CHARS.length; i++) {
                if (DIR_CHARS[i] == currentMove) {
                    dirIndex = i;
                    break;
                }
            }

            // Compute new position
            int newRow = row + DIRECTIONS[dirIndex][0];
            int newCol = col + DIRECTIONS[dirIndex][1];

            // Check if the new position is valid and not visited
            long cellBit = 1L << (newRow * GRID_SIZE + newCol);
            if (isValidMove(newRow, newCol, visitedState)) {
                pathCount = explorePathWithBitmap(
                    newRow, 
                    newCol, 
                    moveIndex + 1, 
                    pathDescription, 
                    visitedState | cellBit
                );
            }
        }

        return pathCount;
    }

    // Check if initial move is within grid
    private static boolean isValidInitialMove(int row, int col) {
        return row >= 0 && row < GRID_SIZE && 
               col >= 0 && col < GRID_SIZE;
    }

    // Check if move is valid using bit manipulation
    private static boolean isValidMove(int row, int col, long visitedState) {
        // Check grid boundaries and visited state
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return false;
        }
        
        // Check if cell is already visited using bit manipulation
        long cellBit = 1L << (row * GRID_SIZE + col);
        return (visitedState & cellBit) == 0;
    }
}