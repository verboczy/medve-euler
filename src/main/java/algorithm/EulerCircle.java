package algorithm;

import graph.Graph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EulerCircle {

    private final Graph graph;
    private final Map<Integer, List<Integer>> adjacencyList;
    private final List<Integer> eulerCircle;

    public EulerCircle(final Graph graph) {
        this.graph = graph;
        this.adjacencyList = graph.copyAdjacencyList();
        this.eulerCircle = new ArrayList<>();
    }

    public List<Integer> computeEulerCircle(final int startVertex) {
        log.info("Computing Euler circle...");

        eulerCircle.add(startVertex);
        eulerSteps(startVertex);

        log.info("Computing Euler circle finished. Euler circle is: \n{}", eulerCircle.stream().map(Object::toString).collect(Collectors.joining("-")));

        return eulerCircle;
    }

    private void eulerSteps(int vertex) {
        List<Integer> neighbors = adjacencyList.get(vertex);

        for (int i = 0; i < neighbors.size(); ++i) {
            int neighbor = neighbors.get(i);
            // 1. Check if we can take this edge.
            if (isValidNextEdge(vertex, neighbor)) {
                log.debug("Next visited is edge is from {} to {}.", vertex, neighbor);
                // 2. Take this edge.
                eulerCircle.add(neighbor);
                // 3. Remove this edge from adjacency list, because we don't need it anymore, it is already visited.
                removeEdge(vertex, neighbor);
                // 4. Call the Euler step method recursively.
                eulerSteps(neighbor);
            }
        }
    }

    private void addEdge(final int vertex, final int neighbor) {
        addEdgeToAdjacencyList(vertex, neighbor);
        addEdgeToAdjacencyList(neighbor, vertex);
    }

    private void addEdgeToAdjacencyList(final int vertex, final int neighbor) {
        if (adjacencyList.containsKey(vertex)) {
            adjacencyList.get(vertex).add(neighbor);
        } else {
            final List<Integer> neighbors = new ArrayList<>();
            neighbors.add(neighbor);
            adjacencyList.put(vertex, neighbors);
        }
    }

    private void removeEdge(final int vertex, final int neighbor) {
        adjacencyList.get(vertex).remove((Integer) neighbor);
        adjacencyList.get(neighbor).remove((Integer) vertex);
    }

    private boolean isValidNextEdge(final int vertex, final int neighbor) {
        // This time we have no other choice, we must take the only edge we have.
        if (adjacencyList.get(vertex).size() == 1) {
            return true;
        }

        // Test if we can take this edge.
        // 1. Count how many vertices are reachable now.
        List<Integer> isVisited = new ArrayList<>();
        int countBeforeRemove = countReachableVertices(vertex, isVisited);
        // 2. Remove the edge under investigation.
        removeEdge(vertex, neighbor);
        // 3. Count how many vertices are reachable after the removal.
        isVisited = new ArrayList<>();
        int countAfterRemove = countReachableVertices(vertex, isVisited);
        // 4. Restore the deleted edge, because we have not taken it yet, we have just tested it.
        addEdge(vertex, neighbor);

        log.trace("Reachable vertices before removal: {}, and after removal: {}.", countBeforeRemove, countAfterRemove);

        // 5. If they are not equal, then the graph is separated to more components, by taking the edge. So we must not choose this edge for now.
        return countBeforeRemove == countAfterRemove;
    }

    // We use a DFS algorithm to count the reachable vertices.
    private int countReachableVertices(int vertex, List<Integer> isVisited) {
        isVisited.add(vertex);
        int count = 1;
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!isVisited.contains(neighbor)) {
                count += countReachableVertices(neighbor, isVisited);
            }
        }
        return count;
    }


}
