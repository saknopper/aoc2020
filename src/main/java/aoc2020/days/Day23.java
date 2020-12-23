package aoc2020.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23
{
    private static final Integer ZERO = Integer.valueOf(0);

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

        final Map<Integer, Integer> nextCupCache = new HashMap<>();

        final List<Integer> cups = new ArrayList<>();
        INPUT.chars().forEach(label -> cups.add(Integer.valueOf(String.valueOf((char) label))));

        for (int i = 0; i < cups.size(); i++) {
            if (i == cups.size() - 1) {
                nextCupCache.put(cups.get(i), cups.get(0));
            } else {
                nextCupCache.put(cups.get(i), cups.get(i+1));
            }
        }

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= 100; i++) {
            currentCup = playRound(cups, nextCupCache, currentCup, min, max);
        }
        System.out.println(nextCupCache);
        var sb = new StringBuilder();
        Integer current = nextCupCache.get(1);
        for (int i = 1; i < max; i++) {
            sb.append(current);
            Integer next = nextCupCache.get(current);
            current = next;
        }

        return sb.toString();
    }

    public String part2()
    {
        final Integer min = 1;
        final Integer max = 1000000;
        final int rounds = 10000000;

        final Map<Integer, Integer> nextCupCache = new HashMap<>();

        final List<Integer> cups = new ArrayList<>(max);
        INPUT.chars().forEach(label -> cups.add(Integer.valueOf(String.valueOf((char) label))));
        for (int i = 10; i <= max; i++) {
            cups.add(Integer.valueOf(i));
        }

        for (int i = 0; i < cups.size(); i++) {
            if (i == cups.size() - 1) {
                nextCupCache.put(cups.get(i), cups.get(0));
            } else {
                nextCupCache.put(cups.get(i), cups.get(i+1));
            }
        }

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= rounds; i++) {
            currentCup = playRound(cups, nextCupCache, currentCup, min, max);
        }

        var i1 = nextCupCache.get(1);
        var i2 = nextCupCache.get(i1);

        System.out.println(i1 + " and " + i2);

        return Long.toString(i1.longValue() * i2.longValue());
    }

    private Integer playRound(final List<Integer> cups, final Map<Integer, Integer> cache, final Integer currentCup, final Integer min, final Integer max)
    {
        final List<Integer> subCups = new ArrayList<>();
        Integer c1 = cache.get(currentCup);
        Integer c2 = cache.get(c1);
        Integer c3 = cache.get(c2);
        subCups.add(c1);
        subCups.add(c2);
        subCups.add(c3);

        Integer destination = getDestinationCup(currentCup, subCups, min, max);

        cache.put(currentCup, cache.get(c3));
        cache.put(c3, cache.get(destination));
        cache.put(destination, c1);

        return cache.get(currentCup);
    }

    private Integer getDestinationCup(final Integer currentCup, final List<Integer> subCups, final Integer min,
            final Integer max)
    {
        Integer currentMinusOne = Integer.valueOf(currentCup.intValue() - 1);
        Integer valueToFind = currentMinusOne == ZERO ? max : currentMinusOne;
        while (subCups.contains(valueToFind)) {
            valueToFind = Integer.valueOf(valueToFind.intValue() - 1);
            if (valueToFind.compareTo(min) == -1) {
                valueToFind = max;
            }
        }

        return valueToFind;
    }
}
