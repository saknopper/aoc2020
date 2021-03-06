package aoc2020;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import aoc2020.days.*;

public class Application
{
    private static Logger LOG;

    static {
        try (InputStream stream = Application.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
            LOG = Logger.getLogger(Application.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        LOG.info("---------------------------------------------");
        LOG.info("Advent of Code 2020");
        LOG.info("---------------------------------------------");
        LOG.info("");

        Day1 day1 = new Day1();
        int day1Part1Answer = day1.determineSumInExpenseReportAndGetProduct(2020, 2);
        LOG.info("day 1 part 1, answer: " + day1Part1Answer);

        int day1Part2Answer = day1.determineSumInExpenseReportAndGetProduct(2020, 3);
        LOG.info("day 1 part 2, answer: " + day1Part2Answer);

        Day2 day2 = new Day2();
        int day2Part1Answer = day2.getAmountOfPasswordsAdheringToPolicy(false);
        LOG.info("day 2 part 1, answer: " + day2Part1Answer);

        int day2Part2Answer = day2.getAmountOfPasswordsAdheringToPolicy(true);
        LOG.info("day 2 part 2, answer: " + day2Part2Answer);

        Day3 day3 = new Day3();
        long day3Part1Answer = day3.getAmountOfTreesEncounteredUsingSlope(3, 1);
        LOG.info("day 3 part 1, answer: " + day3Part1Answer);

        long day3Part2Answer1 = day3.getAmountOfTreesEncounteredUsingSlope(1, 1);
        long day3Part2Answer2 = day3.getAmountOfTreesEncounteredUsingSlope(3, 1);
        long day3Part2Answer3 = day3.getAmountOfTreesEncounteredUsingSlope(5, 1);
        long day3Part2Answer4 = day3.getAmountOfTreesEncounteredUsingSlope(7, 1);
        long day3Part2Answer5 = day3.getAmountOfTreesEncounteredUsingSlope(1, 2);
        long day3Part2Answer = day3Part2Answer1 * day3Part2Answer2 * day3Part2Answer3 * day3Part2Answer4
                * day3Part2Answer5;
        LOG.info("day 3 part 2, answer: " + day3Part2Answer);

        Day4 day4 = new Day4();
        long day4Part1Answer = day4.getAmountOfValidPassports(false);
        LOG.info("day 4 part 1, answer: " + day4Part1Answer);

        long day4Part2Answer = day4.getAmountOfValidPassports(true);
        LOG.info("day 4 part 2, answer: " + day4Part2Answer);

        Day5 day5 = new Day5();
        long day5Part1Answer = day5.getHighestSeatID();
        LOG.info("day 5 part 1, answer: " + day5Part1Answer);

        long day5Part2Answer = day5.getMySeatID();
        LOG.info("day 5 part 2, answer: " + day5Part2Answer);

        Day6 day6 = new Day6();
        long day6Part1Answer = day6.getSumOfUniqueQuestionsAnsweredYesPerGroup();
        LOG.info("day 6 part 1, answer: " + day6Part1Answer);

        long day6Part2Answer = day6.getSumOfQuestionsAnsweredYesByEveryonePerGroup();
        LOG.info("day 6 part 2, answer: " + day6Part2Answer);

        Day7 day7 = new Day7();
        long day7Part1Answer = day7.getPossibleOutermostBagsForBag("shiny gold bag");
        LOG.info("day 7 part 1, answer: " + day7Part1Answer);

        int day7Part2Answer = day7.getAmountOfRequiredBagsToCarryBag("shiny gold bag");
        LOG.info("day 7 part 2, answer: " + day7Part2Answer);

        Day8 day8 = new Day8();
        int day8Part1Answer = day8.getAccValueBeforeEnteringLoop();
        LOG.info("day 8 part 1, answer: " + day8Part1Answer);

        int day8Part2Answer = day8.fixProgramToMakeItTerminateAndGetAccValue();
        LOG.info("day 8 part 2, answer: " + day8Part2Answer);

        Day9 day9 = new Day9();
        long day9Part1Answer = day9.getFirstNumberThatIsNoSumOfPrevious(25);
        LOG.info("day 9 part 1, answer: " + day9Part1Answer);

        long day9Part2Answer = day9
                .getSmallestAndLargestNumberAsSumFromContiguousSetOfNumbersAddingUpTo(day9Part1Answer);
        LOG.info("day 9 part 2, answer: " + day9Part2Answer);

        Day10 day10 = new Day10();
        long day10Part1Answer = day10.getJoltDifferences();
        LOG.info("day 10 part 1, answer: " + day10Part1Answer);

        long day10Part2Answer = day10.getValidCombinations();
        LOG.info("day 10 part 2, answer: " + day10Part2Answer);

        Day11 day11 = new Day11();
        long day11Part1Answer = day11.getOccupiedSeatsAfterStabilizationAdjacentRules();
        LOG.info("day 11 part 1, answer: " + day11Part1Answer);

        long day11Part2Answer = day11.getOccupiedSeatsAfterStabilizationLineOfSightRules();
        LOG.info("day 11 part 2, answer: " + day11Part2Answer);

        Day12 day12 = new Day12();
        long day12Part1Answer = day12.getManhattanDistance();
        LOG.info("day 12 part 1, answer: " + day12Part1Answer);

        long day12Part2Answer = day12.getManhattanDistanceWithWaypoint();
        LOG.info("day 12 part 2, answer: " + day12Part2Answer);

        Day13 day13 = new Day13();
        long day13Part1Answer = day13.getEarliestBusIdMultipliedByNumerOfMinutesToWait();
        LOG.info("day 13 part 1, answer: " + day13Part1Answer);

        long day13Part2Answer = day13.findDepartureTimeLeadingToSubsequentDepartures();
        LOG.info("day 13 part 2, answer: " + day13Part2Answer);

        Day14 day14 = new Day14();
        long day14Part1Answer = day14.getSumOfValuesInMemoryPart1();
        LOG.info("day 14 part 1, answer: " + day14Part1Answer);

        long day14Part2Answer = day14.getSumOfValuesInMemoryPart2();
        LOG.info("day 14 part 2, answer: " + day14Part2Answer);

        Day15 day15 = new Day15();
        long day15Part1Answer = day15.getNumberAtRound(2020);
        LOG.info("day 15 part 1, answer: " + day15Part1Answer);

        long day15Part2Answer = day15.getNumberAtRound(30000000);
        LOG.info("day 15 part 2, answer: " + day15Part2Answer);

        Day16 day16 = new Day16();
        long day16Part1Answer = day16.getErrorRate();
        LOG.info("day 16 part 1, answer: " + day16Part1Answer);

        long day16Part2Answer = day16.productOfFieldsStartingWithDeparture();
        LOG.info("day 16 part 2, answer: " + day16Part2Answer);

        Day17 day17 = new Day17();
        long day17Part1Answer = day17.getActiveCubesAfterCycles3D(6);
        LOG.info("day 17 part 1, answer: " + day17Part1Answer);

        long day17Part2Answer = day17.getActiveCubesAfterCycles4D(6);
        LOG.info("day 17 part 2, answer: " + day17Part2Answer);

        Day18 day18 = new Day18();
        long day18Part1Answer = day18.evaluateAndGetSum(false);
        LOG.info("day 18 part 1, answer: " + day18Part1Answer);

        long day18Part2Answer = day18.evaluateAndGetSum(true);
        LOG.info("day 18 part 2, answer: " + day18Part2Answer);

        Day19 day19 = new Day19();
        long day19Part1Answer = day19.getAmountOfMessagesAdheringToRulePart1(0);
        LOG.info("day 19 part 1, answer: " + day19Part1Answer);

        long day19Part2Answer = day19.getAmountOfMessagesAdheringToRulePart2();
        LOG.info("day 19 part 2, answer: " + day19Part2Answer);

        Day20 day20 = new Day20();
        long day20Part1Answer = day20.getProductOfCornerTiles();
        LOG.info("day 20 part 1, answer: " + day20Part1Answer);

        long day20Part2Answer = day20.part2();
        LOG.info("day 20 part 2, answer: " + day20Part2Answer);

        Day21 day21 = new Day21();
        long day21Part1Answer = day21.part1();
        LOG.info("day 21 part 1, answer: " + day21Part1Answer);

        String day21Part2Answer = day21.part2();
        LOG.info("day 21 part 2, answer: " + day21Part2Answer);

        Day22 day22 = new Day22();
        long day22Part1Answer = day22.part1();
        LOG.info("day 22 part 1, answer: " + day22Part1Answer);

        long day22Part2Answer = day22.part2();
        LOG.info("day 22 part 2, answer: " + day22Part2Answer);

        Day23 day23 = new Day23();
        String day23Part1Answer = day23.part1();
        LOG.info("day 23 part 1, answer: " + day23Part1Answer);

        String day23Part2Answer = day23.part2();
        LOG.info("day 23 part 2, answer: " + day23Part2Answer);

        Day24 day24 = new Day24();
        long day24Part1Answer = day24.part1();
        LOG.info("day 24 part 1, answer: " + day24Part1Answer);

        long day24Part2Answer = day24.part2();
        LOG.info("day 24 part 2, answer: " + day24Part2Answer);

        Day25 day25 = new Day25();
        long day25Part1Answer = day25.part1();
        LOG.info("day 25 part 1, answer: " + day25Part1Answer);
    }
}
