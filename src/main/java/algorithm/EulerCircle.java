package algorithm;

import graph.Edge;
import graph.Graph;
import graph.GraphValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EulerCircle {

    private final Graph graph;
    private final Map<Integer, List<Edge>> adjacencyList;
    private final List<Integer> eulerCircle;

    public EulerCircle(final Graph graph) {
        this.graph = graph;
        this.adjacencyList = graph.getAdjacencyListEdges();
        this.eulerCircle = new ArrayList<>();
    }

    public void computeEulerCircle(final int startVertex) throws GraphValidationException {
        log.info("Computing Euler circle...");

        graph.eulerValidation(startVertex);

        eulerCircle.add(startVertex);
        eulerSteps(startVertex);

        log.info("Computing Euler circle finished. Euler circle is: \n{}", eulerCircle.stream().map(Object::toString).collect(Collectors.joining("-")));

        graph.setEulerCircle(eulerCircle);
    }

    private void eulerSteps(final int vertex) {
        //List<Integer> neighbors = new ArrayList<>(adjacencyList.get(vertex));
        final List<Edge> neighbors = adjacencyList.get(vertex).stream().filter(e -> !e.isVisited()).collect(Collectors.toList());
        for (Edge neighbor : neighbors) {
            final Edge edgePair = getEdgePair(neighbor);
            // 1. Check if we can take this edge.
            if (!neighbor.isVisited() && isValidNextEdge(neighbor, edgePair)) {
                log.debug("Next visited edge is {}.", neighbor.format());
                // 2. Take this edge.
                eulerCircle.add(neighbor.getVertexId2());
                // 3. Remove this edge from adjacency list, because we don't need it anymore, it is already visited.
                visitEdge(neighbor);
                visitEdge(edgePair);
                // 4. Call the Euler step method recursively.
                eulerSteps(neighbor.getVertexId2());
            }
        }
    }

    private boolean isValidNextEdge(final Edge edge, final Edge edgePair) {
        // This time we have no other choice, we must take the only edge we have.
        if (adjacencyList.get(edge.getVertexId1()).stream().filter(e -> !e.isVisited()).count() == 1) {
            return true;
        }

        // Test if we can take this edge.
        // 1. Count how many vertices are reachable now.
        List<Integer> isVisited = new ArrayList<>();
        int countBeforeRemove = countReachableVertices(edge.getVertexId1(), isVisited);
        // 2. Remove (visit) the edge under investigation.
        visitEdge(edge);
        visitEdge(edgePair);
        // 3. Count how many vertices are reachable after the removal.
        isVisited = new ArrayList<>();
        int countAfterRemove = countReachableVertices(edge.getVertexId1(), isVisited);
        // 4. Restore (unvisit) the deleted edge, because we have not taken it yet, we have just tested it.
        unvisitEdge(edge);
        unvisitEdge(edgePair);

        log.trace("Testing edge: {}. Reachable vertices before removal: {}, and after removal: {}.", edge.format(), countBeforeRemove, countAfterRemove);

        // 5. If they are not equal, then the graph is separated to more components, by taking the edge. So we must not choose this edge for now.
        return countBeforeRemove == countAfterRemove;
    }

    // We use a DFS algorithm to count the reachable vertices.
    private int countReachableVertices(final int vertex, final List<Integer> isVisited) {
        isVisited.add(vertex);
        int count = 1;
        for (Edge neighbor : adjacencyList.get(vertex)) {
            if (!neighbor.isVisited()) {
                if (!isVisited.contains(neighbor.getVertexId2())) {
                    count += countReachableVertices(neighbor.getVertexId2(), isVisited);
                }
            }
        }
        return count;
    }

    Edge getEdgePair(Edge edge) {
        return adjacencyList.get(edge.getVertexId2()).stream().filter(e -> !e.isVisited() && e.getVertexId2() == edge.getVertexId1()).findFirst().orElse(null);
    }

    private void visitEdge(final Edge edge) {
        edge.setVisited(true);
    }

    private void unvisitEdge(final Edge edge) {
        edge.setVisited(false);
    }

}
