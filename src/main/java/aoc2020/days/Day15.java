package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15
{
    private final List<Integer> startNumbers = new ArrayList<>();

    public Day15() throws URISyntaxException, IOException
    {
        super();

        Path path = Paths.get(getClass().getClassLoader().getResource("day15.txt").toURI());

        var lines = Files.lines(path);
        lines.forEach(l -> {
            for (var num : l.split(",")) {
                startNumbers.add(Integer.valueOf(num));
            }
        });
        lines.close();
    }

    public long getNumberAtRound(int maxRounds)
    {
        final Map<Long, NumberInfo> numbersAndTurns = new HashMap<>();

        long lastNumber = 0L;
        int curRound = 0;
        for (var n : startNumbers) {
            NumberInfo numberInfo = new NumberInfo();
            numberInfo.pushPreviousTurn(curRound);
            numbersAndTurns.put(n.longValue(), numberInfo);

            lastNumber = n;
            curRound++;
        }

        for (; curRound < maxRounds; curRound++) {
            lastNumber = getAndStoreNumberForRound(curRound, lastNumber, numbersAndTurns);
        }

        return lastNumber;
    }

    private static long getAndStoreNumberForRound(int round, long lastNumber, Map<Long, NumberInfo> numbersAndTurns)
    {
        long newNumber;

        NumberInfo nInfo = numbersAndTurns.get(lastNumber);
        if (nInfo.getPreviousPreviousTurn() != -1) {
            newNumber = nInfo.getPreviousTurn() - (long) nInfo.getPreviousPreviousTurn();
        } else {
            newNumber = 0L;
        }

        if (numbersAndTurns.containsKey(newNumber)) {
            numbersAndTurns.get(newNumber).pushPreviousTurn(round);
        } else {
            NumberInfo numberInfo = new NumberInfo();
            numberInfo.pushPreviousTurn(round);
            numbersAndTurns.put(newNumber, numberInfo);
        }

        return newNumber;
    }

    private static class NumberInfo
    {
        private int previousTurn;
        private int previousPreviousTurn;

        public NumberInfo()
        {
            super();
            previousTurn = -1;
            previousPreviousTurn = -1;
        }

        public int getPreviousTurn()
        {
            return previousTurn;
        }

        public int getPreviousPreviousTurn()
        {
            return previousPreviousTurn;
        }

        public void pushPreviousTurn(int previousTurn)
        {
            this.previousPreviousTurn = this.previousTurn;
            this.previousTurn = previousTurn;
        }

        @Override
        public String toString()
        {
            return "NumberInfo [previousTurn=" + previousTurn + ", previousPreviousTurn=" + previousPreviousTurn + "]";
        }
    }
}
