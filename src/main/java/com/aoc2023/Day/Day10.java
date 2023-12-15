package com.aoc2023.Day;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 implements DayInteface{
    List<char[]> field = new ArrayList<>();
    int rows, cols;
    int [][] distances;

    private static final Map<Character, int[][]> DIRECTION_OFFSETS = Map.of(
            'S', new int[][]{{0, -1}, {1, 0}},
            '7', new int[][]{{0, -1}, {1, 0}},
            'L', new int[][]{{-1, 0}, {0, 1}},
            'F', new int[][]{{0, 1}, {1, 0}},
            'J', new int[][]{{-1, 0}, {0, -1}},
            '|', new int[][]{{-1, 0}, {1, 0}},
            '-', new int[][]{{0, -1}, {0, 1}}
    );

    @Override
    public void processLine(String line) {
        field.add(line.toCharArray());
    }

    @Override
    public void processFile(String filePath) {
        DayInteface.super.processFile(filePath);
        initDistances();
    }

    void solve(){
        int dist = findMaxDistance();
        int dots = findDotsInPath();
        System.out.println("Part1: " + dist);
        System.out.println("Part2: " + dots);
    }

    private int findDotsInPath() {
        char[][] cleanedMap = new char[field.size()][field.get(0).length];
        // set all junk piles as dot
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cleanedMap[i][j] = distances[i][j] == -1 ? '.' : field.get(i)[j];
            }
        }

        // the boundaries for the shape are dictated by F---7....L---J or F---J...L--7
        // create strings, so we only have vertical pipes (|) and then count dots between two pipes
        List<String> cleanedStrings = Arrays.stream(cleanedMap)
                .map(String::new)
                .map(row -> markBoundaries(row))
                .collect(Collectors.toList());

        int count = 0;

        for(String row: cleanedStrings){
            int parity = 0;
            for(char c: row.toCharArray()){
                switch (c) {
                    case '|':
                        parity++;
                        break;
                    case '.':
                        count = parity % 2 == 1 ? count + 1 : count;
                        break;
                }
            }
        }
        return count;
    }

    private String markBoundaries(String row) {
        return Pattern.compile("F-*7|L-*J").matcher(row).replaceAll("")
                .replaceAll("F-*J|L-*7", "|");
    }

    private void initDistances() {
        rows = field.size();
        cols = field.get(0).length;
        distances = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(distances[i], -1);
        }
    }

    private int findMaxDistance() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (field.get(i)[j] == 'S' && distances[i][j] == -1) {
                    bfs(i, j);
                    return findMaxDistanceInComponent();
                }
            }
        }
        return 0;
    }

    private int findMaxDistanceInComponent() {
        return Arrays.stream(distances)
                .flatMapToInt(Arrays::stream)
                .filter(distance -> distance != -1)
                .max()
                .orElse(0);
    }

    private void bfs(int startRow, int startCol) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol, 0});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int distance = current[2];
            char currentPipe = field.get(row)[col];

            if ( (row < 0 || row >= rows || col < 0 || col >= cols || field.get(row)[col] == '.') ||
                    (distances[row][col] != -1 && distances[row][col] <= distance) ) {
                continue;
            }

            distances[row][col] = distance;

            int[][] offsets = DIRECTION_OFFSETS.get(currentPipe);
            for(int[] offset: offsets){
                exploreNeighbor(queue, row + offset[0], col + offset[1], distance + 1);
            }
        }
    }

    private void exploreNeighbor(Queue<int[]> queue, int row, int col, int distance) {
        if (row >= 0 && row < rows && col >= 0 && col < cols && distances[row][col] <= distance) {
            queue.offer(new int[]{row, col, distance});
        }
    }
}