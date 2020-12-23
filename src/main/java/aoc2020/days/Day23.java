package aoc2020.days;

import java.util.ArrayList;
import java.util.List;

public class Day23
{
    //private static final String INPUT = "389125467"; // Example
    private static final String INPUT = "712643589"; // Real

    public Day23()
    {
        super();
    }

    public String part1()
    {
        Integer min = 1;
        Integer max = 9;

        final List<Integer> cups = new ArrayList<>();
        INPUT.chars().forEach(label -> cups.add(Integer.valueOf(String.valueOf((char) label))));

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= 100; i++) {
            currentCup = playRound(cups, currentCup, min, max);
        }

        var sb = new StringBuilder();
        int startIndex = cups.indexOf(Integer.valueOf(1));
        for (int i = startIndex + 1; i < cups.size() + startIndex; i++) {
            sb.append(cups.get(i % cups.size()));
        }

        return sb.toString();
    }

    public String part2()
    {
        final Integer min = 1;
        final Integer max = 1000000;

        final List<Integer> cups = new ArrayList<>(max);
        INPUT.chars().forEach(label -> cups.add(Integer.valueOf(String.valueOf((char) label))));
        for (int i = 10; i <= max; i++) {
            cups.add(Integer.valueOf(i));
        }

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= 10000000; i++) {
            currentCup = playRound(cups, currentCup, min, max);
        }

        int startIndex = cups.indexOf(Integer.valueOf(1));
        var i1 = cups.get((startIndex + 1) % cups.size());
        var i2 = cups.get((startIndex + 2) % cups.size());

        return Long.valueOf(i1.longValue() * i2.longValue()).toString();
    }

    private Integer playRound(final List<Integer> cups, final Integer currentCup, final Integer min, final Integer max)
    {
        int currentCupIndex = cups.indexOf(currentCup);
        final Integer nextActiveCup = cups.get((currentCupIndex + 4) % cups.size());

        final List<Integer> subCups = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            subCups.add(cups.get((currentCupIndex + i) % cups.size()));
        }

        cups.removeAll(subCups);

        int destinationCupIndex = getDestinationCup(cups, currentCup, subCups, min, max);
        cups.addAll(destinationCupIndex + 1, subCups);

        return nextActiveCup;
    }

    private int getDestinationCup(final List<Integer> cups, final Integer currentCup, final List<Integer> subCups,
            final Integer min, final Integer max)
    {
        int currentCupIndex = cups.indexOf(currentCup);

        Integer valueToFind = (currentCup - 1) == 0 ? max : currentCup - 1;
        while (subCups.contains(valueToFind)) {
            valueToFind--;
            if (valueToFind < min) {
                valueToFind = max;
            }
        }

        for (int i = currentCupIndex + 1; i < (cups.size() + currentCupIndex); i++) {
            Integer destCandidate = cups.get(i % cups.size());
            if (destCandidate.equals(valueToFind)) {
                return i % cups.size();
            }
        }

        return -1;
    }
}
