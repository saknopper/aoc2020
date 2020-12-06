package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6
{
	private static final Logger log = Logger.getLogger(Day6.class.getCanonicalName());

	public int getSumOfUniqueQuestionsAnsweredYesPerGroup() throws URISyntaxException, IOException
	{
		final List<Set<String>> questionsAnsweredYesPerGroup = new ArrayList<>();

		Path path = Paths.get(getClass().getClassLoader().getResource("day6.txt").toURI());

		Stream<String> lines = Files.lines(path);
		List<String> linesList = lines.collect(Collectors.toList());
		// Add empty line at the end to make sure the last group is added to
		// the list
		linesList.add("");
		lines.close();

		Set<String> currentGroup = new HashSet<String>();
		for (var l : linesList) {
			if (l.isEmpty()) {
				questionsAnsweredYesPerGroup.add(currentGroup);
				currentGroup = new HashSet<>();
				continue;
			}

			for (var c : l.toCharArray()) {
				currentGroup.add(String.valueOf(c));
			}
		}

		return questionsAnsweredYesPerGroup.stream().map(group -> group.size()).reduce(0, Integer::sum);
	}

	public int getSumOfQuestionsAnsweredYesByEveryonePerGroup() throws URISyntaxException, IOException
	{
		final List<Group> groups = new ArrayList<>();

		Path path = Paths.get(getClass().getClassLoader().getResource("day6.txt").toURI());

		Stream<String> lines = Files.lines(path);
		List<String> linesList = lines.collect(Collectors.toList());
		// Add empty line at the end to make sure the last group is added to
		// the list
		linesList.add("");
		lines.close();

		Group currentGroup = new Group();
		for (var l : linesList) {
			if (l.isEmpty()) {
				groups.add(currentGroup);
				currentGroup = new Group();
				continue;
			}

			currentGroup.addMember();
			for (var c : l.toCharArray()) {
				currentGroup.addAnswer(String.valueOf(c));
			}
		}

		return groups.stream().map(group -> group.getNumberOfQuestionsAnsweredYesByEveryone()).reduce(0, Integer::sum);
	}

	private static class Group
	{
		private int amountOfMembers = 0;
		Map<String, Integer> questionsAnsweredYesWithCounter = new TreeMap<>();

		private void addMember()
		{
			this.amountOfMembers++;
		}

		public void addAnswer(String character)
		{
			if (!questionsAnsweredYesWithCounter.containsKey(character)) {
				questionsAnsweredYesWithCounter.put(character, 1);
			} else {
				questionsAnsweredYesWithCounter.compute(character, (theCharacter, counter) -> counter + 1);
			}
		}

		public int getNumberOfQuestionsAnsweredYesByEveryone()
		{
			int counter = 0;
			for (var c : questionsAnsweredYesWithCounter.values()) {
				if (c.equals(amountOfMembers)) {
					counter++;
				}
			}

			return counter;
		}
	}
}
