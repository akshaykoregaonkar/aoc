package com.aoc2023.Day;

import java.util.HashMap;
import java.util.Map;

public class Day1 extends Day{
    private static final Map<String, Integer> wordToNumber = new HashMap<>();
    {
        wordToNumber.put("one", 1);
        wordToNumber.put("two", 2);
        wordToNumber.put("three", 3);
        wordToNumber.put("four", 4);
        wordToNumber.put("five", 5);
        wordToNumber.put("six", 6);
        wordToNumber.put("seven", 7);
        wordToNumber.put("eight", 8);
        wordToNumber.put("nine", 9);
    }

    Day1(String filePath) {
        super(filePath);
    }

    @Override
    protected long daySpecificCalculation(String line) {
        int firstNum = getFirstNum(line);
        int secondNum = getLastNum(line);
        return (firstNum*10) + secondNum;
    }

    private int getFirstNum(String line) {
        for (int i = 0; i < line.length(); i++) {
            if(Character.isDigit(line.charAt(i))){
                return line.charAt(i) - '0';
            }else{
                for (int j = i + 1; j < line.length(); j++) {
                    if( wordToNumber.containsKey(line.substring(i, j)) ){
                        return wordToNumber.get(line.substring(i, j));
                    }
                }
            }
        }
        throw new IllegalArgumentException("First numeric value not found in line");
    }

    private int getLastNum(String line) {
        for (int i = line.length() - 1; i >= 0; i--) {
            if(Character.isDigit(line.charAt(i))){
                return line.charAt(i) - '0';
            }else{
                for (int j = i - 1; j >= 0; j--) {
                    if(wordToNumber.containsKey(line.substring(j, i+1))){
                        return wordToNumber.get(line.substring(j, i+1));
                    }
                }
            }
        }
        throw new IllegalArgumentException("Last numeric value not found in line");
    }
}
