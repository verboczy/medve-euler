package main;

import algorithm.EulerCircle;
import algorithm.Eulerization;
import graph.Graph;
import graph.GraphUtil;
import graph.GraphValidationException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.time.StopWatch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        StopWatch stopWatch = StopWatch.createStarted();
        log.info("Program started.");

        final Arguments arguments = getArguments(args);

        try {
            // 1. Create the graph.
            final GraphUtil graphUtil = new GraphUtil();
            final Graph graph = graphUtil.read(arguments.inputFilename);

            // 2. Validate the graph.
            graph.prevalidate();

            // 3. Eulerize the graph.
            final Eulerization eulerization = new Eulerization();
            eulerization.eulerizeGraph(graph);
            graph.deleteExtraEdges();

            // 4. Compute Euler circle.
            final EulerCircle eulerCircle = new EulerCircle(graph);
            eulerCircle.computeEulerCircle(arguments.startingVertex);

            // 5. Save the result.
            graphUtil.write(graph, arguments.outputFilename);
        } catch (final FileNotFoundException fileNotFoundException) {
            log.error("File [{}] is not found.", arguments.inputFilename);
        }
        catch (final GraphValidationException graphValidationException) {
            log.error(graphValidationException.getMessage());
        }
        catch (final IOException ioException) {
            log.error("Could not write graph to file [{}].", arguments.outputFilename);
        }
        stopWatch.stop();
        log.info("Program ended in: {} ({} ms)", stopWatch.formatTime(), stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    private static Arguments getArguments(final String[] args) {
        final String helpShort = "h";
        final String helpLong = "help";
        final String startShort = "s";
        final String startLong = "start";
        final String inputShort = "in";
        final String inputLong = "inputFile";
        final String outputShort = "out";
        final String outputLong = "outputFile";

        final Options options = new Options();
        options.addOption(new Option(helpShort, helpLong, false, "Prints this help message."));
        options.addOption(new Option(startShort, startLong, true, "The id of the starting vertex. Mandatory."));
        options.addOption(new Option(inputShort, inputLong, true, "The input file, that contains the graph. Mandatory."));
        options.addOption(new Option(outputShort, outputLong, true, "The path of the output file, that will contain the result. Mandatory."));

        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(options, args);

            boolean hasError = false;
            if (cmd.hasOption(helpShort) || cmd.hasOption(helpLong)) {
                log.info("Print help...");
                hasError = true;
            }
            if (!cmd.hasOption(startShort) && !cmd.hasOption(startLong)) {
                log.error("Starting vertex argument is missing.");
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
                return new Arguments(cmd.getOptionValue(inputShort), cmd.getOptionValue(outputShort), Integer.parseInt(cmd.getOptionValue(startShort)));
            }
        } catch (final ParseException parseException) {
            log.error("Error occurred during parsing arguments.", parseException);
            System.exit(-2);
        } catch (NumberFormatException numberFormatException) {
            log.error("The id of the starting vertex must be an integer.", numberFormatException);
            System.exit(-3);
        }

        return new Arguments("", "", -1);
    }

    private static void printHelp(final Options options) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ant", options);
    }

    @Value
    public static class Arguments {
        String inputFilename;
        String outputFilename;
        int startingVertex;
    }
}