package com.lfl.advent2021.days.day4;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class BingoScorer implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableIntList drawnNumbers = Arrays.stream(lines.get(0).split(","))
                                            .mapToInt(Integer::parseInt)
                                            .collect(IntLists.mutable::empty,
                                                     MutableIntList::add,
                                                     MutableIntList::addAll);

        MutableList<BingoBoard> bingoBoards = buildBingoBoards(lines);

        //part 1
        BingoBoard winner = null;
        int turn = 0;
        while (winner == null) {
            int finalTurn = turn;
            bingoBoards.forEach(bingoBoard -> bingoBoard.drawNumber(drawnNumbers.get(finalTurn)));
            winner = bingoBoards.detect(BingoBoard::hasWon);
            turn++;
        }

        log.info("Number drawn = {}", drawnNumbers.collect(i -> i).subList(0, turn));
        log.info("Winner =\n{}", winner);
        log.info("Score = {}", winner.score());

        //part 2
        BingoBoard lastWinner = null;
        MutableSet<BingoBoard> winners = Sets.mutable.of(winner);
        while (lastWinner == null) {
            int finalTurn = turn;
            bingoBoards.reject(winners::contains)
                       .forEach(bingoBoard -> bingoBoard.drawNumber(drawnNumbers.get(finalTurn)));
            MutableList<BingoBoard> currentWinners = bingoBoards.reject(winners::contains)
                                                                .select(BingoBoard::hasWon);

            if (currentWinners.size() == bingoBoards.reject(winners::contains).size()) {
                lastWinner = currentWinners.get(0);
            }
            winners.addAll(currentWinners);
            turn++;
        }

        log.info("Number drawn = {}", drawnNumbers.collect(i -> i).subList(0, turn));
        log.info("Last winner =\n{}", lastWinner);
        log.info("Score = {}", lastWinner.score());
    }

    private MutableList<BingoBoard> buildBingoBoards(List<String> lines) {
        MutableList<String> accumulator = Lists.mutable.empty();
        MutableList<BingoBoard> bingoBoards = Lists.mutable.empty();
        lines.stream()
             .skip(2)
             .forEach(line -> {
                 if (line.isEmpty()) {
                     bingoBoards.add(BingoBoard.of(accumulator));
                     accumulator.clear();
                 } else {
                     accumulator.add(line);
                 }
             });
        bingoBoards.add(BingoBoard.of(accumulator));
        return bingoBoards;
    }

    public static class BingoBoard {
        private final MutableObjectIntMap<Point> board;
        private final MutableIntSet drawnNumbers = IntSets.mutable.empty();
        private int lastNumber;

        public BingoBoard(MutableObjectIntMap<Point> board) {
            this.board = board;
        }

        public static BingoBoard of(List<String> lines) {
            MutableObjectIntMap<Point> board = ObjectIntMaps.mutable.empty();
            IntStream.range(0, lines.size())
                     .forEach(y -> {
                         MutableIntList rowNumber = Arrays.stream(lines.get(y)
                                                                       .replaceAll(" {2}", " ")
                                                                       .replaceAll("^ ", "")
                                                                       .split(" "))
                                                          .mapToInt(Integer::parseInt)
                                                          .collect(IntLists.mutable::empty,
                                                                   MutableIntList::add,
                                                                   MutableIntList::addAll);
                         IntStream.range(0, rowNumber.size())
                                  .forEach(x -> board.put(Point.of(x, y), rowNumber.get(x)));
                     });
            return new BingoBoard(board);
        }

        public void drawNumber(int number) {
            drawnNumbers.add(number);
            lastNumber = number;
        }

        public boolean hasWon() {
            return IntStream.range(0, 5)
                            .anyMatch(y -> IntStream.range(0, 5)
                                                    .allMatch(x -> drawnNumbers.contains(board.get(Point.of(y, x))))
                                           ||
                                           IntStream.range(0, 5)
                                                    .allMatch(x -> drawnNumbers.contains(board.get(Point.of(x, y))))
                                     );
        }

        public long score() {
            long sum = board.values()
                            .reject(drawnNumbers::contains)
                            .sum();

            return sum * lastNumber;
        }

        public String toString() {
            return IntStream.range(0, 5)
                            .mapToObj(y -> IntStream.range(0, 5)
                                                    .map(x -> board.get(Point.of(x, y)))
                                                    .mapToObj(number -> ((drawnNumbers.contains(number)) ? "*" : " ") + String.format("%2d", number))
                                                    .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining("\n"));
        }
    }
}
