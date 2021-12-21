package com.lfl.advent2021.days.day21;

import com.lfl.advent2021.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Day21 implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        part1();

        WinCounter winCounter = countWin(1, 3, 0, 0);
        System.out.println("winCounter = " + winCounter);
    }

    private final Map<Input, WinCounter> memo = Maps.mutable.empty();

    public WinCounter countWin(int player1, int player2, int score1, int score2) {
        if (score1 >= 21) {
            return new WinCounter(1, 0);
        }

        if (score2 >= 21) {
            return new WinCounter(0, 1);
        }

        if (memo.containsKey(new Input(player1, player2, score1, score2))) {
            return memo.get(new Input(player1, player2, score1, score2));
        }

        WinCounter result = new WinCounter(0, 0);

        //compute all possibilities for first player
        for (int firstDice = 1; firstDice <= 3; firstDice++) {
            for (int secondDice = 1; secondDice <= 3; secondDice++) {
                for (int thirdDice = 1; thirdDice <= 3; thirdDice++) {
                    int newPlayer1 = (player1 + firstDice + secondDice + thirdDice) % 10;
                    int newScore1 = score1 + (newPlayer1 == 0 ? 10 : newPlayer1);

                    WinCounter winCounter = countWin(player2, newPlayer1, score2, newScore1);//we revert the players
                    result = new WinCounter(result.win1() + winCounter.win2(), result.win2() + winCounter.win1());
                }
            }
        }

        memo.put(new Input(player1, player2, score1, score2), result);
        return result;
    }

    private record WinCounter(long win1, long win2) {
    }

    private record Input(int player1, int player2, int score1, int score2) {
    }

    private void part1() {
        int player1 = 1;
        int player2 = 3;

        int score1 = 0;
        int score2 = 0;

        Dice dice = new Dice();

        while (score2 < 1000) {
            int dice1 = dice.nextInt() + dice.nextInt() + dice.nextInt();
            player1 = (player1 + dice1) % 10;
            score1 += player1 == 0 ? 10 : player1;

            if (score1 >= 1000) break;

            int dice2 = dice.nextInt() + dice.nextInt() + dice.nextInt();
            player2 = (player2 + dice2) % 10;
            score2 += player2 == 0 ? 10 : player2;
        }

        int result = Math.min(score1, score2) * dice.getNbRolled();

        System.out.println("result = " + result);
    }

    private static class Dice {
        @Getter
        private int nbRolled;
        private int value = 1;

        public int nextInt() {
            nbRolled++;
            int result = value;
            value++;
            if (value == 101) {
                value = 1;
            }

            return result;
        }
    }
}
