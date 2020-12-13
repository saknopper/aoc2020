package aoc2020.days;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day13
{
	private final List<Bus> busses = new ArrayList<>();
	private final int desiredDepartureTimePartA;

	public Day13() throws URISyntaxException, IOException
	{
		Path path = Paths.get(getClass().getClassLoader().getResource("day13.txt").toURI());

		var lines = Files.lines(path);
		List<String> list = lines.collect(Collectors.toList());
		lines.close();

		desiredDepartureTimePartA = Integer.parseInt(list.get(0));

		int offset = 0;
		for (var bus : list.get(1).split(",")) {
			if (!bus.equals("x")) {
				busses.add(new Bus(Integer.parseInt(bus), offset));
			}

			offset++;
		}
	}

	public long getEarliestBusIdMultipliedByNumerOfMinutesToWait()
	{
		Map<Long, Bus> minutesToWaitPerBus = getMinutesToWaitPerBus(desiredDepartureTimePartA);
		Entry<Long, Bus> earliest = minutesToWaitPerBus.entrySet().stream().findFirst().get();

		return earliest.getKey() * earliest.getValue().getId();
	}

	public long findDepartureTimeLeadingToSubsequentDepartures()
	{
		long departureTime = 0L;
		long currentLCM = 1L;

		for (var b : busses) {
			for (long time = departureTime;; time += currentLCM) {
				if ((time + b.getOffset()) % b.getId() == 0L) {
					departureTime = time;
					break;
				}
			}

			currentLCM = calculateLCM(BigInteger.valueOf(currentLCM), BigInteger.valueOf(b.getId()));
		}

		return departureTime;
	}

	private Map<Long, Bus> getMinutesToWaitPerBus(int desiredDeparture)
	{
		Map<Long, Bus> waitingTimes = new TreeMap<>();

		for (var b : busses) {
			waitingTimes.put(b.getEarliestPossibleBusDepartureFor(desiredDeparture) - desiredDeparture, b);
		}

		return waitingTimes;
	}

	private static class Bus
	{
		private final int id;
		private final int offset;

		public Bus(int id, int offset)
		{
			super();
			this.id = id;
			this.offset = offset;
		}

		public int getId()
		{
			return id;
		}

		public int getOffset()
		{
			return offset;
		}

		public long getEarliestPossibleBusDepartureFor(long desiredDeparture)
		{
			return ((desiredDeparture / id) + 1) * id;
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("Bus [id=").append(id).append(", offset=").append(offset).append("]");
			return builder.toString();
		}
	}

	public static long calculateLCM(BigInteger number1, BigInteger number2)
	{
		BigInteger gcd = number1.gcd(number2);
		BigInteger absProduct = number1.multiply(number2).abs();

		return absProduct.divide(gcd).longValue();
	}
}
