package algorithm;

import graph.Edge;
import graph.Graph;
import graph.GraphValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Eulerization {

    private static final int INFINITE = 1_000_000;

    public void eulerizeGraph(final Graph graph) throws GraphValidationException {
        log.info("Eulerizing graph...");

        List<Integer> verticesWithOddDegree = graph.getVertexSerialsWithOddDegree();
        List<Integer> notPaired = new ArrayList<>(verticesWithOddDegree);

        FloydWarshallResult floydWarshallResult = floydWarshall(graph);
        int[][] distance = floydWarshallResult.getDistance();

        // TODO: run this from every vertex from verticesWithOddDegree, and choose the one with the minimal length
        for (int u : verticesWithOddDegree) {
            if (!notPaired.contains(u)) {
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
            notPaired.remove((Integer) u);
            notPaired.remove((Integer) min);

            duplicateEdges(graph, floydWarshallResult.getPathBetweenTwoVertices(u, min));

            log.debug("Paired [{}] to [{}], with [{}] distance.", u, min, minDistance);
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
