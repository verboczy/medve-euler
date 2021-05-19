package algorithm;

import graph.Edge;
import graph.Graph;
import graph.GraphValidationException;
import graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EulerizationTest {

    private Eulerization eulerization;

    @BeforeEach
    void setup() {
        eulerization = new Eulerization();
    }

    @Test
    void eulerizationTest() throws GraphValidationException {
        // Given
        final Graph graph = new Graph();

        graph.addVertex(new Vertex(0, 1, "A"));
        graph.addVertex(new Vertex(1, 2, "B"));
        graph.addVertex(new Vertex(2, 3, "C"));
        graph.addVertex(new Vertex(3, 4, "D"));

        Edge edge1 = new Edge(1, 2, 25);
        graph.addEdge(edge1);
        Edge edge2 = new Edge(2, 1, 25);
        graph.addEdge(edge2);

        Edge edge3 = new Edge(1, 3, 30);
        graph.addEdge(edge3);
        Edge edge4 = new Edge(3, 1, 30);
        graph.addEdge(edge4);

        Edge edge5 = new Edge(1, 4, 45);
        graph.addEdge(edge5);
        Edge edge6 = new Edge(4, 1, 45);
        graph.addEdge(edge6);

        Edge edge7 = new Edge(2, 3, 40);
        graph.addEdge(edge7);
        Edge edge8 = new Edge(3, 2, 40);
        graph.addEdge(edge8);

        Edge edge9 = new Edge(2, 4, 16);
        graph.addEdge(edge9);
        Edge edge10 = new Edge(4, 2, 16);
        graph.addEdge(edge10);

        Edge edge11 = new Edge(3, 4, 20);
        graph.addEdge(edge11);
        Edge edge12 = new Edge(4, 3, 20);
        graph.addEdge(edge12);

        // When
        eulerization.eulerizeGraph(graph);

        // Then
        final List<Edge> edges = graph.getEdges();
        assertEquals(16, edges.size());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge1)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge2)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge3)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge4)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge5)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge6)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge7)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge8)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge9)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge10)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge11)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge12)).count());
    }

    @Test
    void eulerizationTest2() throws GraphValidationException {
        // Given
        final Graph graph = new Graph();

        graph.addVertex(new Vertex(0, 1, "A"));
        graph.addVertex(new Vertex(1, 2, "B"));
        graph.addVertex(new Vertex(2, 3, "C"));
        graph.addVertex(new Vertex(3, 4, "D"));

        Edge edge1 = new Edge(1, 2, 25);
        graph.addEdge(edge1);
        Edge edge2 = new Edge(2, 1, 25);
        graph.addEdge(edge2);

        Edge edge3 = new Edge(1, 3, 30);
        graph.addEdge(edge3);
        Edge edge4 = new Edge(3, 1, 30);
        graph.addEdge(edge4);

        Edge edge5 = new Edge(1, 4, 45);
        graph.addEdge(edge5);
        Edge edge6 = new Edge(4, 1, 45);
        graph.addEdge(edge6);

        Edge edge7 = new Edge(2, 3, 40);
        graph.addEdge(edge7);
        Edge edge8 = new Edge(3, 2, 40);
        graph.addEdge(edge8);

        Edge edge9 = new Edge(2, 4, 14);
        graph.addEdge(edge9);
        Edge edge10 = new Edge(4, 2, 14);
        graph.addEdge(edge10);

        Edge edge11 = new Edge(3, 4, 20);
        graph.addEdge(edge11);
        Edge edge12 = new Edge(4, 3, 20);
        graph.addEdge(edge12);

        // When
        eulerization.eulerizeGraph(graph);

        // Then
        final List<Edge> edges = graph.getEdges();
        assertEquals(16, edges.size());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge1)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge2)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge3)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge4)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge5)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge6)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge7)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge8)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge9)).count());
        assertEquals(2, edges.stream().filter(edge -> edge.equals(edge10)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge11)).count());
        assertEquals(1, edges.stream().filter(edge -> edge.equals(edge12)).count());
    }
}
