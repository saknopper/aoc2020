package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5
{
	public long getHighestSeatID() throws URISyntaxException, IOException
	{
		final List<BoardingPass> passes = new ArrayList<>();

		Path path = Paths.get(getClass().getClassLoader().getResource("day5.txt").toURI());

		Stream<String> lines = Files.lines(path);
		lines.forEach(l -> passes.add(BoardingPass.createFromString(l)));
		lines.close();

		List<BoardingPass> sortedPasses = passes.stream()
				.sorted(Comparator.comparingInt(BoardingPass::getSeatID).reversed()).collect(Collectors.toList());

		return sortedPasses.get(0).getSeatID();
	}

	public long getMySeatID() throws URISyntaxException, IOException
	{
		final List<BoardingPass> passes = new ArrayList<>();

		Path path = Paths.get(getClass().getClassLoader().getResource("day5.txt").toURI());

		Stream<String> lines = Files.lines(path);
		lines.forEach(l -> passes.add(BoardingPass.createFromString(l)));
		lines.close();

		List<BoardingPass> sortedPasses = passes.stream().sorted(Comparator.comparingInt(BoardingPass::getSeatID))
				.collect(Collectors.toList());

		int lastSeatID = sortedPasses.get(0).getSeatID();
		for (var pass : sortedPasses) {
			if (pass.getSeatID() - lastSeatID > 1) {
				return pass.getSeatID() - 1;
			}

			lastSeatID = pass.getSeatID();
		}

		return 0;
	}

	private static class BoardingPass
	{
		private final int row;
		private final int column;

		public BoardingPass(int row, int column)
		{
			super();
			this.row = row;
			this.column = column;
		}

		public int getSeatID()
		{
			return (row * 8) + column;
		}

		public static BoardingPass createFromString(String pass)
		{
			String rowInput = pass.substring(0, 7);
			String columnInput = pass.substring(7);

			int row = getPosition(0, 127, rowInput);
			int column = getPosition(0, 7, columnInput);

			return new BoardingPass(row, column);
		}

		private static int getPosition(int lowerLimit, int upperLimit, String remainingInput)
		{
			int newLowerLimit = lowerLimit;
			int newUpperLimit = upperLimit;

			if (remainingInput.startsWith("F") || remainingInput.startsWith("L")) {
				newUpperLimit -= (upperLimit - lowerLimit + 1) / 2;
			} else if (remainingInput.startsWith("B") || remainingInput.startsWith("R")) {
				newLowerLimit += (upperLimit - lowerLimit + 1) / 2;
			}

			if (remainingInput.length() == 1) {
				return newLowerLimit;
			}

			return getPosition(newLowerLimit, newUpperLimit, remainingInput.substring(1));
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("BoardingPass [row=").append(row).append(", column=").append(column).append(", seatID=")
					.append(getSeatID()).append("]");
			return builder.toString();
		}
	}
}
