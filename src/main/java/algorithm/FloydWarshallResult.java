package algorithm;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class FloydWarshallResult {
    private int n;
    private int[][] distance;
    private int[][] path;

    public FloydWarshallResult(final int n) {
        this.n = n;
        this.distance = new int[n][n];
        this.path = new int[n][n];
    }

    public List<Integer> getPathBetweenTwoVertices(final int u, final int v) {
        if (path[u][v] == -1) {
            return Collections.emptyList();
        }

        final List<Integer> road = new ArrayList<>();
        road.add(u);

        int nextVertex = u;
        while (nextVertex != v) {
            nextVertex = path[nextVertex][v];
            road.add(nextVertex);
        }

        return road;
    }
}