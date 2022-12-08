package com.robware.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day6 {

    public static void main(String[] args) {

        try (Scanner s = new Scanner(System.in)) {

            final int bufferLength = 14;

            final var line = s.nextLine();
            final var buffer = new char[bufferLength];
            for(int i = 0; i < bufferLength-1; i++) {
                buffer[i] = line.charAt(i);
            }

            for(int i = 3; i < line.length(); i++) {
                int bufferCursor = i % bufferLength;
                buffer[bufferCursor] = line.charAt(i);

                if(isUnique(buffer)) {
                    System.out.println("Found at position " + (i+1));
                    printBuffer(buffer);
                    return;
                }
            }
        }
    }

    static boolean isUnique(char[] buffer) {
        Set<Character> set = new HashSet<>(buffer.length);

        for(int i = 0; i < buffer.length; i++) {
            if(!set.add(buffer[i])) {
                return false;
            }
        }

        return true;
    }

    static void printBuffer(char[] buffer) {
        if(buffer.length == 0) {
            System.out.println("[]");
            return;
        }

        System.out.print("['" + buffer[0]);
        for(int i = 1; i < buffer.length; i++) {
            System.out.print(", '" + buffer[i] + "'");

        }

        System.out.println("]");
    }
}
