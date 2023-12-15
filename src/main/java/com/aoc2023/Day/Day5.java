package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Day5 {
    private Map<Long, Long> seedMap;
    private Map<String, TreeMap<Long, Long>> almanacMap;

    Day5(String filePath){
        seedMap = new HashMap<>();
        almanacMap = new HashMap<>();
        processFile(filePath);
    }

    private void processFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentStage = null;

            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                    switch (line.split(":")[0]) {
                        case "seeds":
                            processSeeds(line);
                            break;
                        case "seed-to-soil map":
                            currentStage = "seedToSoil";
                            break;
                        case "soil-to-fertilizer map":
                            currentStage = "soilToFertilizer";
                            break;
                        case "fertilizer-to-water map":
                            currentStage = "fertilizerToWater";
                            break;
                        case "water-to-light map":
                            currentStage = "waterToLight";
                            break;
                        case "light-to-temperature map":
                            currentStage = "lightToTemp";
                            break;
                        case "temperature-to-humidity map":
                            currentStage = "tempToHumidity";
                            break;
                        case "humidity-to-location map":
                            currentStage = "humidityToLocation";
                            break;
                        default:
                            processLine(line, currentStage);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processLine(String line, String currentStage) {
        almanacMap.computeIfAbsent(currentStage, k -> new TreeMap<>());
        String[] mappings = line.split(" ");
        long source = Long.parseLong(mappings[1]);
        long destination = Long.parseLong(mappings[0]);
        long range = Long.parseLong(mappings[2]) - 1;
        almanacMap.get(currentStage).put(source, destination);
        almanacMap.get(currentStage).put(source + range, destination + range);
    }

    private void processSeeds(String line) {
        String seedString = line.split(":")[1].trim();
        boolean isSeed = true;
        long prevSeed = 0;
        for(String seed: seedString.split(" ")){
            long seedVal = Long.parseLong(seed);
            if(isSeed){
                seedMap.put(seedVal, 0l);
                prevSeed = seedVal;
                isSeed = false;
            }else{
                seedMap.put(prevSeed, prevSeed + seedVal);
                isSeed = true;
            }
        }
    }

//    long getSmallestLocation(){
//        long smallestLocation = Long.MAX_VALUE;
//        for(Map.Entry<Long, Long> entry: seedMap.entrySet()){
//            long from = entry.getKey();
//            long to = entry.getValue();
//            for(long seed = from; seed < to; seed++){
//                long location = calculateLocation(seed)
//                        .then("seedToSoil")
//                        .then("soilToFertilizer")
//                        .then("fertilizerToWater")
//                        .then("waterToLight")
//                        .then("lightToTemp")
//                        .then("tempToHumidity")
//                        .then("humidityToLocation")
//                        .getValue();
//
//                if(smallestLocation > location){
//                    smallestLocation = location;
//                    System.out.println(smallestLocation);
//                }
//            }
//        }
//        return smallestLocation;
//    }

    long getSmallestLocationConcurrently() throws ExecutionException, InterruptedException {
        AtomicLong smallestLocation = new AtomicLong(Long.MAX_VALUE);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : seedMap.entrySet()) {
            long from = entry.getKey();
            long to = entry.getValue();

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (long seed = from; seed < to; seed++) {
                    long location = calculateLocation(seed)
                            .then("seedToSoil")
                            .then("soilToFertilizer")
                            .then("fertilizerToWater")
                            .then("waterToLight")
                            .then("lightToTemp")
                            .then("tempToHumidity")
                            .then("humidityToLocation")
                            .getValue();

                    synchronized (this) {
                        if(smallestLocation.get() > location){
                            smallestLocation.set(location);
                            System.out.println(smallestLocation);
                        }
                    }
                }
            }, executor);

            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get(); // Wait for all tasks to complete

        executor.shutdown(); // Shut down the executor service

        return smallestLocation.get();
    }

    private LocationBuilder calculateLocation(long value) {
        return new LocationBuilder(value);
    }

    private class LocationBuilder {
        private long value;

        public LocationBuilder(long value) {
            this.value = value;
        }

        private LocationBuilder update(long currentValue, String nextStage) {
            TreeMap<Long, Long> nextStageMap = almanacMap.get(nextStage);
            if (nextStageMap == null) {
                return this;
            }

            if (nextStageMap.higherKey(currentValue) != null && nextStageMap.lowerKey(currentValue) != null) {
                long nearestKey = nextStageMap.floorKey(currentValue);
                long nearestValue = nextStageMap.get(nearestKey);
                value = nearestValue + (currentValue - nearestKey);
            }
            return this;
        }

        LocationBuilder then(String nextStage) {
            return update(value, nextStage);
        }

        long getValue(){
            return this.value;
        }
    }
}