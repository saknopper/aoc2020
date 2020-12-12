package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day12
{
	private final List<Instruction> navigationInstructions = new ArrayList<>();

	public Day12() throws URISyntaxException, IOException
	{
		Path path = Paths.get(getClass().getClassLoader().getResource("day12.txt").toURI());

		var lines = Files.lines(path);
		lines.forEach(l -> navigationInstructions.add(Instruction.createFromString(l)));
		lines.close();
	}

	public int getManhattanDistance()
	{
		Ship s = new Ship(null);

		navigationInstructions.forEach(i -> applyInstruction(i, s));

		return Math.abs(s.getPosX()) + Math.abs(s.getPosY());
	}

	public int getManhattanDistanceWithWaypoint()
	{
		Ship s = new Ship(new Waypoint(10, 1));

		navigationInstructions.forEach(i -> applyInstruction(i, s));

		return Math.abs(s.getPosX()) + Math.abs(s.getPosY());
	}

	private void applyInstruction(Instruction i, Ship s)
	{
		boolean hasWaypoint = s.getWaypoint() != null;

		switch (i.getAction()) {
			case North:
				if (hasWaypoint) {
					s.getWaypoint().move(0, i.getArgument());
				} else {
					s.move(0, i.getArgument());
				}
				break;
			case East:
				if (hasWaypoint) {
					s.getWaypoint().move(i.getArgument(), 0);
				} else {
					s.move(i.getArgument(), 0);
				}
				break;
			case South:
				if (hasWaypoint) {
					s.getWaypoint().move(0, -i.getArgument());
				} else {
					s.move(0, -i.getArgument());
				}
				break;
			case West:
				if (hasWaypoint) {
					s.getWaypoint().move(-i.getArgument(), 0);
				} else {
					s.move(-i.getArgument(), 0);
				}
				break;
			case Left:
				if (hasWaypoint) {
					s.getWaypoint().rotate(-(i.getArgument() / 90));
				} else {
					s.setCurrentDirection(-(i.getArgument() / 90));
				}
				break;
			case Right:
				if (hasWaypoint) {
					s.getWaypoint().rotate(i.getArgument() / 90);
				} else {
					s.setCurrentDirection(i.getArgument() / 90);
				}
				break;
			case Forward:
				if (hasWaypoint) {
					s.move(s.getWaypoint().getPosX() * i.getArgument(), s.getWaypoint().getPosY() * i.getArgument());
				} else {
					switch (s.getCurrentDirection()) {
						case North:
							s.move(0, i.getArgument());
							break;
						case East:
							s.move(i.getArgument(), 0);
							break;
						case South:
							s.move(0, -i.getArgument());
							break;
						case West:
							s.move(-i.getArgument(), 0);
							break;
					}
				}
				break;
			default:
				break;
		}
	}

	private static class Ship
	{
		private static final Direction[] directions = { Direction.North, Direction.East, Direction.South,
				Direction.West };

		private final Waypoint waypoint;

		private int currentDirection = 1; // East

		private int posX = 0;
		private int posY = 0;

		public Ship(Waypoint waypoint)
		{
			super();
			this.waypoint = waypoint;
		}

		public Direction getCurrentDirection()
		{
			return directions[currentDirection];
		}

		/*
		 * @steps Steps of 90 degrees, positive is clock-wise, negative is
		 * counter-clock-wise
		 */
		public void setCurrentDirection(int steps)
		{
			currentDirection = ((currentDirection + steps) % directions.length + directions.length) % directions.length;
		}

		public int getPosX()
		{
			return posX;
		}

		public int getPosY()
		{
			return posY;
		}

		public void move(int x, int y)
		{
			posX += x;
			posY += y;
		}

		public Waypoint getWaypoint()
		{
			return waypoint;
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Ship [waypoint=").append(waypoint).append(", currentDirection=").append(currentDirection)
					.append(", posX=").append(posX).append(", posY=").append(posY).append("]");
			return builder.toString();
		}
	}

	private static class Waypoint
	{
		private int posX = 0;
		private int posY = 0;

		public Waypoint(int posX, int posY)
		{
			super();
			this.posX = posX;
			this.posY = posY;
		}

		public int getPosX()
		{
			return posX;
		}

		public int getPosY()
		{
			return posY;
		}

		public void move(int x, int y)
		{
			posX += x;
			posY += y;
		}

		/*
		 * @steps Steps of 90 degrees, positive is clock-wise, negative is
		 * counter-clock-wise
		 */
		public void rotate(int steps)
		{
			if (steps > 0) {
				for (int i = steps; i > 0; i--) {
					int oldX = posX;
					posX = posY;
					posY = -oldX;
				}
			} else if (steps < 0) {
				for (int i = steps; i < 0; i++) {
					int oldX = posX;
					posX = -posY;
					posY = oldX;
				}
			}
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Waypoint [posX=").append(posX).append(", posY=").append(posY).append("]");
			return builder.toString();
		}
	}

	private static class Instruction
	{
		private final Action action;
		private final Integer argument;

		public Instruction(Action action, Integer argument)
		{
			super();
			this.action = action;
			this.argument = argument;
		}

		public Action getAction()
		{
			return action;
		}

		public Integer getArgument()
		{
			return argument;
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Instruction [action=").append(action).append(", argument=").append(argument).append("]");
			return builder.toString();
		}

		public static Instruction createFromString(String input)
		{
			String actionStr = input.substring(0, 1);
			String argumentStr = input.substring(1);

			Action action = null;
			switch (actionStr) {
				case "N":
					action = Action.North;
					break;
				case "E":
					action = Action.East;
					break;
				case "S":
					action = Action.South;
					break;
				case "W":
					action = Action.West;
					break;
				case "L":
					action = Action.Left;
					break;
				case "R":
					action = Action.Right;
					break;
				case "F":
					action = Action.Forward;
					break;
				default:
					throw new IllegalArgumentException("Can't parse action from: " + input);
			}

			return new Instruction(action, Integer.valueOf(argumentStr));
		}
	}

	enum Direction
	{
		North, East, South, West
	}

	enum Action
	{
		North, South, East, West, Left, Right, Forward
	}
}
