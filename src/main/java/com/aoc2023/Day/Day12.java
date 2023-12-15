package com.aoc2023.Day;

import org.apache.commons.math3.analysis.function.StepFunction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 extends Day{
    private static final char DAMAGED = '#';
    private static final char OPERATIONAL = '.';
    private static final char UNKNOWN = '?';
    private boolean part1;
    private static Map<String, Long> cache;
    public Day12(String filePath, boolean part1) {
        super(filePath);
        this.part1 = part1;
        cache = new HashMap<>();
    }

    @Override
    protected long daySpecificCalculation(String line) {
        String[] parts = line.split(" ");
        String record = parts[0];
        Integer[] info = Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        if(part1){
            return countPermutations(record + ".", info, 0);
        }
        return countPermutations(repeatStringWithSeparator(record, 5, "?")  + ".", repeatSizes(info, 5), 0);

    }

    private static String repeatStringWithSeparator(String s, int times, String separator) {
        return IntStream.range(0, times)
                .mapToObj(i -> s)
                .collect(Collectors.joining(separator));
    }

    private static Integer[] repeatSizes(Integer[] sizes, int times) {
        return Stream.iterate(sizes, ignored -> sizes)
                .limit(times)
                .flatMap(Arrays::stream)
                .toArray(Integer[]::new);
    }

    private long countPermutations(String record, Integer[] sizes, int numDoneInGroup) {
        String key = record + Arrays.toString(sizes) + numDoneInGroup;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (record.isEmpty()) {
            long result = sizes.length == 0 && numDoneInGroup == 0 ? 1 : 0;
            cache.put(key, result);
            return result;
        }

        long numSols = 0;
        char[] possible = record.charAt(0) == UNKNOWN ? new char[]{OPERATIONAL, DAMAGED} : new char[]{record.charAt(0)};
        for (char c : possible) {
            if (c == DAMAGED) {
                numSols += countPermutations(record.substring(1), sizes, numDoneInGroup + 1);
            } else {
                if (numDoneInGroup > 0) {
                    if (sizes.length > 0 && sizes[0] == numDoneInGroup) {
                        numSols += countPermutations(record.substring(1), Arrays.copyOfRange(sizes, 1, sizes.length), 0);
                    }
                } else {
                    numSols += countPermutations(record.substring(1), sizes, 0);
                }
            }
        }

        cache.put(key, numSols);
        return numSols;
    }
}