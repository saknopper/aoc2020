package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day3
{
    public long getAmountOfTreesEncounteredUsingSlope(int right, int down) throws URISyntaxException, IOException
    {
        final List<List<MapPosition>> map = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("day3.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(l -> {
            List<MapPosition> row = new ArrayList<>();
            l.chars().forEach(c -> row.add(MapPosition.createFromChar((char) c)));
            map.add(row);
        });
        lines.close();

        int mapWidth = map.get(0).size();
        long amountOfTreesEncountered = 0;

        int posX = 0, posY = 0;
        while (posY < map.size()) {
            if (map.get(posY).get(posX % mapWidth).getType().equals(PositionType.TREE)) {
                amountOfTreesEncountered++;
            }

            posX += right;
            posY += down;
        }

        return amountOfTreesEncountered;
    }

    private static class MapPosition
    {
        private final PositionType type;

        public MapPosition(PositionType type)
        {
            super();
            this.type = type;
        }

        public PositionType getType()
        {
            return type;
        }

        @Override
        public String toString()
        {
            return "MapPosition [type=" + type + "]";
        }

        public static MapPosition createFromChar(char position)
        {
            switch (position) {
            case '#':
                return new MapPosition(PositionType.TREE);
            case '.':
                return new MapPosition(PositionType.OPEN);
            }

            throw new IllegalArgumentException("Can't determine type of map position");
        }
    }

    private enum PositionType {
        OPEN,
        TREE
    }
}
