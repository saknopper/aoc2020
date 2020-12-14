package aoc2020.days;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14
{
    private final List<String> input;

    public Day14() throws URISyntaxException, IOException
    {
        super();

        Path path = Paths.get(getClass().getClassLoader().getResource("day14.txt").toURI());

        var lines = Files.lines(path);
        input = lines.collect(Collectors.toList());
        lines.close();
    }

    public long getSumOfValuesInMemoryPart1()
    {
        String currentMask = "";
        Map<Long, Long> memory = new HashMap<>();

        for (var l : input) {
            String[] args = l.split(" = ");
            if (args[0].equals("mask")) {
                currentMask = new StringBuilder(args[1]).reverse().toString();
                continue;
            }

            BigInteger value = new BigInteger(args[1]);
            Long memAddress = Long.parseLong(args[0].substring(4, args[0].length() - 1));

            for (var i = 0; i < currentMask.length(); i++) {
                char maskValue = currentMask.charAt(i);
                if (maskValue == '0') {
                    value = value.clearBit(i);
                } else if (maskValue == '1') {
                    value = value.setBit(i);
                }
            }

            memory.put(memAddress, value.longValue());
        }

        long sum = 0L;
        for (var v : memory.values()) {
            sum += v.longValue();
        }

        return sum;
    }

    public long getSumOfValuesInMemoryPart2()
    {
        String currentMask = "";
        Map<Long, Long> memory = new HashMap<>();

        for (var l : input) {
            String[] args = l.split(" = ");
            if (args[0].equals("mask")) {
                currentMask = new StringBuilder(args[1]).reverse().toString();
                continue;
            }

            List<Integer> floatingBits = new ArrayList<>();

            Long value = Long.parseLong(args[1]);
            BigInteger memAddress = new BigInteger(args[0].substring(4, args[0].length() - 1));

            for (var i = 0; i < currentMask.length(); i++) {
                char maskValue = currentMask.charAt(i);
                if (maskValue == '1') {
                    memAddress = memAddress.setBit(i);
                } else if (maskValue == 'X') {
                    floatingBits.add(Integer.valueOf(i));
                }
            }

            List<BigInteger> floatingCombinations = new ArrayList<>();
            getCombinationsForFloatingBits(memAddress, floatingBits, floatingCombinations);

            for (var a : floatingCombinations) {
                memory.put(a.longValue(), value);
            }
        }

        long sum = 0L;
        for (var v : memory.values()) {
            sum += v.longValue();
        }

        return sum;
    }

    private void getCombinationsForFloatingBits(BigInteger value, List<Integer> floating, List<BigInteger> combinations)
    {
        if (floating.isEmpty()) {
            combinations.add(value);
            return;
        }

        Integer bit = floating.remove(0);

        BigInteger optionUnset = value.clearBit(bit);
        BigInteger optionSet = value.setBit(bit);

        getCombinationsForFloatingBits(optionUnset, new ArrayList<>(floating), combinations);
        getCombinationsForFloatingBits(optionSet, new ArrayList<>(floating), combinations);
    }
}
