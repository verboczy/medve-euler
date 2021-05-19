package graph;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GraphOperationTest {

    private static final int DISTANCE = 0;
    private static final int VERTEX_ID1 = 1;
    private static final int VERTEX_ID2 = 2;
    private static final int VERTEX_ID3 = 3;

    private Graph graph;

    @BeforeEach
    void setup() {
        graph = new Graph();
        graph.addVertex(new Vertex(0, VERTEX_ID1, "Vertex 1"));
        graph.addVertex(new Vertex(1, VERTEX_ID2, "Vertex 2"));
        graph.addVertex(new Vertex(2, VERTEX_ID3, "Vertex 3"));
        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID1, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID1, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID2, DISTANCE));
    }

    @Test
    void deleteExtraEdges() {
        // Given
        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID1, DISTANCE));

        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID2, DISTANCE));

        assumeTrue(countEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE)) == 3);
        assumeTrue(countEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE)) == 4);

        // When
        graph.deleteExtraEdges();

        // Then
        assertEquals(1, countEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE)));
        assertEquals(2, countEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE)));
    }

    private int countEdge(Edge edge) {
        return (int) graph.getEdges().stream().filter(e -> e.equals(edge)).count();
    }
}
