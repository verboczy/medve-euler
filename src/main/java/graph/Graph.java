package graph;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class Graph {

    private Map<Integer, Vertex> vertices;
    private List<Edge> edges;

    private Map<Integer, List<Integer>> adjacencyList;

    public Graph(final List<Vertex> vertices) {
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
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

        final int vertexIdentifier1 = edge.getVertexId1();
        final int vertexIdentifier2 = edge.getVertexId2();

        addToAdjacencyList(vertexIdentifier1, vertexIdentifier2);
        addToAdjacencyList(vertexIdentifier2, vertexIdentifier1);
    }

    private void addToAdjacencyList(final int startingVertexId, final int endingVertexId) {
        if (adjacencyList.containsKey(startingVertexId)) {
            adjacencyList.get(startingVertexId).add(endingVertexId);
        } else {
            final List<Integer> endingVertices = new ArrayList<>();
            endingVertices.add(endingVertexId);
            adjacencyList.put(startingVertexId, endingVertices);
        }
    }

    public Map<Integer, List<Integer>> copyAdjacencyList() {
        final Map<Integer, List<Integer>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    public boolean containsEdge(int vertexId1, int vertexId2) {
        return adjacencyList.get(vertexId1).contains(vertexId2);
    }
}
