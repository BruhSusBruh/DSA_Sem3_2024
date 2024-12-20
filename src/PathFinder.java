
public class PathFinder {

    private final char[][] grid;
    private final int n;
    private final static int dx[] = new int[]{1, 0, -1, 0};
    private final static int dy[] = new int[]{0, 1, 0, -1};
    private final boolean[][] visited;
    private long totalPath = 0;

    public PathFinder(String path) {
        if (path.length() == 35 || path.length() == 63) {
            path = "*" + path;
        }
        this.n = path.length() == 36 ? 6 : 8;
        grid = new char[n][n];
        visited = new boolean[n][n];

        int row = 0, col = 0;
        for (int i = 0; i < path.length(); i++) {
            grid[row][col++] = path.charAt(i);
            if (col % n == 0) {
                row++;
                col = 0;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visited[i][j] = false;
            }
        }

        visited[0][0] = true;
    }

    public void explore(int x, int y, String path) {
        // System.out.println("Col: " + x + " Row: " + y + " Path: " + path); // Debugging
        if (x == n - 1 && y == 0) {
            if (path.length() == n * n - 1) {
                totalPath++;
            }
            return;
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            char move = getMove(x, y, newX, newY);
            if (isValidLocation(newX, newY) && isValidMove(move, x, y)) {
                visited[newX][newY] = true;
                explore(newX, newY, path + move);
                visited[newX][newY] = false;
            }
        }
    }

    public boolean isValidLocation(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n && !visited[x][y];
    }

    public boolean isValidMove(char move, int curX, int curY) {
        if (grid[curX][curY] == '*') {
            return true;
        }
        return grid[curX][curY] == move;
    }

    public char getMove(int x, int y, int newX, int newY) {
        if (x == newX) {
            return newY == y + 1 ? 'R' : 'L';
        }
        return newX == x + 1 ? 'D' : 'U';
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < n; i++) {
            String tmp = "";
            for (int j = 0; j < n; j++) {
                tmp += grid[i][j] + " ";
            }
            result += tmp + "\n";
        }
        return result;
    }

    public long getTotalPath() {
        return totalPath;
    }
}
