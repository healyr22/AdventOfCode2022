package com.robware.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day5 {
    public static void main(String[] args) {

        try (Scanner s = new Scanner(System.in)) {

            List<List<Character>> stacks = new ArrayList<>();
            while (s.hasNextLine()) {
                final var line = s.nextLine();
                if(line.isBlank()) {
                    break;
                }
                int cursor = 0;
                int currentStackIndex = 0;

                // Parse current line
                while(cursor < line.length()) {
                    // Check if end of stacks
                    if(line.charAt(cursor+1) == '1') {
                        break;
                    }

                    // Init stack
                    if(stacks.size() <= currentStackIndex) {
                        stacks.add(new ArrayList<>());
                    }

                    // Check if crate exists at this position
                    if(line.charAt(cursor) == '[') {
                        // Read crate
                        final char currentCrate = line.charAt(cursor+1);
                        stacks.get(currentStackIndex).add(0, currentCrate);
                    }

                    // Move to next stack
                    cursor += 4;
                    currentStackIndex++;
                }
            }
//            System.out.println(stacks);


            // Now read instructions
            while (s.hasNextLine()) {
                final var lineSplit = s.nextLine().split(" ");
                final var moveCount = Integer.parseInt(lineSplit[1]);
                final var fromStack = Integer.parseInt(lineSplit[3]) - 1;
                final var toStack = Integer.parseInt(lineSplit[5]) - 1;

                final var cratesToMove = new ArrayList<Character>();
                final var stack = stacks.get(fromStack);
                for(int i = moveCount; i > 0; i--) {
                    final var c = stack.remove(stack.size()-1);
                    cratesToMove.add(0, c);
                }
                stacks.get(toStack).addAll(cratesToMove);
            }
//            System.out.println("DONE");
//            System.out.println(stacks);

            System.out.println("Answer:");
            for (List<Character> stack : stacks) {
                System.out.print(stack.get(stack.size()-1));
            }
        }
    }
}
