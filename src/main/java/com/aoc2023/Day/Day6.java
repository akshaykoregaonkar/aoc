package com.aoc2023.Day;

import java.util.HashMap;
import java.util.Map;

public class Day6{
    Map<Long, Long> timeDistanceMap = new HashMap<>();

    Day6(boolean isTest){
        if(isTest){
            timeDistanceMap.put(7l, 9l);
            timeDistanceMap.put(15l, 40l);
            timeDistanceMap.put(30l, 200l);
            timeDistanceMap.put(71530l, 940200l);
        }else {
            timeDistanceMap.put(40929790l, 215106415051100l);
//            timeDistanceMap.put(97l, 1505l);
//            timeDistanceMap.put(90l, 1100l);
        }
    }

    long calculateWins(){
        int winMult = 1;
        for(Map.Entry<Long, Long> entry: timeDistanceMap.entrySet()){
            long time = entry.getKey();
            long record = entry.getValue();
            long wins = 0;
            long raceTime = time;

            for (long i = 0; i <= time; i++) {
                if(i * raceTime-- > record){
                    wins++;
                }
            }
            winMult *= wins;
        }
        return winMult;
    }

}
