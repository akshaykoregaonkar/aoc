package com.aoc2023.Day;

import java.util.List;

public class Input {
    private String record;
    private List<Integer> info;

    public Input(String record, List<Integer> info) {
        this.record = record;
        this.info = info;
    }

    List<Integer> group(){
        return this.info;
    }
    String condition(){
        return this.record;
    }
}
