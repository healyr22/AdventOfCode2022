package com.robware.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day4 {

    public static void main(String[] args) {

        try (Scanner s = new Scanner(System.in)) {
            int count = 0;
            while (s.hasNextLine()) {
                final var elfSplit = s.nextLine().split(",");
                final var elf1 = toSectionList(elfSplit[0].split("-"));
                final var elf2 = toSectionList(elfSplit[1].split("-"));

                elf1.retainAll(elf2);

                if(!elf1.isEmpty()) {
                    count++;
                }
            }
            System.out.println("Count: " + count);
        }
    }

    static List<Integer> toSectionList(String[] s) {
        if(s.length != 2) {
            throw new IllegalStateException("s length should be 2, but was " + s);
        }

        final int start = Integer.parseInt(s[0]);
        final int end = Integer.parseInt(s[1]);
        final var list = new ArrayList<Integer>();

        for(int i = start; i <= end; i++) {
            list.add(i);
        }
        return list;
    }

}
