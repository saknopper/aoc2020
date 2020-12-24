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
import java.util.stream.Collectors;

public class Day24
{
    private static final String DIRECTIONS[] = { "se", "sw", "nw", "ne", "e", "w" };
    private static final int MOVES[][] = { { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 1 }, { -1, -1 } };

    private final List<String> input = new ArrayList<>();

    private Set<Vector> blackTiles = new HashSet<>();

    public Day24() throws URISyntaxException, IOException
    {
        super();

        Path path = Paths.get(getClass().getClassLoader().getResource("day24.txt").toURI());
        var lines = Files.lines(path);
        input.addAll(lines.collect(Collectors.toList()));
        lines.close();
    }

    public long part1()
    {
        blackTiles = new HashSet<>();

        // Initial black tiles
        for (var line : input) {
            var l = new String(line);
            Vector v = new Vector();

            while (!l.isEmpty()) {
                for (int i = 0; i < 6; i++) {
                    if (l.startsWith(DIRECTIONS[i])) {
                        l = l.substring(DIRECTIONS[i].length());
                        v.posX += MOVES[i][0];
                        v.posY += MOVES[i][1];
                    }
                }
            }

            if (blackTiles.contains(v)) {
                blackTiles.remove(v);
            } else {
                blackTiles.add(v);
            }
        }

        return blackTiles.size();
    }

    public long part2()
    {
        for (int i = 0; i < 100; i++) {
            blackTiles = applyRulesToFlipTiles(blackTiles);
        }

        return blackTiles.size();
    }

    private Set<Vector> applyRulesToFlipTiles(Set<Vector> blackTiles)
    {
        Map<Vector, Integer> newTileCounts = new HashMap<>();
        for (var bt : blackTiles) {
            getNewTileCounts(bt, newTileCounts);
        }

        Set<Vector> newBlackTiles = new HashSet<>();
        for (var entry : newTileCounts.entrySet()) {
            if ((blackTiles.contains(entry.getKey()) && entry.getValue() != 0 && entry.getValue() <= 2)
                    || (!blackTiles.contains(entry.getKey()) && entry.getValue() == 2)) {
                newBlackTiles.add(entry.getKey());
            }
        }

        return newBlackTiles;
    }

    private void getNewTileCounts(Vector tile, Map<Vector, Integer> newTileCounts)
    {
        for (var m : MOVES) {
            Vector v = new Vector(tile.posX + m[0], tile.posY + m[1]);
            if (!newTileCounts.containsKey(v)) {
                newTileCounts.put(v, 1);
            } else {
                newTileCounts.compute(v, (theVector, counter) -> counter + 1);
            }
        }
    }

    private static class Vector
    {
        int posX = 0;
        int posY = 0;

        public Vector()
        {
            super();
        }

        public Vector(int posX, int posY)
        {
            super();
            this.posX = posX;
            this.posY = posY;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + posX;
            result = prime * result + posY;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vector other = (Vector) obj;
            if (posX != other.posX)
                return false;
            if (posY != other.posY)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Vector [posX=" + posX + ", posY=" + posY + "]";
        }
    }
}
