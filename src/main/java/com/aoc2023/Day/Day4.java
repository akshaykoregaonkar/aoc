package com.aoc2023.Day;
import java.util.*;

public class Day4 extends Day{
    int[] cardCount;
    Day4(String filePath) {
        super(filePath);
        this.cardCount = new int[187];
        Arrays.fill(cardCount, 1);
    }

    @Override
    protected long daySpecificCalculation(String line) {
        String[] cardMetadata = line.split(":");
        int cardNumber = Integer.parseInt(cardMetadata[0].replace("Card ", "").trim());

        String numbers = line.split(":")[1];
        String[] numberSplit = numbers.split("\\|");
        Set<Integer> winningNumbers = new HashSet<>(convertStringToList(numberSplit[0]));
        List<Integer> scratchCardNumbers = convertStringToList(numberSplit[1]);
        long points = scratchCardNumbers.stream().filter(n -> winningNumbers.contains(n)).count();

        for (int i = 0; i < cardCount[cardNumber-1]; i++) {
            for (int j = cardNumber; j <= cardNumber + points -1; j++) {
                cardCount[j]++;
            }
        }

        return cardCount[cardNumber-1];
    }

    private List<Integer> convertStringToList(String inputString){
        String[] stringArray = inputString.split(" ");
        Integer[] integerArray = Arrays.stream(stringArray)
                .filter(num -> !num.isEmpty())
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
        return Arrays.asList(integerArray);
    }
}
