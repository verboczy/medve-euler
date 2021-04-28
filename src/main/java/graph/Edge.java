package graph;

import lombok.Value;

@Value
public class Edge {
    int vertexId1;
    int vertexId2;
    int distance;
}
