package com.example.pathfindingalgorithmvisualization;

public class Edge {
    public Vertex source;
    public Vertex destination;
    public double weight;
    public Edge(Vertex source, Vertex destination, double weight){
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
}
