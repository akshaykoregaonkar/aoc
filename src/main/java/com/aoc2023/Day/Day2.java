package com.aoc2023.Day;

public class Day2 extends Day{
//    private static final int RED_CUBES = 12;
//    private static final int GREEN_CUBES = 13;
//    private static final int BLUE_CUBES = 14;

    Day2(String filePath) {
        super(filePath);
    }

    @Override
    protected long daySpecificCalculation(String line) {
        String[] gameArray = line.split(":");
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        for(String hand : gameArray[1].split(";")){
            for(String cube : hand.split(",")){
                String[] cubeDetails = cube.trim().split(" ");
                int cubeCount = Integer.parseInt(cubeDetails[0]);
                String cubeColour = cubeDetails[1];
                if(cubeColour.equals("red") && cubeCount > maxRed) {
                    maxRed = cubeCount;
                }
                if(cubeColour.equals("blue") && cubeCount > maxBlue) {
                    maxBlue = cubeCount;
                }
                if(cubeColour.equals("green") && cubeCount > maxGreen) {
                    maxGreen = cubeCount;
                }
            }
        }
        return maxRed * maxBlue * maxGreen;
    }

// Part 1:
//    @Override
//    protected long getDaySpecificCalculation(String line) {
//        String[] gameArray = line.split(":");
//        int gameId = Integer.parseInt(gameArray[0].replace("Game ", ""));
//        for(String hand : gameArray[1].split(";")){
//            for(String cube : hand.split(",")){
//                String[] cubeDetails = cube.trim().split(" ");
//                int cubeCount = Integer.parseInt(cubeDetails[0]);
//                String cubeColour = cubeDetails[1];
//                if( (cubeColour.equals("red") && cubeCount > RED_CUBES) ||
//                    (cubeColour.equals("green") && cubeCount > GREEN_CUBES) ||
//                    (cubeColour.equals("blue") && cubeCount > BLUE_CUBES)) {
//                    return 0;
//                }
//            }
//        }
//        return gameId;
//    }
}
