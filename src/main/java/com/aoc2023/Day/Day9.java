package com.aoc2023.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 extends Day{
    Day9(String filePath) {
        super(filePath);
    }

    @Override
    protected long daySpecificCalculation(String line) {
        List<Integer> numbers = Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        // int lastNumber = numbers.get(numbers.size() - 1);
        int firstNumber = numbers.get(0);
        List<List<Integer>> diffs = new ArrayList<>();
        boolean isZero = false;
        while(!isZero) {
            List<Integer> levelDiff = new ArrayList<>();
            for (int i = 1; i < numbers.size(); i++) {
                int diff = numbers.get(i) - numbers.get(i - 1);
                levelDiff.add(diff);
            }
            diffs.add(levelDiff);
            if(levelDiff.stream().allMatch(i -> i == 0)){
                isZero = true;
            }
            numbers = levelDiff;
        }
        int arraySize = diffs.size();
        int prev = 0;
        for (int i = arraySize - 2; i >= 0 ; i--) {
            prev = diffs.get(i).get(0) - prev;
        }

        return firstNumber - prev ;
    }
}

/*
* 17 19 24 49 132 341 793 1706 3535 7295 15259 32346 68692 144135 295652 589171 1137653 2127907 3859274 6798103 11652852
* 2 5 25 83 209 452 913
* 3 20 58 126 243 461
* 17 38 68 117 344
* 17 30 49 227
* 13 19 178
* 13 - 10 = 3
* 10 = 13 - 3
 * */


/*
* 0  1      3   6  10  15  21  28
    1   2   3   4   5   6   7
 1   1   1   1   1   1
   0   0   0   0   0
* */