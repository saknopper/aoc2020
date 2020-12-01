package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

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
    }
}
