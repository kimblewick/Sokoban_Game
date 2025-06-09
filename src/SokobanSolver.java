/**
 * @author Raj Kavathekar
 * @version 1.0
 * This class is used to solve the Sokoban game.
 * It uses a breadth-first search algorithm to find a solution to the game.
 * It is used to check if a level is solvable.
 */

import java.util.*;


public class SokobanSolver {
    private static class State {
        final int playerRow;
        final int playerCol;
        final int[] boxes; // sorted, encoded as (row * size + col)
        final String key;  // cached string key for hashing

        State(int playerRow, int playerCol, int[] boxes) {
            this.playerRow = playerRow;
            this.playerCol = playerCol;
            this.boxes = boxes;
            Arrays.sort(this.boxes); // ensure deterministic ordering
            StringBuilder sb = new StringBuilder();
            sb.append(playerRow).append(',').append(playerCol).append('-');
            for (int b : this.boxes) {
                sb.append(b).append(';');
            }
            this.key = sb.toString();
        }
    }

    /**
     * Attempts to solve a given Level instance using breadth-first search.
     * Returns true if at least one solution exists.
     */
    public static boolean isLevelSolvable(Level level) {
        int[][] grid = level.getGrid();
        final int size = grid.length;

        // Extract static map information once.
        boolean[] wallMask = new boolean[size * size];
        boolean[] goalMask = new boolean[size * size];
        List<Integer> initialBoxes = new ArrayList<>();
        int startRow = -1, startCol = -1;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int tile = grid[r][c];
                int idx = r * size + c;
                if ((tile & TileType.WALL) != 0) {
                    wallMask[idx] = true;
                }
                if ((tile & TileType.GOAL) != 0) {
                    goalMask[idx] = true;
                }
                if ((tile & TileType.BOX) != 0) {
                    initialBoxes.add(idx);
                }
                if ((tile & TileType.PLAYER) != 0) {
                    startRow = r;
                    startCol = c;
                }
            }
        }

        if (startRow == -1) {
            // No player found – invalid level.
            return false;
        }

        int[] boxArr = initialBoxes.stream().mapToInt(Integer::intValue).toArray();
        State start = new State(startRow, startCol, boxArr);

        Queue<State> queue = new ArrayDeque<>();
        queue.add(start);
        Set<String> visited = new HashSet<>();
        visited.add(start.key);

        final int[] dRow = {-1, 1, 0, 0};
        final int[] dCol = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            // Check goal condition, every goal must have a box on it.
            boolean allGoalsCovered = true;
            for (int i = 0; i < goalMask.length; i++) {
                if (goalMask[i] && !contains(cur.boxes, i)) {
                    allGoalsCovered = false;
                    break;
                }
            }
            if (allGoalsCovered) {
                return true;
            }

            // Try moving in four directions.
            for (int dir = 0; dir < 4; dir++) {
                int newPlayerRow = cur.playerRow + dRow[dir];
                int newPlayerCol = cur.playerCol + dCol[dir];

                if (!inBounds(newPlayerRow, newPlayerCol, size)) continue;
                int nextIdx = newPlayerRow * size + newPlayerCol;

                if (wallMask[nextIdx]) continue; // Wall blocks movement.

                boolean[] boxSet = toBooleanBoxSet(cur.boxes, size * size);

                if (boxSet[nextIdx]) {
                    // There is a box in the square, attempt to push.
                    int boxNextRow = newPlayerRow + dRow[dir];
                    int boxNextCol = newPlayerCol + dCol[dir];
                    if (!inBounds(boxNextRow, boxNextCol, size)) continue;
                    int boxNextIdx = boxNextRow * size + boxNextCol;
                    if (wallMask[boxNextIdx] || boxSet[boxNextIdx]) continue; // Can't push – blocked.

                    // Perform push.
                    int[] newBoxes = Arrays.copyOf(cur.boxes, cur.boxes.length);
                    for (int i = 0; i < newBoxes.length; i++) {
                        if (newBoxes[i] == nextIdx) {
                            newBoxes[i] = boxNextIdx;
                            break;
                        }
                    }
                    State newState = new State(newPlayerRow, newPlayerCol, newBoxes);
                    if (visited.add(newState.key)) {
                        queue.add(newState);
                    }
                } else {
                    // Simple move (no box).
                    State newState = new State(newPlayerRow, newPlayerCol, cur.boxes.clone());
                    if (visited.add(newState.key)) {
                        queue.add(newState);
                    }
                }
            }
        }
        // Explored whole state space without success.
        return false;
    }

    private static boolean inBounds(int r, int c, int size) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    private static boolean contains(int[] arr, int val) {
        for (int a : arr) {
            if (a == val) return true;
        }
        return false;
    }

    private static boolean[] toBooleanBoxSet(int[] boxes, int total) {
        boolean[] set = new boolean[total];
        for (int b : boxes) {
            if (b >= 0 && b < total) {
                set[b] = true;
            }
        }
        return set;
    }
} 