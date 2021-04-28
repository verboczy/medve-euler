package main;

import algorithm.EulerCircle;
import graph.Edge;
import graph.Graph;

public class Main {

    public static void main(final String[] args) {
        System.out.println("Hello world!");
        final Graph graph = new Graph(8);
//        graph.addVertex(new Vertex(1, "foo"));
//        graph.addEdge(new Edge(1, 2, 5));
//        graph.addVertex(new Vertex(2, "bar"));
//        graph.addEdge(new Edge(2, 3, 5));
//        graph.addVertex(new Vertex(3, "asd"));

//        graph.addEdge(new Edge(1, 0, 2));
//        graph.addEdge(new Edge(1, 0, 2));
//        graph.addEdge(new Edge(0, 2, 2));
//        graph.addEdge(new Edge(2, 1, 2));
//        graph.addEdge(new Edge(0, 3, 2));
//        graph.addEdge(new Edge(3, 4, 2));
//        graph.addEdge(new Edge(3, 2, 2));
//        graph.addEdge(new Edge(3, 1, 2));
//        graph.addEdge(new Edge(2, 4, 2));

//        graph.addEdge(new Edge(0, 1, 2));
//        graph.addEdge(new Edge(0, 2, 2));
//        graph.addEdge(new Edge(0, 3, 2));
//        graph.addEdge(new Edge(0, 4, 2));
//        graph.addEdge(new Edge(1, 2, 2));
//        graph.addEdge(new Edge(1, 3, 2));
//        graph.addEdge(new Edge(1, 4, 2));
//        graph.addEdge(new Edge(2, 3, 2));
//        graph.addEdge(new Edge(2, 4, 2));
//        graph.addEdge(new Edge(3, 4, 2));

        graph.addEdge(new Edge(1, 1, 2));
        graph.addEdge(new Edge(1, 2, 2));
        graph.addEdge(new Edge(1, 3, 2));
        graph.addEdge(new Edge(1, 4, 2));
        graph.addEdge(new Edge(1, 8, 2));
        graph.addEdge(new Edge(2, 3, 2));
        graph.addEdge(new Edge(2, 5, 2));
        graph.addEdge(new Edge(2, 5, 2));
        graph.addEdge(new Edge(3, 4, 2));
        graph.addEdge(new Edge(3, 6, 2));
        graph.addEdge(new Edge(4, 7, 2));
        graph.addEdge(new Edge(4, 7, 2));
        graph.addEdge(new Edge(5, 6, 2));
        graph.addEdge(new Edge(5, 8, 2));
        graph.addEdge(new Edge(6, 7, 2));
        graph.addEdge(new Edge(6, 8, 2));
        graph.addEdge(new Edge(7, 8, 2));
        graph.addEdge(new Edge(8, 8, 2));

        EulerCircle eulerCircle = new EulerCircle(graph);


        eulerCircle.computeEulerCircle(2);
    }
}
