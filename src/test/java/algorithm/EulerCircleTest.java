package algorithm;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EulerCircleTest {

    private static final int DISTANCE = 0;

    @DisplayName("Graph 1")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @ParameterizedTest(name = "{index} - Starting from vertex {0}")
    void graph1_test(final int startingVertexId) {
        // Given
        final Vertex vertex1 = new Vertex(1, "1");
        final Vertex vertex2 = new Vertex(2, "2");
        final Vertex vertex3 = new Vertex(3, "3");
        final Vertex vertex4 = new Vertex(4, "4");
        final Vertex vertex5 = new Vertex(5, "5");

        final Graph graph = new Graph(Arrays.asList(vertex1, vertex2, vertex3, vertex4, vertex5));
        graph.addEdge(new Edge(1, 2, DISTANCE));
        graph.addEdge(new Edge(1, 3, DISTANCE));
        graph.addEdge(new Edge(1, 4, DISTANCE));
        graph.addEdge(new Edge(1, 5, DISTANCE));
        graph.addEdge(new Edge(2, 3, DISTANCE));
        graph.addEdge(new Edge(2, 4, DISTANCE));
        graph.addEdge(new Edge(2, 5, DISTANCE));
        graph.addEdge(new Edge(3, 4, DISTANCE));
        graph.addEdge(new Edge(3, 5, DISTANCE));
        graph.addEdge(new Edge(4, 5, DISTANCE));

        final EulerCircle eulerCircle = new EulerCircle(graph);

        // When
        final List<Integer> solution = eulerCircle.computeEulerCircle(startingVertexId);

        // Then
        checkSolution(graph, solution);
    }

    @DisplayName("Graph 2")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    @ParameterizedTest(name = "{index} - Starting from vertex {0}")
    void graph2_test(final int startingVertexId) {
        // Given
        final Vertex vertex1 = new Vertex(1, "1");
        final Vertex vertex2 = new Vertex(2, "2");
        final Vertex vertex3 = new Vertex(3, "3");
        final Vertex vertex4 = new Vertex(4, "4");
        final Vertex vertex5 = new Vertex(5, "5");
        final Vertex vertex6 = new Vertex(6, "6");
        final Vertex vertex7 = new Vertex(7, "7");
        final Vertex vertex8 = new Vertex(8, "8");

        final Graph graph = new Graph(Arrays.asList(vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8));
        graph.addEdge(new Edge(1, 1, DISTANCE));
        graph.addEdge(new Edge(1, 2, DISTANCE));
        graph.addEdge(new Edge(1, 3, DISTANCE));
        graph.addEdge(new Edge(1, 4, DISTANCE));
        graph.addEdge(new Edge(1, 8, DISTANCE));
        graph.addEdge(new Edge(2, 3, DISTANCE));
        graph.addEdge(new Edge(2, 5, DISTANCE));
        graph.addEdge(new Edge(2, 5, DISTANCE));
        graph.addEdge(new Edge(3, 4, DISTANCE));
        graph.addEdge(new Edge(3, 6, DISTANCE));
        graph.addEdge(new Edge(4, 7, DISTANCE));
        graph.addEdge(new Edge(4, 7, DISTANCE));
        graph.addEdge(new Edge(5, 6, DISTANCE));
        graph.addEdge(new Edge(5, 8, DISTANCE));
        graph.addEdge(new Edge(6, 7, DISTANCE));
        graph.addEdge(new Edge(6, 8, DISTANCE));
        graph.addEdge(new Edge(7, 8, DISTANCE));
        graph.addEdge(new Edge(8, 8, DISTANCE));

        final EulerCircle eulerCircle = new EulerCircle(graph);

        // When
        final List<Integer> solution = eulerCircle.computeEulerCircle(startingVertexId);

        // Then
        checkSolution(graph, solution);
    }

    private void checkSolution(final Graph graph, final List<Integer> solution) {
        final int edgeCountOfGraph = graph.getEdges().size();
        final int edgeCountOfEulerCircle = solution.size() - 1; // solution contains the vertices, edges are between them.
        assertEquals(edgeCountOfGraph, edgeCountOfEulerCircle, String.format("Graph contains %d edges, but the Euler circle contains %d edges.", edgeCountOfGraph, edgeCountOfEulerCircle));
        assertAllEdgeIsValid(graph, solution);
        assertAllEdgeIsCovered(graph, solution);
    }

    private void assertAllEdgeIsValid(final Graph graph, final List<Integer> vertices) {
        for (int i = 0; i < vertices.size() - 1; ++i) {
            int actualVertexId = vertices.get(i);
            int nextVertexId = vertices.get(i + 1);

            assertTrue(graph.containsEdge(actualVertexId, nextVertexId), String.format("Graph does not contain edge from %s to %s.", actualVertexId, nextVertexId));
        }
    }

    private void assertAllEdgeIsCovered(final Graph graph, final List<Integer> vertices) {
        Map<Integer, List<Integer>> adjacencyList = graph.copyAdjacencyList();
        for (int i = 0; i < vertices.size() - 1; ++i) {
            int actualVertexId = vertices.get(i);
            int nextVertexId = vertices.get(i + 1);

            assertTrue(adjacencyList.get(actualVertexId).remove((Integer) nextVertexId), String.format("Edge from %d to %d is covered too many times.", actualVertexId, nextVertexId));
            assertTrue(adjacencyList.get(nextVertexId).remove((Integer) actualVertexId), String.format("Edge from %d to %d is covered too many times.", actualVertexId, nextVertexId));
        }
        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            assertTrue(entry.getValue().isEmpty(), "There are uncovered edge(s) in the graph.");
        }
    }
}
