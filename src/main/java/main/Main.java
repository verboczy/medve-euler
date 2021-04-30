package main;

import algorithm.EulerCircle;
import graph.Graph;
import graph.GraphReader;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        final Arguments arguments = getArguments(args);
        try {
            GraphReader graphReader = new GraphReader();
            Graph graph = graphReader.read(arguments.inputFilename);
            EulerCircle eulerCircle = new EulerCircle(graph);
            eulerCircle.computeEulerCircle(1);
            graph.printEulerCircle();
        } catch (FileNotFoundException e) {
            log.error("File [{}] is not found.", arguments.inputFilename);
        }
    }

    private static Arguments getArguments(final String[] args) {
        final String helpShort = "h";
        final String helpLong = "help";
        final String inputShort = "in";
        final String inputLong = "inputFile";
        final String outputShort = "out";
        final String outputLong = "outputFile";

        final Options options = new Options();
        options.addOption(new Option(helpShort, helpLong, false, "Prints this help message."));
        options.addOption(new Option(inputShort, inputLong, true, "The input file, that contains the graph."));
        options.addOption(new Option(outputShort, outputLong, true, "The path of the output file, that will contain the result."));

        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(options, args);

            boolean hasError = false;
            if (cmd.hasOption(helpShort) || cmd.hasOption(helpLong)) {
                log.info("Print help...");
                hasError = true;
            }
            if (!cmd.hasOption(inputShort) && !cmd.hasOption(inputLong)) {
                log.error("Input file argument is missing.");
                hasError = true;
            }
            if (!cmd.hasOption(outputShort) && !cmd.hasOption(outputLong)) {
                log.error("Output file argument is missing.");
                hasError = true;
            }

            if (hasError) {
                printHelp(options);
                System.exit(-1);
            } else {
                return new Arguments(cmd.getOptionValue(inputShort), cmd.getOptionValue(outputShort));
            }
        } catch (final ParseException e) {
            log.error("Error occurred during parsing arguments.", e);
            System.exit(-2);
        }
        return new Arguments("", "");
    }

    private static void printHelp(final Options options) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ant", options);
    }

    @Value
    public static class Arguments {
        String inputFilename;
        String outputFilename;
    }
}