package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11
{
	private final static int MAX_LOOPS = 9999;

	private final List<List<Position>> seatingArea = new ArrayList<>();

	public Day11() throws URISyntaxException, IOException
	{
		Path path = Paths.get(getClass().getClassLoader().getResource("day11.txt").toURI());

		Stream<String> lines = Files.lines(path);
		List<String> linesList = lines.collect(Collectors.toList());
		lines.close();

		for (var l : linesList) {
			List<Position> currentLine = new ArrayList<>();
			for (var c : l.toCharArray()) {
				currentLine.add(Position.createFromString(String.valueOf(c)));
			}

			seatingArea.add(currentLine);
		}
	}

	public long getOccupiedSeatsAfterStabilizationAdjacentRules()
	{
		List<List<Position>> currentArea = createDeepCopy(seatingArea);

		for (int i = 0; i < MAX_LOOPS; i++) {
			List<List<Position>> newArea = applyAdjacentSeatingRules(currentArea);
			if (areasAreEqual(currentArea, newArea)) {
				return getAmountOfOccupiedSeats(newArea);
			}

			currentArea = newArea;
		}

		return 0L;
	}

	public long getOccupiedSeatsAfterStabilizationLineOfSightRules()
	{
		List<List<Position>> currentArea = createDeepCopy(seatingArea);

		for (int i = 0; i < MAX_LOOPS; i++) {
			List<List<Position>> newArea = applyLineOfSightSeatingRules(currentArea);
			if (areasAreEqual(currentArea, newArea)) {
				return getAmountOfOccupiedSeats(newArea);
			}

			currentArea = newArea;
		}

		return 0L;
	}

	private List<List<Position>> createDeepCopy(List<List<Position>> source)
	{
		List<List<Position>> destination = new ArrayList<>();
		for (var row : source) {
			List<Position> newRow = new ArrayList<>();
			for (var pos : row) {
				newRow.add(new Position(pos.getType()));
			}
			destination.add(newRow);
		}

		return destination;
	}

	// Assume both areas have equal dimensions and each row has the same length
	private boolean areasAreEqual(List<List<Position>> currentArea, List<List<Position>> newArea)
	{
		int maxRows = currentArea.size();
		int maxCols = currentArea.get(0).size();

		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < maxCols; j++) {
				if (currentArea.get(i).get(j).getType() != newArea.get(i).get(j).getType()) {
					return false;
				}
			}
		}

		return true;
	}

	private static List<List<Position>> applyAdjacentSeatingRules(List<List<Position>> currentArea)
	{
		List<List<Position>> newArea = new ArrayList<>();

		for (int i = 0; i < currentArea.size(); i++) {
			List<Position> curRow = currentArea.get(i);
			List<Position> newRow = new ArrayList<>();
			for (int j = 0; j < curRow.size(); j++) {
				Position curPos = curRow.get(j);
				if (curPos.getType() == PositionType.SeatEmpty
						&& getNumberOfAdjacentSeatsOccupied(i, j, currentArea) == 0) {
					newRow.add(new Position(PositionType.SeatOccupied));
				} else if (curPos.getType() == PositionType.SeatOccupied
						&& getNumberOfAdjacentSeatsOccupied(i, j, currentArea) >= 4) {
					newRow.add(new Position(PositionType.SeatEmpty));
				} else {
					newRow.add(new Position(curPos.getType()));
				}
			}

			newArea.add(newRow);
		}

		return newArea;

	}

	private static int getNumberOfAdjacentSeatsOccupied(int row, int pos, List<List<Position>> area)
	{
		int seatsOccupied = 0;

		int minRow = Math.max(row - 1, 0);
		int maxRow = Math.min(row + 2, area.size());
		int minPos = Math.max(pos - 1, 0);
		int maxPos = Math.min(pos + 2, area.get(0).size());

		for (int i = minRow; i < maxRow; i++) {
			for (int j = minPos; j < maxPos; j++) {
				if (i == row && j == pos) {
					continue;
				}

				if (area.get(i).get(j).getType() == PositionType.SeatOccupied) {
					seatsOccupied++;
				}
			}
		}

		return seatsOccupied;
	}

	private static List<List<Position>> applyLineOfSightSeatingRules(List<List<Position>> currentArea)
	{
		List<List<Position>> newArea = new ArrayList<>();

		for (int i = 0; i < currentArea.size(); i++) {
			List<Position> curRow = currentArea.get(i);
			List<Position> newRow = new ArrayList<>();
			for (int j = 0; j < curRow.size(); j++) {
				Position curPos = curRow.get(j);
				if (curPos.getType() == PositionType.SeatEmpty
						&& getNumberOfLineOfSightSeatsOccupied(i, j, currentArea) == 0) {
					newRow.add(new Position(PositionType.SeatOccupied));
				} else if (curPos.getType() == PositionType.SeatOccupied
						&& getNumberOfLineOfSightSeatsOccupied(i, j, currentArea) >= 5) {
					newRow.add(new Position(PositionType.SeatEmpty));
				} else {
					newRow.add(new Position(curPos.getType()));
				}
			}

			newArea.add(newRow);
		}

		return newArea;

	}

	private static int getNumberOfLineOfSightSeatsOccupied(int row, int pos, List<List<Position>> area)
	{
		int seatsOccupied = 0;

		// X (pos) and Y (row) directions
		int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };

		int minX = 0;
		int maxX = area.get(0).size() - 1;
		int minY = 0;
		int maxY = area.size() - 1;

		for (var d : directions) {
			int newRow = row;
			int newPos = pos;

			do {
				newRow += d[1];
				newPos += d[0];

				if (newRow < minY || newRow > maxY || newPos < minX || newPos > maxX) {
					break;
				}

				if (area.get(newRow).get(newPos).getType() == PositionType.SeatEmpty) {
					break;
				}

				if (area.get(newRow).get(newPos).getType() == PositionType.SeatOccupied) {
					seatsOccupied++;
					break;
				}
			} while (true);
		}

		return seatsOccupied;
	}

	private static long getAmountOfOccupiedSeats(List<List<Position>> area)
	{
		long counter = 0L;

		for (var row : area) {
			for (var pos : row) {
				if (pos.getType() == PositionType.SeatOccupied) {
					counter++;
				}
			}
		}

		return counter;
	}

	private static class Position
	{
		private final PositionType type;

		public Position(PositionType type)
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
			return type.toString();
		}

		public static Position createFromString(String input)
		{
			if (input.equals("."))
				return new Position(PositionType.Floor);
			else if (input.equals("L"))
				return new Position(PositionType.SeatEmpty);
			else if (input.equals("#"))
				return new Position(PositionType.SeatOccupied);

			throw new IllegalArgumentException("Can't handle input: " + input);
		}
	}

	enum PositionType
	{
		Floor, SeatEmpty, SeatOccupied
	}
}
