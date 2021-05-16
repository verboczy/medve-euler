package graph;

import lombok.Data;

@Data
public class Edge {
    private final int vertexId1;
    private final int vertexId2;
    private final int distance;
    private boolean visited;

    public Edge(int v1, int v2, int distance) {
        this.vertexId1 = v1;
        this.vertexId2 = v2;
        this.distance = distance;
        this.visited = false;
    }
}
