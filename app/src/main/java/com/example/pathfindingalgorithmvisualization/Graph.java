package com.example.pathfindingalgorithmvisualization;

import java.util.*;
public class Graph {
    public ArrayList<Vertex> vertex = new ArrayList<>(); //List of all vertexes

    public void addVertex(int id, double x, double y) { //Adds a new vertex to the list of all vertexes
        Vertex v1 = new Vertex(id, x, y);
        vertex.add(v1);
    }

    public void addEdge(Vertex source, Vertex destination, double weight) { // Adds an edge or connection to the source Vertex's edges list.
        source.edges.add(new Edge(source, destination, weight));
    }

    public Vertex getV(double x, double y) { //Returns a vertex if it exists in the list of all vertexes.
        for (int i = 0; i < vertex.size(); i++)
            if (vertex.get(i).x == x && vertex.get(i).y == y)
                return vertex.get(i);
        return null;
    }
}