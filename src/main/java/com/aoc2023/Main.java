package com.aoc2023;

import com.aoc2023.Day.Day11;
import com.aoc2023.Day.Day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
//        Day11 day11 = new Day11();
//        day11.processFile("src/main/resources/day11_input.txt");
//        day11.solve(true);

        Day12 day12 = new Day12("src/main/resources/day12_input.txt", false);
        System.out.println(day12.getTotalFromFile());
    }
}