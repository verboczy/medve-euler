package graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GraphUtilTest {

    private static GraphUtil graphUtil;

    @BeforeAll
    static void setup() {
        graphUtil = new GraphUtil();
    }

    @DisplayName("Graph reader - file not found")
    @Test
    void testGraphReader_fileNotFound() {
        // When - Then
        assertThrows(FileNotFoundException.class, () -> graphUtil.read("no such file"));
    }

    @DisplayName("Graph reader - successful")
    @Test
    void testGraphReader() throws FileNotFoundException {
        // Given
        Graph expectedGraph = new Graph();
        expectedGraph.addVertex(new Vertex(1, "Name for vertex 1"));
        expectedGraph.addVertex(new Vertex(2, "Name for vertex 2"));
        expectedGraph.addVertex(new Vertex(3, "Name for vertex 3"));
        expectedGraph.addVertex(new Vertex(4, "Name for vertex 4"));
        expectedGraph.addVertex(new Vertex(5, "Name for vertex 5"));
        expectedGraph.addEdge(new Edge(1, 2, 10));
        expectedGraph.addEdge(new Edge(1, 3, 10));
        expectedGraph.addEdge(new Edge(1, 4, 15));
        expectedGraph.addEdge(new Edge(1, 5, 15));
        expectedGraph.addEdge(new Edge(2, 1, 10));
        expectedGraph.addEdge(new Edge(2, 3, 5));
        expectedGraph.addEdge(new Edge(2, 4, 10));
        expectedGraph.addEdge(new Edge(2, 5, 10));
        expectedGraph.addEdge(new Edge(3, 1, 10));
        expectedGraph.addEdge(new Edge(3, 2, 5));
        expectedGraph.addEdge(new Edge(3, 4, 10));
        expectedGraph.addEdge(new Edge(3, 5, 10));
        expectedGraph.addEdge(new Edge(4, 1, 15));
        expectedGraph.addEdge(new Edge(4, 2, 10));
        expectedGraph.addEdge(new Edge(4, 3, 10));
        expectedGraph.addEdge(new Edge(4, 5, 5));
        expectedGraph.addEdge(new Edge(5, 1, 15));
        expectedGraph.addEdge(new Edge(5, 2, 10));
        expectedGraph.addEdge(new Edge(5, 3, 10));
        expectedGraph.addEdge(new Edge(5, 4, 5));

        // When
        final Graph actualGraph = graphUtil.read("src/test/resources/graphs/graph1.txt");

        // Then
        assertEquals(expectedGraph, actualGraph);
    }
}
