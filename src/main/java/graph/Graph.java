package graph;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
@Data
public class Graph {

    private static final String RESULT_LINE_TEMPLATE = "%d,%d,%d,%s,%s,%d,%d";

    private Map<Integer, Vertex> vertices;
    private List<Edge> edges;

    private List<Integer> eulerCircle;

    public Graph() {
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        this.eulerCircle = new ArrayList<>();
    }

    public void addVertex(final Vertex vertex) {
        final int identifier = vertex.getIdentifier();
        if (vertices.containsKey(identifier)) {
            log.warn("Graph already contains vertex with id {}.", identifier);
        } else {
            vertices.put(identifier, vertex);
        }
    }

    public void addEdge(final Edge edge) {
        edges.add(edge);

        // If graph contains self loop, then add the edge one more times.
        if (edge.getVertexId1() == edge.getVertexId2()) {
            edges.add(edge);
        }
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        edges.forEach(edge -> {
            int fromVertex = edge.getVertexId1();
            int toVertex = edge.getVertexId2();
            if (map.containsKey(fromVertex)) {
                map.get(fromVertex).add(toVertex);
            } else {
                List<Integer> toVertices = new ArrayList<>();
                toVertices.add(toVertex);
                map.put(fromVertex, toVertices);
            }
        });

        return map;
    }

    private Predicate<Edge> edgeEqualsPredicate(final int vertexId1, final int vertexId2) {
        return edge -> (edge.getVertexId1() == vertexId1 && edge.getVertexId2() == vertexId2)
                || (edge.getVertexId1() == vertexId2 && edge.getVertexId2() == vertexId1);
    }

    public boolean containsEdge(final int vertexId1, final int vertexId2) {
        return edges.stream().anyMatch(edgeEqualsPredicate(vertexId1, vertexId2));
    }

    public void printEulerCircle() {
        final StringBuilder stringBuilder = new StringBuilder();

        int sum = 0;

        for (int i = 0; i < eulerCircle.size() - 1; ++i) {
            final int actualVertexId = eulerCircle.get(i);
            final int nextVertexId = eulerCircle.get(i + 1);

            int distance = edges.stream()
                    .filter(edgeEqualsPredicate(actualVertexId, nextVertexId))
                    .findFirst()
                    .map(Edge::getDistance)
                    .orElseThrow(() -> new IllegalStateException(String.format("Graph doesn't contain edge between vertex %d and %d.", nextVertexId, actualVertexId)));

            sum += distance;
            final String actualVertex = vertices.get(actualVertexId).getName();
            final String nextVertex = vertices.get(nextVertexId).getName();
            stringBuilder.append(String.format(RESULT_LINE_TEMPLATE, i + 1, actualVertexId, nextVertexId, actualVertex, nextVertex, distance, sum));
            stringBuilder.append("\n");
        }

        log.info("Result: \n{}", stringBuilder);
    }
}
