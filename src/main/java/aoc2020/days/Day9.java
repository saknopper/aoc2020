package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.paukov.combinatorics3.Generator;

public class Day9
{
    private final List<Long> numbers = new ArrayList<>();

    public Day9() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day9.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> numbers.add(Long.parseLong(line)));
        lines.close();
    }

    public long getFirstNumberThatIsNoSumOfPrevious(int preamble)
    {
        Long firstThatIsNoSum = 0L;
        for (int i = preamble; i < numbers.size(); i++) {
            List<Long> subList = numbers.subList(i - preamble, i);
            Long sumTarget = numbers.get(i);

            if (!hasNumbersThatMakeUpSum(sumTarget, subList)) {
                firstThatIsNoSum = sumTarget;
                break;
            }
        }

        return firstThatIsNoSum;
    }

    private boolean hasNumbersThatMakeUpSum(Long sumTarget, List<Long> input)
    {
        List<List<Long>> validForTargetSum = Generator.combination(input).simple(2).stream()
                .filter(items -> items.stream().collect(Collectors.summingLong(Long::longValue)).equals(sumTarget))
                .collect(Collectors.toList());

        return !validForTargetSum.isEmpty();
    }

    public long getSmallestAndLargestNumberAsSumFromContiguousSetOfNumbersAddingUpTo(long sumTarget)
    {
        List<Long> numbersAddingUpToSumTarget = getContiguousSetOfNumbersAddingUpTo(sumTarget);
        Collections.sort(numbersAddingUpToSumTarget);

        return numbersAddingUpToSumTarget.get(0)
                + numbersAddingUpToSumTarget.get(numbersAddingUpToSumTarget.size() - 1);
    }

    private List<Long> getContiguousSetOfNumbersAddingUpTo(long sumTarget)
    {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 2; j < numbers.size() - i; j++) {
                List<Long> subList = numbers.subList(i, j);
                long sum = subList.stream().collect(Collectors.summingLong(Long::longValue)).longValue();

                if (sum == sumTarget) {
                    return subList;
                }

                if (sum > sumTarget) {
                    break;
                }
            }
        }

        return new ArrayList<>();
    }
}
