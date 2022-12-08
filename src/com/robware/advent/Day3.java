package com.robware.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Day3 {
    /*
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
     */

    public static void main(String[] args) {

        try(Scanner s = new Scanner(System.in)) {
            int prioSum = 0;
            while(s.hasNextLine()) {
                final var line1 = strToList(s.nextLine());
                final var line2 = strToList(s.nextLine());
                final var line3 = strToList(s.nextLine());

                line1.retainAll(line2);
                line1.retainAll(line3);

                final var commonChar = line1.get(0);

                prioSum += charToPrio(commonChar);
            }

            System.out.println("Sum = " + prioSum);
        }
    }

    static List<Character> strToList(String s) {
        return new ArrayList<>(s.chars().mapToObj(i -> (char) i).toList());
    }

    static int charToPrio(char c) {
        if(c < 'A' || c > 'z') {
            throw new IllegalStateException("Invalid char - " + c);
        }

        if(c >= 'a') {
            return c - 96;
        }

        return c - 38;
    }
}
