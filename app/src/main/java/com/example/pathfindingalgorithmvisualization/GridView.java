package com.example.pathfindingalgorithmvisualization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class GridView extends View {
    int rows = 16;
    int cols = 10;
    float tileWidth;
    float tileHeight;
    float width;
    float height;
    int[][] grid = new int[rows][cols]; //0-empty, 1-start, 2-end, 3-wall, 4-in set, 5-in queue, 6-in shortest path
    int startClicked = 0;
    int start_x, start_y;
    int stopClicked = 0;
    int stop_x, stop_y;
    final int milliseconds = 1;

    Graph graph;
    Paint paint = new Paint();
    performAlgorithm thread = new performAlgorithm();

    public GridView(Context context) {
        super(context);
        init(null, 0);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas) { //Draws the grid
        super.onDraw(canvas);
        tileWidth = width / cols;
        tileHeight = height / rows;
        int border = 0;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                paint.setStyle(Paint.Style.FILL);
                int value = grid[r][c];
                switch (value) {
                    case 0: //empty
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.BLACK);
                        break;
                    case 1: //start
                        paint.setColor(Color.RED);
                        break;
                    case 2: //end
                        paint.setColor(Color.GREEN);
                        break;
                    case 3: //wall
                        paint.setColor(Color.BLACK);
                        break;
                    case 4: //in set
                        paint.setColor(Color.CYAN);
                        border = 1;
                        break;
                    case 5: //in queue
                        paint.setColor(Color.LTGRAY);
                        border = 1;
                        break;
                    case 6: //in shortest path
                        paint.setColor(Color.YELLOW);
                        border = 1;
                        break;
                }
                canvas.drawRect(c * tileWidth, r * tileHeight, c * tileWidth + tileWidth, r * tileHeight + tileHeight, paint);
                if (border == 1) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK);
                    canvas.drawRect(c * tileWidth, r * tileHeight, c * tileWidth + tileWidth, r * tileHeight + tileHeight, paint);
                }
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //Determines the height and width of the view
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) { //Handles user interaction with the grid
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int c = (int) (x / tileWidth);
            int r = (int) (y / tileHeight);
            if (startClicked == 1) {
                grid[start_y][start_x] = 0;
                grid[r][c] = 1;
                start_x = c;
                start_y = r;
                startClicked = 0;
            } else if (stopClicked == 1) {
                grid[stop_y][stop_x] = 0;
                grid[r][c] = 2;
                stop_x = c;
                stop_y = r;
                stopClicked = 0;
            } else if (grid[r][c] != 1 && grid[r][c] != 2) {
                if (grid[r][c] != 3)
                    grid[r][c] = 3;
                else
                    grid[r][c] = 0;
            }
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int c = (int) (x / tileWidth);
            int r = (int) (y / tileHeight);
            if (x > width || y > height || x < 0 || y < 0)
                return false;//pusho se degjuari ACTION_MOVE nqs dole jashte kufijve te view.
            if (grid[r][c] != 1 && grid[r][c] != 2)
                if (grid[r][c] != 3)
                    grid[r][c] = 3;
        }
        invalidate(); //Forces a redraw
        return true;
    }

    public void Dijkstra() { //Executes the algorithm
        getGraph();
        thread = new performAlgorithm();
        thread.execute();
        getGraph();
    }

    public void getGraph() { //Creates a vertex for every value in the matrix and adds all vertexes' edges to their respective list of edges.
        Graph g = new Graph();
        int counter = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 3)
                    g.addVertex(counter, c, r); //If it is not a wall, add the vertex to the list of all vertexes
                if (grid[r][c] == 4 || grid[r][c] == 5 || grid[r][c] == 6)
                    grid[r][c] = 0;
                counter++;
            }
        }
        int x1, x2, x3, x4;
        int y1, y2, y3, y4;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 3)
                    continue;
                // VERTEX BELOW
                x1 = c;
                y1 = r - 1;

                //VERTEX TO THE LEFT
                x2 = c - 1;
                y2 = r;

                //VERTEX TO THE RIGHT
                x3 = c + 1;
                y3 = r;

                //VERTEX ABOVE
                x4 = c;
                y4 = r + 1;

                if (x1 >= 0 && x1 < cols && y1 >= 0 && y1 < rows)
                    if (grid[y1][x1] != 3)
                        //If the vertex below the current vertex exists and is not a wall, add it to the current vertex's edges list.
                        g.addEdge(g.getV(c, r), g.getV(x1, y1), 1);
                if (x2 >= 0 && x2 < cols && y2 >= 0 && y2 < rows)
                    if (grid[y2][x2] != 3)
                        //If the vertex to the left of the current vertex exists and is not a wall, add it to the current vertex's edges list.
                        g.addEdge(g.getV(c, r), g.getV(x2, y2), 1);
                if (x3 >= 0 && x3 < cols && y3 >= 0 && y3 < rows)
                    if (grid[y3][x3] != 3)
                        //If the vertex to the right of the current vertex exists and is not a wall, add it to the current vertex's edges list.
                        g.addEdge(g.getV(c, r), g.getV(x3, y3), 1);
                if (x4 >= 0 && x4 < cols && y4 >= 0 && y4 < rows)
                    if (grid[y4][x4] != 3)
                        //If the vertex above the current vertex exists and is not a wall, add it to the current vertex's edges list.
                        g.addEdge(g.getV(c, r), g.getV(x4, y4), 1);
            }
        }
        graph = g;
        invalidate(); //Forces a redraw
    }

    public class performAlgorithm extends AsyncTask<Void, Void, Void> { //Actual algorithm
        protected Void doInBackground(Void... params) {
            Vertex start = graph.getV(start_x, start_y); //Start Vertex
            Vertex destination = graph.getV(stop_x, stop_y); //End Vertex
            LinkedList<Vertex> set = new LinkedList<>();
            PriorityQueue<Vertex> queue = new PriorityQueue<>();
            queue.add(start);
            start.d_value = 0;

            //cycle until queue is empty or destination has been inserted into the set
            while (!queue.isEmpty()) {
                if (isCancelled()) break;
                Vertex current = queue.poll();
                current.discovered = true;//kur eshte ne set dhe tashme d_value eshte percaktuar
                set.add(current);
                if (grid[(int) current.y][(int) current.x] != 1 && grid[(int) current.y][(int) current.x] != 2)
                    grid[(int) current.y][(int) current.x] = 4;
                publishProgress();

                try {
                    Thread.sleep(milliseconds); //Sleep the thread to slow down the visualization.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (current == destination) { //We have reached the end vertex. Exit the while loop.
                    break;
                }

                //Iterate through each of the current vertex's edges
                for (int i = 0; i < current.edges.size(); i++) {
                    Edge currentEdge = current.edges.get(i);
                    Vertex currentNeighbor = currentEdge.destination;
                    if (currentNeighbor.discovered == false) {
                        if (grid[(int) currentNeighbor.y][(int) currentNeighbor.x] != 1 && grid[(int) currentNeighbor.y][(int) currentNeighbor.x] != 2)
                            grid[(int) currentNeighbor.y][(int) currentNeighbor.x] = 5;
                        publishProgress();
                        try {
                            Thread.sleep(milliseconds); //Sleep the thread to slow down the visualization.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (currentNeighbor.d_value > current.d_value + currentEdge.weight) {
                            currentNeighbor.d_value = current.d_value + currentEdge.weight;
                            currentNeighbor.parent = current;
                            queue.remove(currentNeighbor); //Remove the currentNeighbor if it is already in the queue
                            queue.add(currentNeighbor); //Insert the currentNeighbor with updated d_value and parent vertex
                        }
                    }
                }
            }
            Vertex current = destination;
            //Retrace steps starting with destination until you get to the starting vertex.
            while (current != null) {
                if (isCancelled()) break;
                if (grid[(int) current.y][(int) current.x] != 1 && grid[(int) current.y][(int) current.x] != 2)
                    grid[(int) current.y][(int) current.x] = 6;
                publishProgress();
                try {
                    Thread.sleep(milliseconds); //Sleep the thread to slow down the visualization.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current = current.parent;
            }
            return null;
        }

        protected void onProgressUpdate(Void...args) { //Run by using method publishProgress()
            invalidate();
        }
    }
}
