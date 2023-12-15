package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public interface DayInteface {
    default void processFile(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void processLine(String line){

    }

}
