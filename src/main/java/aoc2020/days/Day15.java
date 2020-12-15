package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        int[] lastSeenInTurn = new int[maxRounds];

        int curRound = 1;
        for (var n : startNumbers) {
            lastSeenInTurn[n] = curRound;
            curRound++;
        }

        int lastNumber = startNumbers.get(startNumbers.size() - 1);

        for (; curRound <= maxRounds; curRound++) {
            int newNumber;
            if (lastSeenInTurn[lastNumber] != 0) {
                newNumber = (curRound - 1) - lastSeenInTurn[lastNumber];
            } else {
                newNumber = 0;
            }

            lastSeenInTurn[lastNumber] = curRound - 1;
            lastNumber = newNumber;
        }

        return lastNumber;
    }
}
