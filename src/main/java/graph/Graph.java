package graph;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Graph {

    private static final String RESULT_LINE_TEMPLATE = "%d,%d,%d,%s,%s,%d,%d";

    private Map<Integer, Vertex> vertices;
    private Map<Integer, Integer> vertexSerials;
    private List<Edge> edges;

    private List<Integer> eulerCircle;

    public Graph() {
        this.vertices = new HashMap<>();
        this.vertexSerials = new HashMap<>();
        this.edges = new ArrayList<>();
        this.eulerCircle = new ArrayList<>();
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getVertexSerialByVertexId(final int vertexId) {
        return vertices.get(vertexId).getSerial();
    }

    public int getVertexIdByVertexSerial(final int serial) {
        return vertexSerials.get(serial);
    }

    public void addVertex(final Vertex vertex) {
        final int identifier = vertex.getIdentifier();
        if (vertices.containsKey(identifier)) {
            log.warn("Graph already contains vertex with id {}.", identifier);
        } else {
            vertices.put(identifier, vertex);
            vertexSerials.put(vertex.getSerial(), identifier);
        }
    }

    public void addEdge(final Edge edge) {
        edges.add(edge);

        // If graph contains self loop, then add the edge one more times.
        if (edge.getVertexId1() == edge.getVertexId2()) {
            edges.add(edge);
        }
    }

    public void validate(final int startVertex) throws GraphValidationException {
        if (!vertices.containsKey(startVertex)) {
            throw new GraphValidationException("The graph doesn't contain the given vertex [%d].", startVertex);
        }

        validateEdges();
        validateDistances();
        validateDegrees();
    }

    public void validate2() {
        for (Edge edge : edges) {
            if (edges.stream().noneMatch(e -> e.getVertexId1() == edge.getVertexId2() && e.getVertexId2() == edge.getVertexId1())) {
                log.error("Edge ({} - {}) does not have a pair", edge.getVertexId1(), edge.getVertexId2());
            }

        }
    }

    private void validateEdges() throws GraphValidationException {
        final StringBuilder errorMessages = new StringBuilder();
        boolean hasError = false;
        final Set<Integer> vertexIdSet = vertices.keySet();

        for (final Edge edge : edges) {
            final int vertexId1 = edge.getVertexId1();
            if (!vertexIdSet.contains(vertexId1)) {
                errorMessages.append(String.format("The graph doesn't contain vertex [%d].", vertexId1)).append("\n");
                hasError = true;
            }
            int vertexId2 = edge.getVertexId2();
            if (!vertexIdSet.contains(vertexId2)) {
                errorMessages.append(String.format("The graph doesn't contain vertex [%d].", vertexId2)).append("\n");
                hasError = true;
            }
        }

        if (hasError) {
            throw new GraphValidationException(errorMessages.toString());
        }
    }

    private void validateDistances() throws GraphValidationException {
        for (final Edge edge : edges) {
            for (final Edge edge2 : edges) {
                if (edge2.getDistance() < 0) {
                    throw new GraphValidationException("The edge [%d-%d] has negative distance [%d].", edge2.getVertexId1(), edge2.getVertexId2(), edge2.getDistance());
                }
                if ((edge.getVertexId1() == edge2.getVertexId1() && edge.getVertexId2() == edge2.getVertexId2())
                        || (edge.getVertexId1() == edge2.getVertexId2() && edge.getVertexId2() == edge2.getVertexId1())) {
                    if (edge.getDistance() != edge2.getDistance()) {
                        throw new GraphValidationException("The same edges [%d-%d] have different distances [%d, %d].", edge.getVertexId1(), edge.getVertexId2(), edge.getDistance(), edge2.getDistance());
                    }
                }
            }
        }
    }

    private void validateDegrees() throws GraphValidationException {
        final StringBuilder errorMessages = new StringBuilder();
        boolean hasError = false;
        for (final int vertexId : vertices.keySet()) {
            final long degree = edges.stream().filter(edge -> edge.getVertexId1() == vertexId).count();
            if (degree % 2 != 0) {
                hasError = true;
                errorMessages.append(String.format("Vertex %d has odd degree [%d].", vertexId, degree)).append("\n");
            }
        }

        if (hasError) {
            throw new GraphValidationException(errorMessages.toString());
        }
    }

    public List<Integer> getVertexSerialsWithOddDegree() {
        final List<Integer> verticesWithOddDegree = new ArrayList<>();

        for (final int vertexId : vertices.keySet()) {
            final long degree = edges.stream().filter(edge -> edge.getVertexId1() == vertexId).count();
            if (degree % 2 != 0) {
                verticesWithOddDegree.add(getVertexSerialByVertexId(vertexId));
            }
        }

        return verticesWithOddDegree;
    }

    public List<Integer> getVerticesWithOddDegree() {
        final List<Integer> verticesWithOddDegree = new ArrayList<>();

        for (final int vertexId : vertices.keySet()) {
            final long degree = edges.stream().filter(edge -> edge.getVertexId1() == vertexId).count();
            if (degree % 2 != 0) {
                verticesWithOddDegree.add(vertexId);
            }
        }

        return verticesWithOddDegree;
    }

    public Map<Integer, List<Edge>> getAdjacencyListEdges() {
        final Map<Integer, List<Edge>> adjacencyList = new HashMap<>();
        for (Integer u : vertices.keySet()) {
            adjacencyList.put(u, edges.stream().filter(edge -> edge.getVertexId1() == u).collect(Collectors.toList()));
        }
        return adjacencyList;
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

    public void duplicateEdge(final int u, final int v) throws GraphValidationException {
        final Edge edge = edges.stream().filter(e -> e.getVertexId1() == u && e.getVertexId2() == v).findFirst().orElseThrow(() -> new GraphValidationException(String.format("Graph doesn't have edge between %d and %d.", u, v)));
        addEdge(new Edge(edge.getVertexId1(), edge.getVertexId2(), edge.getDistance()));
        addEdge(new Edge(edge.getVertexId2(), edge.getVertexId1(), edge.getDistance()));
    }

    public String getPrintableEulerCircle() {
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
        return stringBuilder.toString();
    }
}
