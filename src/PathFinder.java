import java.util.HashMap;
import java.util.Map;

public class PathFinder {
    private final int gridSize;
    private long totalPaths = 0;
    private final Map<String, Long> memo = new HashMap<>(); // Memoization

    public PathFinder(int gridSize) {
        this.gridSize = gridSize;
    }

    public void explore(int row, int col, int step, String path, boolean[][] visited) {
        // If finished all steps, check if we are at the end point
        if (step == path.length()) {
            if (row == gridSize - 1 && col == 0) {
                totalPaths++;
            }
            return;
        }

        // Check the memoization to avoid double calculating
        String stateKey = row + "," + col + "," + step;
        if (memo.containsKey(stateKey)) {
            totalPaths += memo.get(stateKey);
            return;
        }

        char direction = path.charAt(step);
        long pathsFromCurrentState = 0;

        // Check all possible directions (*)
        for (char dir : getPossibleDirections(direction)) {
            int newRow = row, newCol = col;
            if (dir == 'D') newRow++;
            else if (dir == 'U') newRow--;
            else if (dir == 'L') newCol--;
            else if (dir == 'R') newCol++;

            //
            if (isValidMove(newRow, newCol, visited)) {
                visited[newRow][newCol] = true;
                explore(newRow, newCol, step + 1, path, visited);
                pathsFromCurrentState += totalPaths;
                visited[newRow][newCol] = false;
            }
        }

        // Safe the output to memo
        memo.put(stateKey, pathsFromCurrentState);
    }

    private char[] getPossibleDirections(char direction) {
        return (direction == '*') ? new char[]{'D', 'U', 'L', 'R'} : new char[]{direction};
    }

    private boolean isValidMove(int row, int col, boolean[][] visited) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize && !visited[row][col];
    }

    public long getTotalPaths() {
        return totalPaths;
    }
}
