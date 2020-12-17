package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17
{
    private static final int[] NEIGHBOUR_INDICES = { -1, 0, 1 };

    private final List<List<Cube>> initialStateInput = new ArrayList<>();

    public Day17() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day17.txt").toURI());

        var lines = Files.lines(path);
        var linesList = lines.collect(Collectors.toList());
        lines.close();

        for (var l : linesList) {
            List<Cube> currentLine = new ArrayList<>();
            for (var c : l.toCharArray()) {
                currentLine.add(Cube.createFromString(String.valueOf(c)));
            }

            initialStateInput.add(currentLine);
        }
    }

    public long getActiveCubesAfterCycles3D(int cycles)
    {
        var initialState = createDeepCopy2D(initialStateInput);
        int gridSize = initialState.size() + (cycles * 2);
        int initialStateOffset = (gridSize - initialState.size()) / 2;
        Cube[][][] grid = new Cube[gridSize][gridSize][gridSize];
        for (int z = 0; z < gridSize; z++) {
            for (int y = 0; y < gridSize; y++) {
                for (int x = 0; x < gridSize; x++) {
                    grid[z][y][x] = new Cube();
                }
            }
        }

        for (int y = 0; y < initialState.size(); y++) {
            for (int x = 0; x < initialState.size(); x++) {
                grid[grid.length / 2 - initialState.size() / 2][initialStateOffset + y][initialStateOffset
                        + x] = initialState.get(y).get(x);
            }
        }

        for (int cycle = 0; cycle < cycles; cycle++) {
            Cube[][][] copy = new Cube[gridSize][gridSize][gridSize];
            for (int z = 0; z < grid.length; z++) {
                for (int y = 0; y < grid.length; y++) {
                    for (int x = 0; x < grid.length; x++) {
                        int activeNeighbors = getNumberOfActiveCubesAroundPosition3D(z, y, x, grid);
                        if (grid[z][y][x].getState() == CubeState.Active
                                && (activeNeighbors == 2 || activeNeighbors == 3)) {
                            copy[z][y][x] = new Cube(CubeState.Active);
                        } else if (grid[z][y][x].getState() == CubeState.Inactive && activeNeighbors == 3) {
                            copy[z][y][x] = new Cube(CubeState.Active);

                        } else {
                            copy[z][y][x] = new Cube();
                        }
                    }
                }
            }

            grid = copy;
        }

        long active = 0L;
        for (int z = 0; z < gridSize; z++) {
            for (int y = 0; y < gridSize; y++) {
                for (int x = 0; x < gridSize; x++) {
                    if (grid[z][y][x].getState() == CubeState.Active) {
                        active++;
                    }
                }
            }
        }

        return active;
    }

    public long getActiveCubesAfterCycles4D(int cycles)
    {
        var initialState = createDeepCopy2D(initialStateInput);
        int gridSize = initialState.size() + (cycles * 2);
        int initialStateOffset = (gridSize - initialState.size()) / 2;
        Cube[][][][] grid = new Cube[gridSize][gridSize][gridSize][gridSize];
        for (int w = 0; w < gridSize; w++) {
            for (int z = 0; z < gridSize; z++) {
                for (int y = 0; y < gridSize; y++) {
                    for (int x = 0; x < gridSize; x++) {
                        grid[w][z][y][x] = new Cube();
                    }
                }
            }
        }

        for (int z = 0; z < gridSize; z++) {
            for (int y = 0; y < initialState.size(); y++) {
                for (int x = 0; x < initialState.size(); x++) {
                    grid[grid.length / 2 - initialState.size() / 2][grid.length / 2
                            - initialState.size() / 2][initialStateOffset + y][initialStateOffset + x] = initialState
                                    .get(y).get(x);
                }
            }
        }

        for (int cycle = 0; cycle < cycles; cycle++) {
            Cube[][][][] copy = new Cube[gridSize][gridSize][gridSize][gridSize];
            for (int w = 0; w < grid.length; w++) {
                for (int z = 0; z < grid.length; z++) {
                    for (int y = 0; y < grid.length; y++) {
                        for (int x = 0; x < grid.length; x++) {
                            int activeNeighbors = getNumberOfActiveCubesAroundPosition4D(w, z, y, x, grid);
                            if (grid[w][z][y][x].getState() == CubeState.Active
                                    && (activeNeighbors == 2 || activeNeighbors == 3)) {
                                copy[w][z][y][x] = new Cube(CubeState.Active);
                            } else if (grid[w][z][y][x].getState() == CubeState.Inactive && activeNeighbors == 3) {
                                copy[w][z][y][x] = new Cube(CubeState.Active);
                            } else {
                                copy[w][z][y][x] = new Cube();
                            }
                        }
                    }
                }
            }

            grid = copy;
        }

        long active = 0L;
        for (int w = 0; w < gridSize; w++) {
            for (int z = 0; z < gridSize; z++) {
                for (int y = 0; y < gridSize; y++) {
                    for (int x = 0; x < gridSize; x++) {
                        if (grid[w][z][y][x].getState() == CubeState.Active) {
                            active++;
                        }
                    }
                }
            }
        }

        return active;
    }

    private List<List<Cube>> createDeepCopy2D(List<List<Cube>> source)
    {
        List<List<Cube>> destination = new ArrayList<>();
        for (var row : source) {
            List<Cube> newRow = new ArrayList<>();
            for (var pos : row) {
                newRow.add(new Cube(pos.getState()));
            }
            destination.add(newRow);
        }

        return destination;
    }

    private static int getNumberOfActiveCubesAroundPosition3D(int plane, int row, int pos, Cube[][][] grid)
    {
        int counter = 0;

        for (var dPlane : NEIGHBOUR_INDICES) {
            for (var dRow : NEIGHBOUR_INDICES) {
                for (var dPos : NEIGHBOUR_INDICES) {
                    if (plane + dPlane < 0 || row + dRow < 0 || pos + dPos < 0) {
                        continue;
                    }
                    if (plane + dPlane == grid.length || row + dRow == grid.length || pos + dPos == grid.length) {
                        continue;
                    }
                    if (dPlane == 0 && dRow == 0 && dPos == 0) {
                        continue;
                    }

                    if (grid[plane + dPlane][row + dRow][pos + dPos].getState() == CubeState.Active) {
                        counter++;
                    }
                }
            }
        }

        return counter;
    }

    private static int getNumberOfActiveCubesAroundPosition4D(int w, int plane, int row, int pos, Cube[][][][] grid)
    {
        int counter = 0;

        for (var dW : NEIGHBOUR_INDICES) {
            for (var dPlane : NEIGHBOUR_INDICES) {
                for (var dRow : NEIGHBOUR_INDICES) {
                    for (var dPos : NEIGHBOUR_INDICES) {
                        if (w + dW < 0 || plane + dPlane < 0 || row + dRow < 0 || pos + dPos < 0) {
                            continue;
                        }
                        if (w + dW == grid.length || plane + dPlane == grid.length || row + dRow == grid.length
                                || pos + dPos == grid.length) {
                            continue;
                        }
                        if (dW == 0 && dPlane == 0 && dRow == 0 && dPos == 0) {
                            continue;
                        }

                        if (grid[w + dW][plane + dPlane][row + dRow][pos + dPos].getState() == CubeState.Active) {
                            counter++;
                        }
                    }
                }
            }
        }

        return counter;
    }

    private static class Cube
    {
        private final CubeState state;

        public Cube()
        {
            super();
            this.state = CubeState.Inactive;
        }

        public Cube(CubeState type)
        {
            super();
            this.state = type;
        }

        public CubeState getState()
        {
            return state;
        }

        @Override
        public String toString()
        {
            return state.toString();
        }

        public static Cube createFromString(String input)
        {
            if (input.equals("."))
                return new Cube(CubeState.Inactive);
            else if (input.equals("#"))
                return new Cube(CubeState.Active);

            throw new IllegalArgumentException("Can't handle input: " + input);
        }
    }

    enum CubeState {
        Active,
        Inactive
    }
}
