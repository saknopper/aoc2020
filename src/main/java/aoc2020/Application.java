package aoc2020;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import aoc2020.days.Day1;
import aoc2020.days.Day2;

public class Application
{
    private static final Logger log = Logger.getLogger(Application.class.getCanonicalName());

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        log.info("Advent of Code 2020");
        log.info("-----------------------------");
        log.info("");

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
    }
}
