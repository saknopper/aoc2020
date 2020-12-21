package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class Day21
{
    private final List<Food> foods = new ArrayList<>();

    private final Set<String> allAllergens = new HashSet<>();
    private final Set<String> ingredients = new HashSet<>();
    private final Map<String, Integer> countsPerIngredient = new HashMap<>();
    private final Map<String, Set<String>> possibleIngredientsForAllergen = new HashMap<>();

    public Day21() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day21.txt").toURI());

        var lines = Files.lines(path);
        lines.forEach(i -> foods.add(Food.createFromString(i)));
        lines.close();

        for (var f : foods) {
            for (var i : f.getIngredients()) {
                ingredients.add(i);
                countsPerIngredient.merge(i, 1, (o, n) -> o + n);
            }
            for (var a : f.getAllergens()) {
                allAllergens.add(a);
            }
        }

        for (String aller : allAllergens) {
            possibleIngredientsForAllergen.put(aller, new HashSet<>(ingredients));
        }

        for (var f : foods) {
            for (var a : f.getAllergens()) {
                for (var i : ingredients) {
                    if (!f.getIngredients().contains(i)) {
                        possibleIngredientsForAllergen.get(a).remove(i);
                    }
                }
            }
        }
    }

    public long part1()
    {
        int count = 0;
        for (var i : ingredients) {
            boolean skip = false;
            for (Set<String> poss : possibleIngredientsForAllergen.values()) {
                if (poss.contains(i)) {
                    skip = true;
                }
            }
            if (!skip) {
                count += countsPerIngredient.get(i);
            }
        }

        return count;
    }

    public String part2()
    {
        Set<String> complete = new HashSet<>();
        while (complete.size() < allAllergens.size()) {
            for (var allergenA : allAllergens) {
                if (possibleIngredientsForAllergen.get(allergenA).size() == 1 && !complete.contains(allergenA)) {
                    complete.add(allergenA);
                    String i = possibleIngredientsForAllergen.get(allergenA).iterator().next();
                    for (var allergenB : allAllergens) {
                        if (!allergenA.equals(allergenB)) {
                            possibleIngredientsForAllergen.get(allergenB).remove(i);
                        }
                    }
                }
            }
        }

        final StringJoiner joiner = new StringJoiner(",");
        allAllergens.stream().sorted()
                .forEach(a -> joiner.add(possibleIngredientsForAllergen.get(a).iterator().next()));

        return joiner.toString();
    }

    private static class Food
    {
        private final List<String> ingredients;
        private final List<String> allergens;

        public Food(List<String> ingredients, List<String> allergens)
        {
            super();
            this.ingredients = ingredients;
            this.allergens = allergens;
        }

        public List<String> getIngredients()
        {
            return ingredients;
        }

        public List<String> getAllergens()
        {
            return allergens;
        }

        @Override
        public String toString()
        {
            return "Food [ingredients=" + ingredients + ", allergens=" + allergens + "]";
        }

        public static Food createFromString(String input)
        {
            int splitIndex = input.indexOf('(');
            String ingredients = input.substring(0, splitIndex - 1);
            String allergens = input.substring(splitIndex + 10, input.length() - 1);

            return new Food(new ArrayList<>(Arrays.asList(ingredients.split(" "))),
                    new ArrayList<>(Arrays.asList(allergens.split(", "))));
        }
    }
}
