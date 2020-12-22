package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20
{
    private static final int FLIP_ROTATE_POSSIBILITIES[][] = { { 0, 0 }, { 90, 0 }, { 180, 0 }, { 270, 0 }, { 0, 1 },
            { 90, 1 }, { 180, 1 }, { 270, 1 } };

    private static final int CHECK_POSITION[][] = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
    private static final String CHECK_POSITION_RESULT[] = { "above", "below", "left", "right" };

    private final Tile seaMonster;

    private final List<Tile> tiles = new ArrayList<>();

    public Day20() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day20.txt").toURI());
        var lines = Files.lines(path);
        List<String> input = lines.collect(Collectors.toList());
        lines.close();
        input.add("");

        int currentTileId = -1;
        List<String> currentTileData = new ArrayList<>();
        for (var l : input) {
            if (l.isBlank()) {
                tiles.add(new Tile(currentTileId, currentTileData));
                currentTileData = new ArrayList<>();
                continue;
            }
            if (l.startsWith("Tile ")) {
                currentTileId = Integer.parseInt(l.substring(5, 9));
                continue;
            }

            currentTileData.add(l);
        }

        List<String> seaMonsterInput = new ArrayList<>();
        seaMonsterInput.add("                  # ");
        seaMonsterInput.add("#    ##    ##    ###");
        seaMonsterInput.add(" #  #  #  #  #  #   ");

        seaMonster = new Tile(66666, seaMonsterInput);
    }

    public long getProductOfCornerTiles()
    {
        long product = 1L;
        for (var t1 : tiles) {
            int counter = 0;
            for (var t2 : tiles) {
                int matchingSides = matchingSides(t1, t2);
                counter += matchingSides;
            }

            if (counter == 2) {
                product *= t1.getId();
            }
        }

        return product;
    }

    public long part2()
    {
        for (var t : tiles) {
            t.setRotate(0);
            t.setFlip(false);
        }

        List<Tile> remaining = new ArrayList<>(tiles);

        List<Tile> cornerTiles = new ArrayList<>();
        for (var t1 : tiles) {
            int counterSides = 0;
            for (var t2 : tiles) {
                int matchingSides = matchingSides(t1, t2);
                counterSides += matchingSides;
            }

            if (counterSides == 2) {
                cornerTiles.add(t1);
            }
        }

        int rowLength = (int) Math.sqrt(tiles.size());

        Tile topLeft = null;
        for (var t1 : cornerTiles) {
            boolean done = false;

            Set<String> sidesMatch = new HashSet<>();
            for (var tOther : remaining) {
                if (cornerTiles.contains(tOther)) {
                    continue;
                }

                for (var rf2 : FLIP_ROTATE_POSSIBILITIES) {
                    tOther.setRotate(rf2[0]);
                    tOther.setFlip(rf2[1] == 1);
                    sidesMatch.addAll(tilesFit(t1, tOther));
                }
            }

            if (sidesMatch.size() >= 2) {
                if (sidesMatch.contains("below") && sidesMatch.contains("right")) {
                    topLeft = t1;
                    done = true;

                    break;
                }
            }

            if (done) {
                break;
            }
        }

        remaining.remove(topLeft);

        List<List<Integer>> jigsaw = new ArrayList<>(rowLength);
        for (int i = 0; i < rowLength; i++) {
            List<Integer> row = new ArrayList<>(rowLength);
            padTo(row, rowLength);
            jigsaw.add(row);
        }

        jigsaw.get(0).set(0, topLeft.getId());

        for (int j = 0; j < jigsaw.size(); j++) {
            for (int i = 0; i < jigsaw.get(j).size(); i++) {
                if (j == 0 && i == 0) {
                    continue;
                }

                Tile usingTile = null;
                for (var tOther : remaining) {
                    for (var rf : FLIP_ROTATE_POSSIBILITIES) {
                        tOther.setRotate(rf[0]);
                        tOther.setFlip(rf[1] == 1);

                        int surroundingTilesFit = surroundingTilesFit(j, i, tOther, jigsaw);
                        if ((j == 0 && surroundingTilesFit == 1) || (i == 0 && surroundingTilesFit == 1)
                                || surroundingTilesFit == 2) {
                            jigsaw.get(j).set(i, tOther.getId());
                            usingTile = tOther;
                            break;
                        }
                    }
                    if (usingTile != null) {
                        break;
                    }
                }

                if (usingTile != null) {
                    remaining.remove(usingTile);
                }
            }
        }

        List<String> finalImage = new ArrayList<>();
        for (var row : jigsaw) {
            for (int line = 1; line < getTileById(row.get(0)).getData().size() - 1; line++) {
                StringBuilder sb = new StringBuilder();
                for (var col : row) {
                    String colStr = getTileById(col).getData().get(line);
                    sb.append(colStr.substring(1, colStr.length() - 1));
                }
                finalImage.add(sb.toString());
            }
        }

        int seaMonstersFound = 0;
        for (var rf : FLIP_ROTATE_POSSIBILITIES) {
            seaMonster.setRotate(rf[0]);
            seaMonster.setFlip(rf[1] == 1);

            int found = getAmountOfSeaMonsterOccurences(finalImage, seaMonster);
            if (found > 0) {
                seaMonstersFound = found;
                break;
            }
        }

        int hashesInSeaMonster = getAmountOfHashes(seaMonster.getData());
        int hashesInImage = getAmountOfHashes(finalImage);

        return (long) hashesInImage - (seaMonstersFound * hashesInSeaMonster);
    }

    private int getAmountOfHashes(List<String> input)
    {
        int count = 0;

        for (var l : input) {
            for (var c : l.toCharArray()) {
                if (c == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    private int getAmountOfSeaMonsterOccurences(List<String> image, Tile seaMonster)
    {
        int counter = 0;

        List<String> seaMonsterData = seaMonster.getData();

        for (int imageRow = 0; imageRow < image.size() - seaMonsterData.size(); imageRow++) {
            for (int imageCol = 0; imageCol < image.get(0).length() - seaMonsterData.get(0).length(); imageCol++) {

                boolean valid = true;
                for (int monsterRow = 0; monsterRow < seaMonsterData.size(); monsterRow++) {
                    for (int monsterCol = 0; monsterCol < seaMonsterData.get(0).length(); monsterCol++) {
                        if (seaMonsterData.get(monsterRow).charAt(monsterCol) == '#') {
                            if (image.get(imageRow + monsterRow).charAt(imageCol + monsterCol) != '#') {
                                valid = false;
                            }
                        }
                    }
                }

                if (valid) {
                    counter++;
                }
            }
        }

        return counter;
    }

    private int matchingSides(Tile t1, Tile t2)
    {
        if (t1 == t2) {
            return 0;
        }

        int counter = 0;

        List<String> t1Borders = t1.getBorders();
        List<String> t2Borders = t2.getBorders();

        for (var border : t2Borders) {
            String borderReversed = new StringBuilder(border).reverse().toString();
            if (t1Borders.contains(border) || t1Borders.contains(borderReversed)) {
                counter++;
            }
        }

        return counter;
    }

    private int surroundingTilesFit(int posY, int posX, Tile candidateTile, List<List<Integer>> jigsaw)
    {
        int matches = 0;

        for (var i = 0; i < CHECK_POSITION.length; i++) {
            try {
                List<Integer> row = jigsaw.get(posY + CHECK_POSITION[i][1]);
                Integer tileIdToCheck = row.get(posX + CHECK_POSITION[i][0]);
                if (tileIdToCheck != null) {
                    Tile tileToCheck = getTileById(tileIdToCheck);
                    if (tilesFit(candidateTile, tileToCheck).contains(CHECK_POSITION_RESULT[i])) {
                        matches++;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

        return matches;
    }

    private List<String> tilesFit(Tile placed, Tile candidate)
    {
        List<String> possible = new ArrayList<>();

        List<String> pBorders = placed.getBorders();
        List<String> cBorders = candidate.getBorders();

        if (pBorders.get(0).equals(cBorders.get(1))) {
            possible.add("above");
        }

        if (pBorders.get(1).equals(cBorders.get(0))) {
            possible.add("below");
        }

        if (pBorders.get(2).equals(cBorders.get(3))) {
            possible.add("right");
        }

        if (pBorders.get(3).equals(cBorders.get(2))) {
            possible.add("left");
        }

        return possible;
    }

    private Tile getTileById(int id)
    {
        Optional<Tile> tile = tiles.stream().filter(t -> t.getId() == id).findFirst();
        if (tile.isEmpty()) {
            return null;
        }

        return tile.get();
    }

    private static void padTo(List<?> list, int size)
    {
        for (int i = list.size(); i < size; i++)
            list.add(null);
    }

    private static class Tile
    {
        private final int id;
        private final List<String> data;

        private boolean flip;
        private int rotate = 0;

        public Tile(int id, List<String> data)
        {
            super();
            this.id = id;
            this.data = data;
        }

        public int getId()
        {
            return id;
        }

        public List<String> getData()
        {
            List<String> tmp = new ArrayList<>(data);

            int rotateSteps = rotate / 90;
            for (int i = 0; i < rotateSteps; i++) {
                tmp = rotate(tmp);
            }

            if (flip) {
                List<String> tmp2 = new ArrayList<>();
                for (var l : tmp) {
                    tmp2.add(new StringBuilder(l).reverse().toString());
                }

                return tmp2;
            }

            return tmp;
        }

        public void setFlip(boolean flip)
        {
            this.flip = flip;
        }

        public void setRotate(int rotate)
        {
            this.rotate = rotate;
        }

        public List<String> getBorders()
        {
            List<String> borders = new ArrayList<>(4);

            var dataTmp = getData();

            borders.add(dataTmp.get(0));
            borders.add(dataTmp.get(data.size() - 1));
            final StringBuilder rightSb = new StringBuilder();
            dataTmp.forEach(row -> rightSb.append(row.charAt(row.length() - 1)));
            borders.add(rightSb.toString());
            final StringBuilder leftSb = new StringBuilder();
            dataTmp.forEach(row -> leftSb.append(row.charAt(0)));
            borders.add(leftSb.toString());

            return borders;
        }

        private static List<String> rotate(List<String> input)
        {
            List<String> data2 = new ArrayList<>(input);
            Collections.reverse(data2);

            ArrayList<String> tmp = new ArrayList<>();
            for (int i = 0; i < input.get(0).length(); i++) {
                StringBuilder sb = new StringBuilder();
                for (var row : data2) {
                    sb.append(row.charAt(i));
                }
                tmp.add(sb.toString());
            }

            return tmp;
        }

        @Override
        public String toString()
        {
            final StringBuilder builder = new StringBuilder();
            builder.append("Tile ").append(id).append("\n");
            getData().forEach(l -> builder.append(l).append('\n'));
            return builder.toString();
        }
    }
}
