package com.robware.advent;

import java.util.Scanner;

public class Day2 {

    enum MatchResult {
        WIN("Z", 6),
        LOSS("X", 0),
        DRAW("Y", 3);

        private final String code;
        final int score;

        MatchResult(String code, int score) {
            this.code = code;
            this.score = score;
        }

        int score() {
            return this.score;
        }

        static MatchResult fromCode(String code) {
            for (MatchResult result : values()) {
                if(result.code.equals(code)) {
                    return result;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    enum Choice {
        ROCK("A", 1),
        PAPER("B", 2),
        SCISSORS("C", 3);

        private final String code;
        private final int choiceScore;

        Choice(String code, int choiceScore) {
            this.code = code;
            this.choiceScore = choiceScore;
        }

        boolean beats(Choice choice) {
            return choice == getBeatsChoice();
        }

        Choice getBeatsChoice() {
            return switch (this) {
                case ROCK -> Choice.SCISSORS;
                case PAPER -> Choice.ROCK;
                case SCISSORS -> Choice.PAPER;
            };
        }

        int score() {
            return this.choiceScore;
        }

        static Choice fromCode(String code) {
            for (Choice choice : values()) {
                if(choice.code.equals(code)) {
                    return choice;
                }
            }
            throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    public static void main(String[] args) {
        try(Scanner s = new Scanner(System.in)) {
            int score = 0;
            while(s.hasNextLine()) {
                final var lineSplit = s.nextLine().split(" ");
                final var opponentChoice = Choice.fromCode(lineSplit[0]);
                final var matchResult = MatchResult.fromCode(lineSplit[1]);

                score += getMatchScore(opponentChoice, matchResult);
            }

            System.out.println("Total score: " + score);
        }
    }

    private static int getMatchScore(Choice opponentChoice, MatchResult matchResult) {

        int outcomeScore = matchResult.score();

        Choice yourChoice = switch (matchResult) {
            case DRAW -> opponentChoice;
            case LOSS -> opponentChoice.getBeatsChoice();
            case WIN -> opponentChoice.getBeatsChoice().getBeatsChoice();
        };

        return outcomeScore + yourChoice.score();
    }

}
