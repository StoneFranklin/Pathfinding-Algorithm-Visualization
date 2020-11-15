package com.example.pathfindingalgorithmvisualization;
//Connection between two vertexes.
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
