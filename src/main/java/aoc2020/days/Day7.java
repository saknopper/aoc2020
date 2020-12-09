package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day7
{
    private final List<Rule> rules = new ArrayList<>();

    public Day7() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day7.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(l -> rules.add(Rule.createFromString(l)));
        lines.close();
    }

    public int getPossibleOutermostBagsForBag(String bag)
    {
        Set<String> possibleBags = new HashSet<>(); 
        findAmountOfPossibleOutermostBagsForBag(bag, possibleBags, rules);

        return possibleBags.size();
    }

    public int getAmountOfRequiredBagsToCarryBag(String bag)
    {
        return findAmountOfRequiredBagsToCarryBag(bag, rules);
    }

    private void findAmountOfPossibleOutermostBagsForBag(String bag, Set<String> possibleBags, List<Rule> rules)
    {
        for (var r : rules) {
            if (r.containsBag(bag)) {
                possibleBags.add(r.getBag());
                findAmountOfPossibleOutermostBagsForBag(r.getBag(), possibleBags, rules);
            }
        }
    }

    private int findAmountOfRequiredBagsToCarryBag(String bagInput, List<Rule> rules)
    {
        int amountOfBagsForCurrentLevel = 0;

        for (var r : rules) {
            if (bagInput.equals(r.getBag())) {
                for (var bag : r.getBags().entrySet()) {
                    amountOfBagsForCurrentLevel += bag.getValue();
                    amountOfBagsForCurrentLevel += bag.getValue() * findAmountOfRequiredBagsToCarryBag(bag.getKey(), rules);
                }
            }
        }

        return amountOfBagsForCurrentLevel;
    }

    private static class Rule
    {
        private final String bag;
        private final Map<String, Integer> bags;

        public Rule(String bag, Map<String, Integer> bags)
        {
            super();
            this.bag = bag;
            this.bags = bags;
        }

        public String getBag()
        {
            return bag;
        }

        public Map<String, Integer> getBags()
        {
            return bags;
        }

        public boolean containsBag(String bag)
        {
            Integer amount = bags.get(bag);

            return amount != null && !amount.equals(Integer.valueOf("0"));
        }

        @Override
        public String toString()
        {
            return "Rule [bag=" + bag + ", bags=" + bags + "]";
        }

        public static Rule createFromString(String ruleInput)
        {
            String normalizedInput = ruleInput.replace("bags", "bag").replace(".", "");
            String[] splitted1 = normalizedInput.split(" contain ");
            String[] childBags = splitted1[1].split(", ");

            Map<String, Integer> children = new HashMap<>();
            for (var c : childBags) {
                int index = c.indexOf(" ");
                Integer quantity = Integer.valueOf(0);
                try {
                    quantity = Integer.parseInt(c.substring(0, index));
                } catch (NumberFormatException e) { /* ignore */ }

                children.put(c.substring(index + 1), quantity);
            }

            return new Rule(splitted1[0], children);
        }
    }
}
