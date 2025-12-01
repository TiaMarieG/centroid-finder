package io.github.TiaMarieG.centroidFinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Identifies all connected groups of white pixels (1s) in a binary image.
 * Uses iterative DFS to avoid stack overflow issues.
 */
public class DfsBinaryGroupFinder implements BinaryGroupFinder {

    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if (image == null || image.length == 0 || image[0] == null) {
            throw new NullPointerException("Input image is null or improperly formatted.");
        }

        int rows = image.length;
        int cols = image[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<Group> groups = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (image[row][col] == 1 && !visited[row][col]) {
                    List<int[]> groupPixels = new ArrayList<>();
                    pixelGroupTraversal(image, visited, row, col, groupPixels);
                    groups.add(toGroup(groupPixels));
                }
            }
        }

        // Sort using Group's compareTo (descending by size, then centroid)
        Collections.sort(groups, Collections.reverseOrder());
        return groups;
    }

    /**
     * Performs an iterative DFS to explore a group of connected 1s.
     */
    private void pixelGroupTraversal(int[][] image, boolean[][] visited, int startRow, int startCol, List<int[]> groupPixels) {
        int rows = image.length;
        int cols = image[0].length;

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {startRow, startCol});
        visited[startRow][startCol] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int row = current[0];
            int col = current[1];

            groupPixels.add(new int[] {row, col});

            // Directions: up, down, left, right
            int[][] directions = {
                {-1, 0}, // up
                {1, 0},  // down
                {0, -1}, // left
                {0, 1}   // right
            };

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (!visited[newRow][newCol] && image[newRow][newCol] == 1) {
                        visited[newRow][newCol] = true;
                        stack.push(new int[] {newRow, newCol});
                    }
                }
            }
        }
    }

    /**
     * Converts a list of (row, col) int[] coordinates into a Group.
     */
    private Group toGroup(List<int[]> pixels) {
        int size = pixels.size();
        int totalX = 0;
        int totalY = 0;

        for (int[] coord : pixels) {
            totalX += coord[1]; // column is x
            totalY += coord[0]; // row is y
        }

        int centroidX = totalX / size;
        int centroidY = totalY / size;

        return new Group(size, new Coordinate(centroidX, centroidY));
    }
}
