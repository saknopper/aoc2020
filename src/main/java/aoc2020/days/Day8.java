package aoc2020.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Day8
{
    private static final Logger log = Logger.getLogger(Day8.class.getCanonicalName());

    private final List<ProgramLine> programLines = new ArrayList<>();

    private int accumulator = 0;

    public Day8() throws URISyntaxException, IOException
    {
        Path path = Paths.get(getClass().getClassLoader().getResource("day8.txt").toURI());

        Stream<String> lines = Files.lines(path);
        lines.forEach(l -> programLines.add(ProgramLine.createFromString(l)));
        lines.close();
    }

    public int getAccValueBeforeEnteringLoop()
    {
        try {
            executeProgram(1);
        } catch (IllegalStateException e) {
            return accumulator;
        }

        return 0;
    }

    public int fixProgramToMakeItTerminateAndGetAccValue()
    {
        for (int line = programLines.size() - 1; line > 0; line--) {
            ProgramLine pl = programLines.get(line);
            if (pl.getOperation() == Operation.NOP || pl.getOperation() == Operation.JMP) {
                ProgramLine oldPl = pl;

                Operation newOperation = pl.getOperation() == Operation.NOP ? Operation.JMP : Operation.NOP;
                programLines.set(line, new ProgramLine(newOperation, pl.getArgument()));

                programLines.forEach(ProgramLine::resetTimesEntered);
                try {
                    return executeProgram(1);
                } catch (IllegalStateException e) {
                    /* ignore, continue with next line */
                }

                programLines.set(line, oldPl);
            }
        }

        return 0;
    }

    private int executeProgram(int maxLoops)
    {
        accumulator = 0;
        for (int l = 0; l < programLines.size();) {
            l = executeProgramLine(l, maxLoops);
        }

        return accumulator;
    }

    private int executeProgramLine(int line, int maxTimesEntered)
    {
        ProgramLine pl = programLines.get(line);
        pl.increaseTimesEntered();
        if (pl.getTimesEntered() > maxTimesEntered) {
            throw new IllegalStateException("Potential infinite loop...");
        }

        if (pl.getOperation() == Operation.ACC) {
            accumulator += pl.getArgument();
        }

        if (pl.getOperation() == Operation.JMP) {
            return line + pl.getArgument();
        }

        return line + 1;
    }

    private static class ProgramLine
    {
        private final Operation operation;
        private final int argument;

        private int timesEntered = 0;

        public ProgramLine(Operation operation, int argument)
        {
            super();
            this.operation = operation;
            this.argument = argument;
        }

        public Operation getOperation()
        {
            return operation;
        }

        public int getArgument()
        {
            return argument;
        }

        public int getTimesEntered()
        {
            return timesEntered;
        }

        public void increaseTimesEntered()
        {
            timesEntered++;
        }

        public void resetTimesEntered()
        {
            timesEntered = 0;
        }

        @Override
        public String toString()
        {
            return "ProgramLine [operation=" + operation + ", argument=" + argument + ", timesEntered=" + timesEntered
                    + "]";
        }

        public static ProgramLine createFromString(String inputString)
        {
            String[] splitted = inputString.split(" ");

            Operation op = null;
            switch (splitted[0]) {
            case "acc":
                op = Operation.ACC;
                break;
            case "jmp":
                op = Operation.JMP;
                break;
            case "nop":
                op = Operation.NOP;
                break;
            default:
                throw new IllegalArgumentException("Can't parse operation");
            }

            return new ProgramLine(op, Integer.parseInt(splitted[1]));
        }
    }

    private enum Operation {
        NOP,
        ACC,
        JMP
    }
}
