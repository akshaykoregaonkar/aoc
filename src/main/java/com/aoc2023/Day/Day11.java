package com.aoc2023.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day11 implements DayInteface {
    private int matrixLength;
    List<String> universe = new ArrayList<>();
    List<long[]> galaxyIndexes = new ArrayList<>();

    @Override
    public void processLine(String line) {
        DayInteface.super.processLine(line);
        universe.add(line);
    }

    @Override
    public void processFile(String filePath) {
        DayInteface.super.processFile(filePath);
        processGalaxies();
    }

    private void processGalaxies() {
        findGalaxyIndexes();
    }

    public void solve(boolean part1) {
        int expansionCost = part1 ? 2 : 1_000_000;
        long sum = 0;

        expandGalaxies(expansionCost);

        for (int i = 0; i < galaxyIndexes.size() - 1; i++) {
            for (int j = i; j < galaxyIndexes.size(); j++) {
                sum += getManhattanDist(galaxyIndexes.get(i), galaxyIndexes.get(j));
            }
        }
        System.out.println(part1 ? "Part 1" : "Part 2" + sum);
    }

    private void expandGalaxies(int expansionCost) {
        Set<Long> rowPositions = galaxyIndexes.stream().map(g -> g[0]).collect(Collectors.toSet());
        Set<Long> colPositions = galaxyIndexes.stream().map(g -> g[1]).collect(Collectors.toSet());
        List<Long> emptyRows = LongStream.rangeClosed(0, matrixLength - 1).filter(g -> !rowPositions.contains(g)).boxed().collect(Collectors.toList());
        List<Long> emptyCols = LongStream.rangeClosed(0, matrixLength - 1).filter(g -> !colPositions.contains(g)).boxed().collect(Collectors.toList());

        for(int i = 0; i < galaxyIndexes.size(); i++){
            long[] galaxy = galaxyIndexes.get(i);

            long deltaRows =  emptyRows.stream().filter(emptyCol -> galaxy[0] > emptyCol).count();
            long deltaCols =  emptyCols.stream().filter(emptyRow -> galaxy[1] > emptyRow).count();
            long newRow = deltaRows * (expansionCost - 1) + galaxy[0];
            long newCol = deltaCols * (expansionCost - 1) + galaxy[1];
            galaxyIndexes.set(i, new long[]{newRow, newCol});
        }
    }

    private long getManhattanDist(long[] galaxy1, long[] galaxy2){
        return Math.abs(galaxy2[0] - galaxy1[0]) + Math.abs(galaxy2[1] - galaxy1[1]);
    }

    private void findGalaxyIndexes() {
        matrixLength = universe.get(0).length();
        for(int i = 0; i < universe.size(); i++){
            int row = i;
            String galaxyString = universe.get(i);
            IntStream galaxies = findGalaxiesInString(galaxyString, matrixLength);
            galaxies.forEach(col -> galaxyIndexes.add(new long[]{row, col}));
        }
    }

    private IntStream findGalaxiesInString(String galaxyString, int length) {
        return IntStream.range(0, length)
                .filter(i -> galaxyString.charAt(i) == '#');
    }
}