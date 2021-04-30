package main;

import algorithm.EulerCircle;
import graph.Graph;
import graph.GraphReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        GraphReader graphReader = new GraphReader();
        String filename = "src/main/resources/graphs/graph1.txt";
        try {
            Graph graph = graphReader.read(filename);
            EulerCircle eulerCircle = new EulerCircle(graph);
            eulerCircle.computeEulerCircle(1);
            graph.printEulerCircle();
        } catch (FileNotFoundException e) {
            log.error("File {} is not found.", filename);
        }
    }
}