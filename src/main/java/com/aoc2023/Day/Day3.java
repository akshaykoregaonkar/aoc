package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day3 {

    char[][] matrix;
    boolean[][] visited;
    private static final int MATRIX_DIM = 140;
    Day3(String filePath){
        this.matrix = convertFileToMatrix(filePath);
        this.visited = new boolean[MATRIX_DIM][MATRIX_DIM];
    }

    private char[][] convertFileToMatrix(String filePath) {
        char[][] matrix = new char[MATRIX_DIM][MATRIX_DIM];
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            int i = 0;
            while((line = reader.readLine()) != null){
                matrix[i++] = line.toCharArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return matrix;
    }

    long getTotalFromFile(){
        long total = 0;
        for (int i = 0; i < MATRIX_DIM; i++) {
            for (int j = 0; j < MATRIX_DIM; j++) {
                if(!visited[i][j] && Character.isDigit(matrix[i][j])){
                    int digitStart = j;
                    StringBuilder numberString = new StringBuilder();
                    while(j < MATRIX_DIM && Character.isDigit(matrix[i][j])){
                        numberString.append(matrix[i][j]);
                        j++;
                    }
                    int number = Integer.parseInt(numberString.toString());
                    total += getGearRatio(number, i, digitStart - 1, j);
                }
            }
        }
        return total;
    }

    private int getGearRatio(int number, int rowIndex, int colBoundaryStart, int colBoundaryEnd) {
        colBoundaryStart = Math.max(0, colBoundaryStart);
        colBoundaryEnd = Math.min(MATRIX_DIM-1, colBoundaryEnd);
        int [] starIndex = new int[2];
        boolean foundStar = false;

        if(isStar(rowIndex, colBoundaryStart)){
           starIndex[0] = rowIndex;
           starIndex[1] = colBoundaryStart;
           foundStar = true;
        }
        if (isStar(rowIndex, colBoundaryEnd)){
            starIndex[0] = rowIndex;
            starIndex[1] = colBoundaryEnd;
            foundStar = true;
        }

        for (int i = colBoundaryStart; i <= colBoundaryEnd; i++) {
            if(rowIndex - 1 >= 0 && isStar(rowIndex - 1, i)){
                starIndex[0] = rowIndex - 1;
                starIndex[1] = i;
                foundStar = true;
            }
            if(rowIndex + 1 < MATRIX_DIM && isStar(rowIndex + 1, i)){
                starIndex[0] = rowIndex + 1;
                starIndex[1] = i;
                foundStar = true;
            }
        }
        if(foundStar){
            for (int i = colBoundaryStart + 1; i < colBoundaryEnd; i++) {
                visited[rowIndex][i] = true;
            }
            int numberAdjacentToStar = getNumberAdjacentToStar(starIndex);
            if(numberAdjacentToStar > 0){
                return numberAdjacentToStar * number;
            }
        }

        return 0;
    }

    private int getNumberAdjacentToStar(int[] starIndex) {
        int starRow = starIndex[0];
        int starCol = starIndex[1];

        int checkFromRow = Math.max(0, starRow - 1);
        int checkToRow = Math.min(MATRIX_DIM - 1, starRow + 1);
        int checkFromCol = Math.max(0, starCol - 1);
        int checkToCol = Math.min(MATRIX_DIM - 1, starCol + 1);

        for (int i = checkFromRow; i <= checkToRow; i++) {
            for (int j = checkFromCol; j <= checkToCol ; j++) {
                if(!visited[i][j] && Character.isDigit(matrix[i][j])){
                    return getFullNumber(i, j);
                }
            }
        }
        return 0;
    }

    private int getFullNumber(int i, int j) {
        int colStart = j;
        int colEnd = j;
        while(Character.isDigit(matrix[i][colStart])){
            colStart--;
        }
        while(Character.isDigit(matrix[i][colEnd])){
            colEnd++;
        }
        StringBuilder numberString = new StringBuilder();
        for (int k = colStart + 1; k < colEnd; k++) {
            numberString.append(matrix[i][k]);
        }
        return Integer.parseInt(numberString.toString());
    }

    private boolean isStar(int row, int col){
        return matrix[row][col] == '*';
    }

    /*
    Part 1:
    private boolean isAdjacentToSymbol(int rowIndex, int colBoundaryStart, int colBoundaryEnd) {
        colBoundaryStart = Math.max(0, colBoundaryStart);
        colBoundaryEnd = Math.min(MATRIX_DIM-1, colBoundaryEnd);


        if( isValid(rowIndex, colBoundaryStart) || isValid(rowIndex, colBoundaryEnd)){
            return true;
        }

        for (int i = colBoundaryStart; i <= colBoundaryEnd; i++) {
            if((rowIndex - 1 >= 0 && isValid(rowIndex - 1, i)) ||
                    (rowIndex + 1 < MATRIX_DIM && isValid(rowIndex + 1, i))){
                return true;
            }
        }

        return false;
    }

    private boolean isValid(int row, int col){
        return matrix[row][col] != '.' && !Character.isLetterOrDigit(matrix[row][col]);
    }

     */
}
