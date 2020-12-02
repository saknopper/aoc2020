package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2
{
    private static final Logger log = Logger.getLogger(Day2.class.getCanonicalName());

    public int getAmountOfPasswordsAdheringToPolicy(boolean useNewPolicy) throws URISyntaxException, IOException
    {
        final List<PasswordAndPolicy> passwordItems = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("day2.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(l -> passwordItems.add(PasswordAndPolicy.createFromString(l)));
        lines.close();

        List<PasswordAndPolicy> compliantPasswords;

        if (useNewPolicy) {
            compliantPasswords = passwordItems.stream().filter(i -> i.passwordCompliesWithNewPolicy())
                    .collect(Collectors.toList());
        } else {
            compliantPasswords = passwordItems.stream().filter(i -> i.passwordCompliesWithOldPolicy())
                    .collect(Collectors.toList());
        }

        return compliantPasswords.size();
    }

    private static class PasswordAndPolicy
    {
        private final String password;
        private final String character;
        private final Integer minOccurences;
        private final Integer maxOccurences;

        public PasswordAndPolicy(String password, String character, Integer minOccurences, Integer maxOccurences)
        {
            super();
            this.password = password;
            this.character = character;
            this.minOccurences = minOccurences;
            this.maxOccurences = maxOccurences;
        }

        public static PasswordAndPolicy createFromString(String passwordAndPolicy)
        {
            String[] splitted1 = passwordAndPolicy.split(": ");
            String[] splitted2 = splitted1[0].split(" ");
            String[] splitted3 = splitted2[0].split("-");
            String password = splitted1[1];
            String character = splitted2[1];
            Integer minOccurences = Integer.parseInt(splitted3[0]);
            Integer maxOccurences = Integer.parseInt(splitted3[1]);

            return new PasswordAndPolicy(password, character, minOccurences, maxOccurences);
        }

        public boolean passwordCompliesWithOldPolicy()
        {
            long count = password.chars().filter(c -> c == character.charAt(0)).count();

            return count >= minOccurences.longValue() && count <= maxOccurences.longValue();
        }

        public boolean passwordCompliesWithNewPolicy()
        {
            // minOccurences and maxOccurences are now interpreted as indices (starting from 1!)
            int index1 = minOccurences - 1;
            int index2 = maxOccurences - 1;

            boolean foundAtIndex1 = password.charAt(index1) == character.charAt(0);
            boolean foundAtIndex2 = password.charAt(index2) == character.charAt(0);

            if (foundAtIndex1 && foundAtIndex2) {
                return false;
            }

            return foundAtIndex1 || foundAtIndex2;
        }

        @Override
        public String toString()
        {
            return "PasswordAndPolicy [password=" + password + ", character=" + character + ", minOccurences="
                    + minOccurences + ", maxOccurences=" + maxOccurences + "]";
        }
    }
}
