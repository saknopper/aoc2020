package aoc2020.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day23
{
    // private static final String INPUT = "389125467"; // Example
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

        final Map<Integer, Integer> nextCupCache = new HashMap<>(cups.size());
        for (int i = 0; i < cups.size() - 1; i++) {
            nextCupCache.put(cups.get(i), cups.get(i + 1));
        }
        nextCupCache.put(cups.get(cups.size() - 1), cups.get(0));

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= 100; i++) {
            currentCup = playRound(nextCupCache, currentCup, min, max);
        }

        var sb = new StringBuilder();
        Integer current = nextCupCache.get(1);
        for (int i = 1; i < max; i++) {
            sb.append(current);
            current = nextCupCache.get(current);
        }

        return sb.toString();
    }

    public String part2()
    {
        final Integer min = 1;
        final Integer max = 1000000;
        final int rounds = 10000000;

        final List<Integer> cups = new ArrayList<>(max);
        INPUT.chars().forEach(label -> cups.add(Integer.valueOf(String.valueOf((char) label))));
        IntStream.rangeClosed(10, max).forEach(cups::add);

        final Map<Integer, Integer> nextCupCache = new HashMap<>(cups.size());
        for (int i = 0; i < cups.size() - 1; i++) {
            nextCupCache.put(cups.get(i), cups.get(i + 1));
        }
        nextCupCache.put(cups.get(cups.size() - 1), cups.get(0));

        Integer currentCup = cups.get(0);
        for (int i = 1; i <= rounds; i++) {
            currentCup = playRound(nextCupCache, currentCup, min, max);
        }

        var i1 = nextCupCache.get(1);
        var i2 = nextCupCache.get(i1);

        return Long.toString(i1.longValue() * i2.longValue());
    }

    private Integer playRound(final Map<Integer, Integer> cache, final Integer currentCup, final Integer min,
            final Integer max)
    {
        Integer c1 = cache.get(currentCup);
        Integer c2 = cache.get(c1);
        Integer c3 = cache.get(c2);

        Integer destination = currentCup;
        do {
            destination = destination - 1;
            if (destination.compareTo(min) < 0) {
                destination = max;
            }
        } while (destination == c1 || destination == c2 || destination == c3);

        cache.put(currentCup, cache.get(c3));
        cache.put(c3, cache.get(destination));
        cache.put(destination, c1);

        return cache.get(currentCup);
    }
}
