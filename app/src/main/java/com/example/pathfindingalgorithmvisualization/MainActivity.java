package com.example.pathfindingalgorithmvisualization;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    GridView grid;
    int algoClicked = 0;
    int startClicked = 0;
    int endClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) findViewById(R.id.start);
        Button end = (Button) findViewById(R.id.end);
        Button clear = (Button) findViewById(R.id.clear);
        Button dijkstra = (Button) findViewById(R.id.dijkstra);
        grid = (GridView) findViewById(R.id.grid);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grid.stopClicked=0;
                grid.startClicked=1;
                startClicked = 1;
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.startClicked=0;
                grid.stopClicked=1;
                endClicked = 1;
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop(); //Stop the process
                for(int r=0;r<grid.rows;r++) {
                    for (int c = 0; c < grid.cols; c++) {
                        grid.grid[r][c] = 0; //clear out all nodes
                    }
                }
                algoClicked = 0;
                startClicked = 0;
                endClicked = 0;
                grid.invalidate();
            }
        });
        dijkstra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(algoClicked == 0 && startClicked == 1 && endClicked == 1) {
                    grid.Dijkstra();
                    algoClicked = 1;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        if(grid.thread.getStatus()== AsyncTask.Status.RUNNING)
            grid.thread.cancel(true);
        super.onStop();
    }
}