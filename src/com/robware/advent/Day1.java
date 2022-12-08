package com.robware.advent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Day1 {

    static class ElfCalorieCount implements Comparable<ElfCalorieCount> {
        private final int elfNum, calories;

        ElfCalorieCount(final int elfNum, final int calories) {
            this.elfNum = elfNum;
            this.calories = calories;
        }

        int getCalories() {
            return this.calories;
        }

        @Override
        public String toString() {
            return "ElfCalorieCount{" +
                    "elfNum=" + elfNum +
                    ", calories=" + calories +
                    '}';
        }

        @Override
        public int compareTo(ElfCalorieCount e) {
            return e.calories - this.calories;
        }
    }

    public static void main(String[] args) {
        try(Scanner s = new Scanner(System.in)) {
            Set<ElfCalorieCount> elfSet = new TreeSet<>();

            int currentElf = 0;
            int currentCount = 0;
            int maxCount = -1;
            int maxElf = -1;
            System.out.println("Enter calories");
            while(s.hasNextLine()) {
                final String line = s.nextLine();

                if(line.isBlank()) {
                    elfSet.add(new ElfCalorieCount(currentElf, currentCount));
                    if(maxCount < currentCount) {
                        maxCount = currentCount;
                        maxElf = currentElf;
                    }

                    currentElf++;
                    currentCount = 0;
                    continue;
                }

                final int currentCalories = Integer.parseInt(line);
                currentCount += currentCalories;
            }
            elfSet.add(new ElfCalorieCount(currentElf, currentCount));
            if(maxCount < currentCount) {
                maxCount = currentCount;
                maxElf = currentElf;
            }

            System.out.println("Most calories is elf " + maxElf + " with " + maxCount + " " +
                    "calories");
            final var it = elfSet.iterator();
            System.out.println("First elf is " + it.next());
            System.out.println("Second elf is " + it.next());
            System.out.println("Third elf is " + it.next());
        }
    }

}
