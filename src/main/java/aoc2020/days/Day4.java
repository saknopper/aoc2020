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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4
{
	private static final String[] REQUIRED_FIELDS = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl",
			"pid" /* , "cid" */ };
	private static final String[] VALID_ECL_VALUES = { "amb", "blu", "brn", "gry", "grn", "hzl", "oth" };

	public long getAmountOfValidPassports(boolean checkData) throws URISyntaxException, IOException
	{
		final List<Map<String, String>> passports = new ArrayList<>();

		Path path = Paths.get(getClass().getClassLoader().getResource("day4.txt").toURI());

		Map<String, String> currentPassword = new HashMap<>();
		Stream<String> lines = Files.lines(path);
		List<String> linesList = lines.collect(Collectors.toList());
		// Add empty line at the end to make sure the last passport is added to
		// the list
		linesList.add("");
		lines.close();

		for (String l : linesList) {
			if (l.isEmpty()) {
				passports.add(currentPassword);
				currentPassword = new HashMap<>();
				continue;
			}

			String[] fieldPairs = l.split(" ");
			for (String pair : fieldPairs) {
				String[] keyAndValue = pair.split(":");
				currentPassword.put(keyAndValue[0], keyAndValue[1]);
			}
		}

		if (checkData) {
			return passports.stream().filter(Day4::containsRequiredFieldsAndValidData).count();
		} else {
			return passports.stream().filter(Day4::containsRequiredFields).count();
		}
	}

	private static boolean containsRequiredFields(Map<String, String> passport)
	{
		for (String field : REQUIRED_FIELDS) {
			if (!passport.containsKey(field)) {
				return false;
			}
		}

		return true;
	}

	private static boolean containsRequiredFieldsAndValidData(Map<String, String> passport)
	{
		for (String field : REQUIRED_FIELDS) {
			if (!passport.containsKey(field)) {
				return false;
			}
		}

		String byr = passport.get("byr");
		if (!validNumber(byr, 4, 1920, 2002)) {
			return false;
		}

		String iyr = passport.get("iyr");
		if (!validNumber(iyr, 4, 2010, 2020)) {
			return false;
		}

		String eyr = passport.get("eyr");
		if (!validNumber(eyr, 4, 2020, 2030)) {
			return false;
		}

		String hgt = passport.get("hgt");
		try {
			int hgtInt = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
			if (hgt.endsWith("cm")) {
				if (hgtInt < 150 || hgtInt > 193) {
					return false;
				}
			} else if (hgt.endsWith("in")) {
				if (hgtInt < 59 || hgtInt > 76) {
					return false;
				}
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

		String hcl = passport.get("hcl");
		if (!hcl.startsWith("#") || hcl.length() != 7) {
			return false;
		}
		try {
			Integer.parseInt(hcl.substring(1), 16);
		} catch (NumberFormatException e) {
			return false;
		}

		String ecl = passport.get("ecl");
		if (!List.of(VALID_ECL_VALUES).contains(ecl)) {
			return false;
		}

		String pid = passport.get("pid");
		if (!pid.matches("^(\\d){9}$")) {
			return false;
		}

		return true;
	}

	private static boolean validNumber(String number, int digits, int min, int max)
	{
		if (number.length() > digits) {
			return false;
		}

		int numberAsInt = Integer.parseInt(number);
		if (numberAsInt < min || numberAsInt > max) {
			return false;
		}

		return true;
	}
}
