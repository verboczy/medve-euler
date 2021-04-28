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

    private final int numberOfVertices;

    private Map<Integer, Vertex> vertices;
    private List<Edge> edges;

    private Map<Integer, List<Integer>> adjacencyList;

    public Graph(final int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;

        vertices = new HashMap<>();
        edges = new ArrayList<>();
        adjacencyList = new HashMap<>();
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
        return new HashMap<>(adjacencyList);
    }

}
