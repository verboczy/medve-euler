package algorithm;

import graph.Edge;
import graph.Graph;
import graph.GraphValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Eulerization {

    private static final int INFINITE = 1_000_000;

    public void eulerizeGraph(final Graph graph) throws GraphValidationException {
        log.info("Eulerizing graph...");

        List<Integer> verticesWithOddDegree = graph.getVertexSerialsWithOddDegree();

        FloydWarshallResult floydWarshallResult = floydWarshall(graph);
        int[][] distance = floydWarshallResult.getDistance();

        int minimalPairingCost = INFINITE;
        Map<Integer, Integer> minimalPairing = new HashMap<>();
        for (int startIndex = 0; startIndex < verticesWithOddDegree.size(); ++startIndex) {
            final List<Integer> notPaired = new ArrayList<>(verticesWithOddDegree);
            int index = startIndex;

            int actualPairingCost = 0;
            final Map<Integer, Integer> actualPairing = new HashMap<>();
            while (!notPaired.isEmpty()) {
                if (index == verticesWithOddDegree.size()) {
                    index = 0;
                }

                final int u = verticesWithOddDegree.get(index);
                if (!notPaired.contains(u)) {
                    ++index;
                    continue;
                }

                int minDistance = INFINITE;
                int min = u;
                for (int v : notPaired) {
                    if (u == v) {
                        continue;
                    }
                    if (distance[u][v] < minDistance) {
                        minDistance = distance[u][v];
                        min = v;
                    }
                }
                actualPairingCost += minDistance;
                actualPairing.put(u, min);

                notPaired.remove((Integer) u);
                notPaired.remove((Integer) min);

                ++index;
            }

            if (actualPairingCost < minimalPairingCost) {
                log.trace("Pairing cost [{} - pairing {}] is lower than [{} - pairing {}].", actualPairingCost, actualPairing, minimalPairingCost, minimalPairing);
                minimalPairing = actualPairing;
                minimalPairingCost = actualPairingCost;
            }
        }

        for (Map.Entry<Integer, Integer> pair : minimalPairing.entrySet()) {
            final int u = pair.getKey();
            final int v = pair.getValue();
            duplicateEdges(graph, floydWarshallResult.getPathBetweenTwoVertices(u, v));
            log.debug("Paired [{}] to [{}].", u, v);
        }

        log.info("Eulerizing finished");
    }

    private void duplicateEdges(final Graph graph, final List<Integer> pathBetweenTwoVertices) throws GraphValidationException {
        for (int i = 0; i < pathBetweenTwoVertices.size() - 1; ++i) {
            final int u = graph.getVertexIdByVertexSerial(pathBetweenTwoVertices.get(i));
            final int v = graph.getVertexIdByVertexSerial(pathBetweenTwoVertices.get(i + 1));

            graph.duplicateEdge(u, v);
        }
    }

    public FloydWarshallResult floydWarshall(final Graph graph) {
        final int n = graph.getVertexCount();
        final int[][] distance = new int[n][n];
        final int[][] path = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                distance[i][j] = INFINITE;
                path[i][j] = -1;
            }
        }

        for (final Edge edge : graph.getEdges()) {
            final int u = graph.getVertexSerialByVertexId(edge.getVertexId1());
            final int v = graph.getVertexSerialByVertexId(edge.getVertexId2());
            distance[u][v] = edge.getDistance();
            path[u][v] = v;
        }
        for (int vertexId : graph.getVertexSerials().keySet()) {
            distance[vertexId][vertexId] = 0;
            path[vertexId][vertexId] = vertexId;
        }

        for (int k = 0; k < n; ++k) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (distance[i][j] > distance[i][k] + distance[k][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                        path[i][j] = path[i][k];
                    }
                }
            }
        }

        FloydWarshallResult result = new FloydWarshallResult(n);
        result.setDistance(distance);
        result.setPath(path);

        return result;
    }
}
