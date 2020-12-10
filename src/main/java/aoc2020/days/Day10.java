package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Day10
{
    private final List<Integer> adapters = new ArrayList<>();

    public Day10() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day10.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> adapters.add(Integer.parseInt(line)));
        lines.close();

        Collections.sort(adapters);

        // Add outlet
        adapters.add(0, 0);

        // Add built-in adapter
        Integer highest = adapters.get(adapters.size() - 1);
        adapters.add(highest + 3);
    }

    public long getJoltDifferences()
    {
        int amountOf1JoltDifferences = 0;
        int amountOf3JoltDifferences = 0;

        for (int i = 0; i < adapters.size() - 1; i++) {
            int diff = adapters.get(i + 1) - adapters.get(i);
            if (diff == 1) {
                amountOf1JoltDifferences++;
            } else if (diff == 3) {
                amountOf3JoltDifferences++;
            }
        }

        return (long) amountOf1JoltDifferences * amountOf3JoltDifferences;
    }

    public long getValidCombinations()
    {
        long[] combinationsAtIndex = new long[adapters.size()];
        Arrays.fill(combinationsAtIndex, -1);

        return countValidCombinationsFromIndex(adapters, 0, combinationsAtIndex);
    }

    private static long countValidCombinationsFromIndex(List<Integer> input, int index, long[] combinationsAtIndex)
    {
        if (index == input.size() - 1) {
            return 1;
        }

        if (combinationsAtIndex[index] != -1) {
            return combinationsAtIndex[index];
        }

        long count = 0;
        int upperLimit = Math.min(index + 4, input.size());
        for (int i = index + 1; i < upperLimit; i++) {
            if (input.get(index) + 3 >= input.get(i)) {
                count += countValidCombinationsFromIndex(input, i, combinationsAtIndex);
            }
        }

        combinationsAtIndex[index] = count;

        return count;
    }
}
