package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import aoc2020.days.Day1;
import aoc2020.days.Day2;
import aoc2020.days.Day3;
import aoc2020.days.Day4;
import aoc2020.days.Day5;
import aoc2020.days.Day6;
import aoc2020.days.Day7;
import aoc2020.days.Day8;

public class Application
{
    private static final Logger log = Logger.getLogger(Application.class.getCanonicalName());

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        log.info("Advent of Code 2020");
        log.info("-----------------------------");
        log.info("");

        long startTimeMillis = System.currentTimeMillis();

        Day1 day1 = new Day1();
        int day1Part1Answer = day1.determineSumInExpenseReportAndGetProduct(2020, 2);
        log.info("day 1 part 1, answer: " + day1Part1Answer);

        int day1Part2Answer = day1.determineSumInExpenseReportAndGetProduct(2020, 3);
        log.info("day 1 part 2, answer: " + day1Part2Answer);

        Day2 day2 = new Day2();
        int day2Part1Answer = day2.getAmountOfPasswordsAdheringToPolicy(false);
        log.info("day 2 part 1, answer: " + day2Part1Answer);

        int day2Part2Answer = day2.getAmountOfPasswordsAdheringToPolicy(true);
        log.info("day 2 part 2, answer: " + day2Part2Answer);

        Day3 day3 = new Day3();
        long day3Part1Answer = day3.getAmountOfTreesEncounteredUsingSlope(3, 1);
        log.info("day 3 part 1, answer: " + day3Part1Answer);

        long day3Part2Answer1 = day3.getAmountOfTreesEncounteredUsingSlope(1, 1);
        long day3Part2Answer2 = day3.getAmountOfTreesEncounteredUsingSlope(3, 1);
        long day3Part2Answer3 = day3.getAmountOfTreesEncounteredUsingSlope(5, 1);
        long day3Part2Answer4 = day3.getAmountOfTreesEncounteredUsingSlope(7, 1);
        long day3Part2Answer5 = day3.getAmountOfTreesEncounteredUsingSlope(1, 2);
        long day3Part2Answer = day3Part2Answer1 * day3Part2Answer2 * day3Part2Answer3 * day3Part2Answer4
                * day3Part2Answer5;
        log.info("day 3 part 2, answer: " + day3Part2Answer);

        Day4 day4 = new Day4();
        long day4Part1Answer = day4.getAmountOfValidPassports(false);
        log.info("day 4 part 1, answer: " + day4Part1Answer);

        long day4Part2Answer = day4.getAmountOfValidPassports(true);
        log.info("day 4 part 2, answer: " + day4Part2Answer);

        Day5 day5 = new Day5();
        long day5Part1Answer = day5.getHighestSeatID();
        log.info("day 5 part 1, answer: " + day5Part1Answer);

        long day5Part2Answer = day5.getMySeatID();
        log.info("day 5 part 2, answer: " + day5Part2Answer);

        Day6 day6 = new Day6();
        long day6Part1Answer = day6.getSumOfUniqueQuestionsAnsweredYesPerGroup();
        log.info("day 6 part 1, answer: " + day6Part1Answer);

        long day6Part2Answer = day6.getSumOfQuestionsAnsweredYesByEveryonePerGroup();
        log.info("day 6 part 2, answer: " + day6Part2Answer);

        Day7 day7 = new Day7();
        long day7Part1Answer = day7.getPossibleOutermostBagsForBag("shiny gold bag");
        log.info("day 7 part 1, answer: " + day7Part1Answer);

        int day7Part2Answer = day7.getAmountOfRequiredBagsToCarryBag("shiny gold bag");
        log.info("day 7 part 2, answer: " + day7Part2Answer);

        Day8 day8 = new Day8();
        int day8Part1Answer = day8.getAccValueBeforeEnteringLoop();
        log.info("day 8 part 1, answer: " + day8Part1Answer);

        int day8Part2Answer = day8.fixProgramToMakeItTerminateAndGetAccValue();
        log.info("day 8 part 2, answer: " + day8Part2Answer);

        log.info("took: " + (System.currentTimeMillis() - startTimeMillis) + "ms");
    }
}
