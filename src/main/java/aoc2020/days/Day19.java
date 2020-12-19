package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19
{
	private final List<String> messages = new ArrayList<>();
	private final Map<Integer, String> rules = new HashMap<>();

	public Day19() throws URISyntaxException, IOException
	{
		Path path = Paths.get(getClass().getClassLoader().getResource("day19.txt").toURI());
		var lines = Files.lines(path);
		List<String> input = lines.collect(Collectors.toList());
		lines.close();

		boolean parsingRules = true;
		for (var l : input) {
			if (l.isBlank()) {
				parsingRules = false;
				continue;
			}

			if (parsingRules) {
				String[] split = l.split(": ");
				rules.put(Integer.valueOf(split[0]), split[1]);
			} else {
				messages.add(l);
			}
		}
	}

	public long getAmountOfMessagesAdheringToRulePart1(int rule)
	{
		return messages.stream().filter(m -> validateMessage(m, 0, Integer.valueOf(rule))).count();
	}

	public long getAmountOfMessagesAdheringToRulePart2()
	{
		List<String> possibilities42 = possibilities(Integer.valueOf(42));
		List<String> possibilities31 = possibilities(Integer.valueOf(31));

		long counter = 0L;
		for (var msg : messages) {
			// Should start with 2x '42'
			if (!possibilities42.contains(msg.substring(0, 8)) || !possibilities42.contains(msg.substring(8, 16))) {
				continue;
			}
			// Should end with '31'
			if (!possibilities31.contains(msg.substring(msg.length() - 8))) {
				continue;
			}

			// Should contain only '42' and then '31'. But more times '42'.
			int count42 = 0;
			int count31 = 0;
			String subStr = msg.substring(16, msg.length() - 8);
			while (subStr.length() > 0 && possibilities42.contains(subStr.substring(0, 8))) {
				subStr = subStr.substring(8);
				count42++;
			}
			while (subStr.length() > 0 && possibilities31.contains(subStr.substring(0, 8))) {
				subStr = subStr.substring(8);
				count31++;
			}
			if (subStr.length() > 0 || count31 > count42) {
				continue;
			}

			counter++;
		}

		return counter;
	}

	private boolean validateMessage(String msg, int pos, Integer ruleIdx)
	{
		int ret = checkNextRule(msg, pos, ruleIdx);
		if (ret == msg.length()) {
			return true;
		}

		return false;
	}

	private int checkNextRule(String msg, int pos, Integer ruleIdx)
	{
		String rule = rules.get(ruleIdx);

		if (pos > msg.length() - 1) {
			return 0;
		}

		String charAtPos = String.valueOf(msg.charAt(pos));

		if (rule.startsWith("\"")) {
			// Literal
			if (charAtPos.equals(rule.substring(1, 2))) {
				return 1;
			} else {
				return 0;
			}
		} else if (rule.contains("|")) {
			String[] factors = rule.split(Pattern.quote(" | "));
			for (var f : factors) {
				boolean allValid = true;
				String[] ruleIndicesToCheck = f.split(" ");
				int posOffset = 0;
				for (int i = 0; i < ruleIndicesToCheck.length; i++) {
					int posOffsetRule = checkNextRule(msg, pos + posOffset, Integer.valueOf(ruleIndicesToCheck[i]));
					if (posOffsetRule == 0) {
						allValid = false;
						break;
					}

					posOffset += posOffsetRule;
				}

				if (allValid == true) {
					return posOffset;
				}
			}
		} else {
			String[] ruleIndicesToCheck = rule.split(" ");
			boolean allValid = true;
			int posOffset = 0;
			for (int i = 0; i < ruleIndicesToCheck.length; i++) {
				int posOffsetRule = checkNextRule(msg, pos + posOffset, Integer.valueOf(ruleIndicesToCheck[i]));
				if (posOffsetRule == 0) {
					allValid = false;
					break;
				}

				posOffset += posOffsetRule;
			}

			if (allValid == true) {
				return posOffset;
			}
		}

		return 0;
	}

	private List<String> possibilities(Integer ruleIdx)
	{
		List<String> possibilities = new ArrayList<>();

		String rule = rules.get(ruleIdx);
		if (rule.startsWith("\"")) {
			possibilities.add(rule.substring(1, 2));
			return possibilities;
		}

		for (var t : rule.split(Pattern.quote(" | "))) {
			List<List<String>> children = new ArrayList<>();
			for (var f : t.split(" ")) {
				children.add(possibilities(Integer.valueOf(f)));
			}

			if (children.size() == 1) {
				possibilities.addAll(children.get(0));
			} else if (children.size() == 2) {
				for (var left : children.get(0)) {
					for (var right : children.get(1)) {
						possibilities.add(left + right);
					}
				}
			}
		}

		return possibilities;
	}
}
