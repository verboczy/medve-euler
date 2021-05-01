package graph;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Scanner;

@Slf4j
public class GraphUtil {

    private static final String SEPARATOR = ",";

    public Graph read(final String filename) throws FileNotFoundException {
        log.info("Reading graph from file [{}]...", filename);

        Scanner scanner = new Scanner(new File(filename));
        final Graph graph = new Graph();

        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            log.trace("Read line: [{}]", line);

            final String[] words = line.split(SEPARATOR);
            final int vertexId = Integer.parseInt(words[0]);
            final String vertexName = words[1];
            final Vertex vertex = new Vertex(vertexId, vertexName);
            graph.addVertex(vertex);
            for (int i = 2; i < words.length - 1; i += 2) {
                final int toVertex = Integer.parseInt(words[i]);
                final int distance = Integer.parseInt(words[i + 1]);
                graph.addEdge(new Edge(vertexId, toVertex, distance));
            }
        }
        log.info("Graph was read successfully.");
        return graph;
    }

    public void write(final Graph graph, final String filename) throws IOException {
        log.info("Writing euler circle to file [{}]...", filename);

        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        final String content = graph.getPrintableEulerCircle();
        writer.write(content);
        writer.close();

        log.info("Euler circle was written successfully.");
    }
}
