package graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GraphValidationTest {

    private static final int DISTANCE = 0;
    private static final int VERTEX_ID1 = 1;
    private static final int VERTEX_ID2 = 2;
    private static final int VERTEX_ID3 = 3;
    private static final int VERTEX_ID4 = 4;

    private Graph graph;
    private int validStartingVertex;

    @BeforeEach
    void setup() {
        graph = new Graph();
        graph.addVertex(new Vertex(0, VERTEX_ID1, "Vertex 1"));
        graph.addVertex(new Vertex(1, VERTEX_ID2, "Vertex 2"));
        graph.addVertex(new Vertex(2, VERTEX_ID3, "Vertex 3"));
        graph.addVertex(new Vertex(3, VERTEX_ID4, "Vertex 4"));
        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID1, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID3, VERTEX_ID4, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID4, VERTEX_ID3, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID4, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID4, VERTEX_ID1, DISTANCE));

        validStartingVertex = 1;
    }

    @DisplayName("Starting vertex validation")
    @Test
    void startingVertexValidation() {
        // Given
        final int notValidStartingVertex = 404;
        final String expectedErrorMessage = String.format("The graph doesn't contain the given vertex [%d].", notValidStartingVertex);

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(notValidStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }

    @DisplayName("Edge validation")
    @Test
    void edgeValidation() {
        // Given
        final int nonExistingVertexId = 404;
        final String expectedErrorMessage = String.format("The graph doesn't contain vertex [%d].\n", nonExistingVertexId);

        graph.addEdge(new Edge(nonExistingVertexId, VERTEX_ID1, DISTANCE));

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(validStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }

    @DisplayName("Edge validation - to vertex is invalid")
    @Test
    void edgeValidation_otherVertex() {
        // Given
        final int nonExistingVertexId = 404;
        final String expectedErrorMessage = String.format("The graph doesn't contain vertex [%d].\n", nonExistingVertexId);

        graph.addEdge(new Edge(VERTEX_ID1, nonExistingVertexId, DISTANCE));

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(validStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }

    @DisplayName("Distance validation - negative")
    @Test
    void negativeDistanceValidation() {
        // Given
        final int negativeDistance = -1;
        final String expectedErrorMessage = String.format("The edge [%d-%d] has negative distance [%d].", VERTEX_ID1, VERTEX_ID2, negativeDistance);

        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, negativeDistance));

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(validStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }

    @DisplayName("Distance validation")
    @Test
    void distanceValidation() {
        // Given
        final int invalidDistance = 400;
        final String expectedErrorMessage = String.format("The same edges [%d-%d] have different distances [%d, %d].", VERTEX_ID1, VERTEX_ID2, DISTANCE, invalidDistance);

        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, invalidDistance));

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(validStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }

    @DisplayName("Degree validation")
    @Test
    void degreeValidation() {
        // Given
        final String expectedErrorMessage1 = String.format("Vertex %d has odd degree [%d].", VERTEX_ID1, 3);
        final String expectedErrorMessage2 = String.format("Vertex %d has odd degree [%d].\n", VERTEX_ID2, 3);
        final String expectedErrorMessage = String.join("\n", expectedErrorMessage1, expectedErrorMessage2);

        graph.addEdge(new Edge(VERTEX_ID1, VERTEX_ID2, DISTANCE));
        graph.addEdge(new Edge(VERTEX_ID2, VERTEX_ID1, DISTANCE));

        // When
        final GraphValidationException graphValidationException = assertThrows(GraphValidationException.class, () -> graph.validate(validStartingVertex));

        // Then
        assertEquals(expectedErrorMessage, graphValidationException.getMessage());
    }
}
