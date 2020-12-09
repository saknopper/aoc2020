package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.paukov.combinatorics3.Generator;

public class Day1
{
    public int determineSumInExpenseReportAndGetProduct(int sumTarget, int numbersToPick)
            throws URISyntaxException, IOException
    {
        final List<Integer> expenses = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("day1.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> expenses.add(Integer.parseInt(line)));
        lines.close();

        List<List<Integer>> validForTargetSum = Generator.combination(expenses).simple(numbersToPick).stream()
                .filter(items -> items.stream().collect(Collectors.summingInt(Integer::intValue)) == sumTarget)
                .collect(Collectors.toList());

        if (!validForTargetSum.isEmpty()) {
            List<Integer> solution = validForTargetSum.get(0);

            Integer multiplied = solution.stream().reduce(1, (a, b) -> a * b);

            return multiplied;
        }

        return 0;
    }
}
