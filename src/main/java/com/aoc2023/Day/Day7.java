package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7{
    Map<String, Integer> orderedHands = new TreeMap<>(new PokerComparator());

    enum PokerHandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    Day7(String filePath) {
        processFile(filePath);
    }

    private void processFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                    String[] hand = line.split(" ");
                    orderedHands.put(hand[0], Integer.parseInt(hand[1]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int rank = orderedHands.size();
        long total = 0;
        for(Map.Entry<String,Integer> entry: orderedHands.entrySet()){
            total += entry.getValue() * rank--;
        }
        System.out.println("total: " + total);
    }

    static class PokerComparator implements Comparator<String> {
        @Override
        public int compare(String hand1, String hand2) {
            PokerHandType type1 = determineHandType(hand1);
            PokerHandType type2 = determineHandType(hand2);

            // Compare hand types first
            int typeComparison = type1.compareTo(type2);
            if (typeComparison != 0) {
                return typeComparison;
            }

            Map<Character, Integer> cardStrengths = new HashMap<>();
            {
                cardStrengths.put('J', 1); // Jack
                cardStrengths.put('2', 2);
                cardStrengths.put('3', 3);
                cardStrengths.put('4', 4);
                cardStrengths.put('5', 5);
                cardStrengths.put('6', 6);
                cardStrengths.put('7', 7);
                cardStrengths.put('8', 8);
                cardStrengths.put('9', 9);
                cardStrengths.put('T', 10); // 10
                cardStrengths.put('Q', 12); // Queen
                cardStrengths.put('K', 13); // King
                cardStrengths.put('A', 14); // Ace
            }

            for (int i = 0; i < 5; i++) {
                if(cardStrengths.get(hand1.charAt(i)) > cardStrengths.get(hand2.charAt(i))){
                    return -1;
                }else if(cardStrengths.get(hand1.charAt(i)) < cardStrengths.get(hand2.charAt(i))){
                    return 1;
                }
            }

            return hand1.compareTo(hand2);
        }
    }

    private static PokerHandType determineHandType(String hand) {

        Map<Character, Long> charCountMap = hand.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        // Create a TreeSet with a custom comparator for descending order by values
        TreeSet<Map.Entry<Character, Long>> sortedEntries = new TreeSet<>((e1, e2) -> {
            int result = e2.getValue().compareTo(e1.getValue());
            return result != 0 ? result : Character.compare(e1.getKey(), e2.getKey());
        });

        // Add all entries to the TreeSet
        sortedEntries.addAll(charCountMap.entrySet());

        Map.Entry<Character, Long> firstEntry = sortedEntries.pollFirst();
        Map.Entry<Character, Long> secondEntry = null;
        if(!sortedEntries.isEmpty()){
             secondEntry = sortedEntries.pollFirst();
        }
        long firstVal = firstEntry.getValue();

        if (charCountMap.containsKey('J')) {
            if (firstEntry.getKey() != 'J') {
                firstVal += charCountMap.get('J');
            } else {
                if (firstVal < 5) {
                    firstVal = secondEntry != null ? secondEntry.getValue() + charCountMap.get('J') : charCountMap.get('J');
                }
            }
        }

        if(firstVal == 5L) return PokerHandType.FIVE_OF_A_KIND;
        if(firstVal == 4L) return PokerHandType.FOUR_OF_A_KIND;

        if(firstVal == 3L){
            if(secondEntry.getValue() == 2L){
                return PokerHandType.FULL_HOUSE;
            }else{
                return PokerHandType.THREE_OF_A_KIND;
            }
        }
        if(firstVal == 2L){
            if(secondEntry.getValue() == 2L){
                return PokerHandType.TWO_PAIR;
            }else{
                return PokerHandType.ONE_PAIR;
            }
        }

        return PokerHandType.HIGH_CARD;
    }
}
