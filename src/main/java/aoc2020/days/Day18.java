package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day18
{
	private final List<String> homework = new ArrayList<>();

	public Day18() throws URISyntaxException, IOException
	{
		Path path = Paths.get(getClass().getClassLoader().getResource("day18.txt").toURI());

		var lines = Files.lines(path);
		lines.forEach(homework::add);
		lines.close();
	}

	public long evaluateAndGetSum(boolean additionBeforeMultiplication)
	{
		return homework.stream().map(l -> evaluate(l, additionBeforeMultiplication))
				.collect(Collectors.summingDouble(Double::valueOf)).longValue();
	}

	private static double evaluate(final String str, boolean additionBeforeMultiplication)
	{
		return new Object() {
			int pos = -1, ch;

			void nextChar()
			{
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat)
			{
				while (ch == ' ')
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse()
			{
				nextChar();
				double x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException("Unexpected: " + (char) ch);
				return x;
			}

			double parseExpression()
			{
				double x = parseTerm();
				for (;;) {
					if (!additionBeforeMultiplication) {
						return x;
					}

					if (eat('*'))
						x *= parseTerm(); // multiplication
					else if (eat('/'))
						x /= parseTerm(); // division
					else
						return x;
				}
			}

			double parseTerm()
			{
				double x = parseFactor();
				for (;;) {
					if (additionBeforeMultiplication) {
						if (eat('+'))
							x += parseFactor(); // addition
						else if (eat('-'))
							x -= parseFactor(); // subtraction
						else
							return x;

						continue;
					}

					if (eat('*'))
						x *= parseFactor(); // multiplication
					else if (eat('/'))
						x /= parseFactor(); // division
					else if (eat('+'))
						x += parseFactor(); // addition
					else if (eat('-'))
						x -= parseFactor(); // subtraction
					else
						return x;
				}
			}

			double parseFactor()
			{
				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}

				return x;
			}
		}.parse();
	}
}