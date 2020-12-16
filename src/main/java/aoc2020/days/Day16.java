package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day16
{
    private final List<Rule> rules = new ArrayList<>();
    private final List<List<Integer>> myTicket = new ArrayList<>();
    private final List<List<Integer>> nearbyTickets = new ArrayList<>();

    public Day16() throws URISyntaxException, IOException
    {
        super();

        Path path = Paths.get(getClass().getClassLoader().getResource("day16.txt").toURI());

        var lines = Files.lines(path);
        List<String> list = lines.collect(Collectors.toList());
        lines.close();

        List<List<Integer>> destination = null;
        for (var l : list) {
            if (l.isBlank()) {
                continue;
            }
            if (l.equals("your ticket:")) {
                destination = myTicket;
                continue;
            }
            if (l.equals("nearby tickets:")) {
                destination = nearbyTickets;
                continue;
            }

            if (destination != null) {
                List<Integer> ticket = new ArrayList<>();
                for (var n : l.split(",")) {
                    ticket.add(Integer.valueOf(n));
                }
                destination.add(ticket);
            } else {
                String[] split1 = l.split(": ");
                String[] rulesStr = split1[1].split(" or ");
                String[] rule1 = rulesStr[0].split("-");
                String[] rule2 = rulesStr[1].split("-");

                String name = split1[0];
                rules.add(new Rule(name, Integer.valueOf(rule1[0]), Integer.valueOf(rule1[1]),
                        Integer.valueOf(rule2[0]), Integer.valueOf(rule2[1])));
            }
        }
    }

    public int getErrorRate()
    {
        List<Integer> invalidValues = new ArrayList<>();

        for (var t : nearbyTickets) {
            for (var v : t) {
                boolean isValid = false;
                for (var r : rules) {
                    if (r.isValid(v)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    invalidValues.add(v);
                }
            }
        }

        return invalidValues.stream().collect(Collectors.summingInt(Integer::valueOf));
    }

    public long productOfFieldsStartingWithDeparture()
    {
        var tickets = filterInvalidTickets(nearbyTickets, rules);

        // Field index -> Rule -> Amount valid
        Map<Integer, Map<Rule, Integer>> scores = new HashMap<>();

        for (int i = 0; i < tickets.get(0).size(); i++) {
            scores.put(Integer.valueOf(i), new HashMap<>());
            for (var r : rules) {
                int counter = 0;
                for (var t : tickets) {
                    if (isTicketValid(t, r, i)) {
                        counter++;
                    }
                }
                scores.get(Integer.valueOf(i)).put(r, Integer.valueOf(counter));
            }
        }

        // Field index -> Rule
        Map<Integer, Rule> mapping = new HashMap<>();

        int candidatesMatch = 1;
        for (Iterator<Integer> iterator = scores.keySet().iterator(); iterator.hasNext();) {
            Integer index = iterator.next();

            Rule candidate = null;
            int candidates = 0;
            Map<Rule, Integer> ruleScore = scores.get(index);
            for (var entry : ruleScore.entrySet()) {
                if (entry.getValue().intValue() == tickets.size()) {
                    candidates++;
                    if (!mapping.values().contains(entry.getKey())) {
                        candidate = entry.getKey();
                    }
                }
            }

            if (candidates == candidatesMatch) {
                candidatesMatch++;
                mapping.put(index, candidate);
                scores.remove(index);
                iterator = scores.keySet().iterator();
            }
        }

        long product = 1L;
        for (var entry : mapping.entrySet()) {
            if (entry.getValue().getName().startsWith("departure")) {
                product *= myTicket.get(0).get(entry.getKey());
            }
        }

        return product;
    }

    private static List<List<Integer>> filterInvalidTickets(List<List<Integer>> tickets, List<Rule> rules)
    {
        List<List<Integer>> filtered = new ArrayList<>();

        for (var t : tickets) {
            if (!isTicketInvalid(t, rules)) {
                filtered.add(t);
            }
        }

        return filtered;
    }

    private static boolean isTicketInvalid(List<Integer> ticket, List<Rule> rules)
    {
        boolean isValidTicket = true;
        for (var v : ticket) {
            boolean isValidValue = false;
            for (var r : rules) {
                if (r.isValid(v)) {
                    isValidValue = true;
                    break;
                }
            }
            if (!isValidValue) {
                isValidTicket = false;
            }
        }

        return !isValidTicket;
    }

    private static boolean isTicketValid(List<Integer> ticket, Rule rule, int fieldIndex)
    {
        var v = ticket.get(fieldIndex);

        return rule.isValid(v);
    }

    private static class Rule
    {
        private final String name;

        private final int min1;
        private final int max1;

        private final int min2;
        private final int max2;

        public Rule(String name, int min1, int max1, int min2, int max2)
        {
            super();
            this.name = name;
            this.min1 = min1;
            this.max1 = max1;
            this.min2 = min2;
            this.max2 = max2;
        }

        public String getName()
        {
            return name;
        }

        public boolean isValid(int input)
        {
            return (input >= min1 && input <= max1) || (input >= min2 && input <= max2);
        }

        @Override
        public String toString()
        {
            return "Rule [name=" + name + ", min1=" + min1 + ", max1=" + max1 + ", min2=" + min2 + ", max2=" + max2
                    + "]";
        }
    }
}
