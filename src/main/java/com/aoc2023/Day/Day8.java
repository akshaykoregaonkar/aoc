package com.aoc2023.Day;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {
    Map<String, String[]> network = new HashMap<>();

    char[] instructions;

    Day8(String filePath){
        processFile(filePath);
    }

    private void processFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                    if(!line.contains("=")){
                        instructions = line.toCharArray();
                    }else{
                        processLine(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    BigInteger getStepCount(){
        List<String[]> nodes = new ArrayList<>();
        List<String> aaas = network.keySet().stream()
                .filter(k -> k.charAt(2) == 'A')
                .collect(Collectors.toList());

        for (String node: aaas) {
            nodes.add(network.get(node));
        }

        List<BigInteger> zcounts = new ArrayList<>();

        for(int j = 0; j < nodes.size(); j++) {
            long count = 0;
            String[] node = nodes.get(j);
            for (int i = 0; i < instructions.length; i++) {
                char instruction = instructions[i];
                List<String> nextNodes = new ArrayList<>();

                String nextNode = instruction == 'L' ? node[0] : node[1];
                node = network.get(nextNode);
                count++;
                if (nextNode.endsWith("Z")) {
                    break;
                }
                if (i == instructions.length - 1) {
                    i = -1;
                }
            }
            zcounts.add(BigInteger.valueOf(count));
        }
        return findLCM(zcounts);
    }

    private BigInteger findLCM(List<BigInteger> zcounts) {
        BigInteger lcm = zcounts.get(0);
        for (int i = 1; i < zcounts.size(); i++) {
            lcm =  calcLCM(lcm, zcounts.get(i));
        }
        return lcm;
    }

    private BigInteger calcLCM(BigInteger a, BigInteger b) {
        return (a.multiply(b)).divide(a.gcd(b));
    }

    // [24253, 42980, 61093, 82890, 105301, 121572]
    private void processLine(String line) {
        Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            String key = matcher.group(1);
            String[] values = matcher.group(2).split(",\\s*");

            network.put(key, values);
        }
    }
}
