package algorithm;

import graph.Edge;
import graph.Graph;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Eulerization {

    public void eulerizeGraph() {
        log.info("Eulerizing graph...");
        // TODO
        log.info("Eulerizing finished");
    }

    public int[][] floydWarshall(final Graph graph) {
        final int n = graph.getVertexCount() + 1;
        final int[][] distance = new int[n][n];
        for (final Edge edge : graph.getEdges()) {
            final int u = edge.getVertexId1();
            final int v = edge.getVertexId2();
            distance[u][v] = edge.getDistance();
        }
        for (int vertexId : graph.getVertices().keySet()) {
            distance[vertexId][vertexId] = 0;
        }

        for (int k = 1; k < n; ++k) {
            for (int i = 1; i < n; ++i) {
                for (int j = 1; j < n; ++j) {
                    if (distance[i][j] > distance[i][k] + distance[k][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        return distance;
    }
}
