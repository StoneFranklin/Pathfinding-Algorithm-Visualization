package com.example.pathfindingalgorithmvisualization;

import java.util.*;
public class  Vertex implements Comparable<Vertex>{
    public int id;
    public Vertex parent = null;
    public double d_value = Double.POSITIVE_INFINITY;
    public LinkedList<Edge> edges = new LinkedList<>();
    public boolean discovered = false;
    public double x,y; //grid coordinates

    public Vertex(int id,double x,double y){
        this.id=id;
        this.x=x;
        this.y=y;
    }

    @Override
    public int compareTo(Vertex vertex2) {
        return Double.compare(this.d_value, vertex2.d_value);
    }
}
