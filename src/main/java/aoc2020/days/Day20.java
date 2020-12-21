package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20
{
	private static final int FLIP_ROTATE_POSSIBILITIES[][] = { { 0, 0 }, { 90, 0 }, { 180, 0 }, { 270, 0 }, { 0, 1 },
			{ 90, 1 }, { 180, 1 }, { 270, 1 } };
	private static final int ROTATE_POSSIBILITIES[] = { 0, 90, 180, 270 };

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

		Collections.sort(cornerTiles, new Comparator<Tile>() {
			@Override
			public int compare(Tile t1, Tile t2)
			{
				if (t1.getId() > t2.getId()) {
					return 1;
				}

				return -1;
			}
		});

		int rowLength = (int) Math.sqrt(tiles.size());
		System.out.println("Amount of tiles: " + tiles.size() + ", row length: " + rowLength);
		System.out.println("Corner tiles:");
		cornerTiles.forEach(t -> System.out.println(t.getId() + ", borders: " + t.getBorders()));

		Tile topLeft = null;
		for (var t1 : cornerTiles) {
//			if (t1.getId() != 1951) {
//				continue;
//			}
			//System.out.println(t1);
			boolean done = false;
			for (var rf1 : FLIP_ROTATE_POSSIBILITIES) {
				t1.setRotate(rf1[0]);
				t1.setFlip(rf1[1] == 1);
				System.out.println(t1);

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

				//System.out.println(sidesMatch);
				if (sidesMatch.size() >= 2) {
					if (sidesMatch.contains("below") && sidesMatch.contains("right")) {
						topLeft = t1;
						done = true;

						break;
					}
				}
			}
			if (done) {
				break;
			}
		}

		System.out.println("top left:\n" + topLeft + "\n");

		remaining.remove(topLeft);

		List<List<Integer>> jigsaw = new ArrayList<>(rowLength);
		for (int i = 0; i < rowLength; i++) {
			List<Integer> row = new ArrayList<>(rowLength);
			padTo(row, rowLength);
			jigsaw.add(row);
		}

		jigsaw.get(0).set(0, topLeft.getId());

		// First row
		for (int i = 1; i < jigsaw.get(0).size(); i++) {
			Tile usingTile = null;
			for (var tOther : remaining) {
				for (var rf : FLIP_ROTATE_POSSIBILITIES) {
					tOther.setRotate(rf[0]);
					tOther.setFlip(rf[1] == 1);

					int surroundingTilesFit = surroundingTilesFit(0, i, null, tOther, jigsaw);
					//System.out.println(tOther.getId() + " fits: " + surroundingTilesFit);
					if (surroundingTilesFit == 1) {
						jigsaw.get(0).set(i, tOther.getId());
						usingTile = tOther;
						break;
					}
				}
				if (usingTile != null) {
					break;
				}
			}

			if (usingTile != null) {
				System.out.println(usingTile);
				remaining.remove(usingTile);
				usingTile = null;
			}
		}

		System.out.println("\nFinal jigsaw:\n");
		jigsaw.forEach(System.out::println);

		long counter = 0L;

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

	private int surroundingTilesFit(int posY, int posX, String side, Tile candidateTile, List<List<Integer>> jigsaw)
	{
		int matches = 0;

		// Above
		try {
			List<Integer> row = jigsaw.get(posY + 1);
			Integer tileIdToCheck = row.get(posX);
			if (tileIdToCheck != null) {
				Tile tileToCheck = getTileById(tileIdToCheck);
				if (tilesFit(candidateTile, tileToCheck).contains("above")) {
					matches++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		// Below
		try {
			List<Integer> row = jigsaw.get(posY - 1);
			Integer tileIdToCheck = row.get(posX);
			if (tileIdToCheck != null) {
				Tile tileToCheck = getTileById(tileIdToCheck);
				if (tilesFit(candidateTile, tileToCheck).contains("below")) {
					matches++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		// Right
		try {
			List<Integer> row = jigsaw.get(posY);
			Integer tileIdToCheck = row.get(posX + 1);
			if (tileIdToCheck != null) {
				Tile tileToCheck = getTileById(tileIdToCheck);
				if (tilesFit(candidateTile, tileToCheck).contains("right")) {
					matches++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		// Left
		try {
			List<Integer> row = jigsaw.get(posY);
			Integer tileIdToCheck = row.get(posX - 1);
			if (tileIdToCheck != null) {
				Tile tileToCheck = getTileById(tileIdToCheck);
				if (tilesFit(candidateTile, tileToCheck).contains("left")) {
					matches++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
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
			ArrayList<String> tmp = new ArrayList<>();
			for (int i = 0; i < input.size(); i++) {
				StringBuilder sb = new StringBuilder();
				List<String> data2 = new ArrayList<>(input);
				Collections.reverse(data2);
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
