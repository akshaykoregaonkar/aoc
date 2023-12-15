package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Day implements DayInteface{
    String filePath;

    Day(String filePath){
        this.filePath = filePath;
    }

    public long getTotalFromFile(){
        long total = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                total += daySpecificCalculation(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return total;
    }

    protected abstract long daySpecificCalculation(String line);
}

