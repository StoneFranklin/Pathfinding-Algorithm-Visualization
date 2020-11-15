package com.example.pathfindingalgorithmvisualization;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;

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
                else if(algoClicked == 0 && startClicked == 0 && endClicked == 0) {
                    alertDialogShow(MainActivity.this, "Please select a start and end point.");
                }
                else if(algoClicked == 0 && startClicked == 0 && endClicked == 1) {
                    alertDialogShow(MainActivity.this, "Please select a start point.");
                }
                else if(algoClicked == 0 && startClicked == 1 && endClicked == 0) {
                    alertDialogShow(MainActivity.this, "Please select an end point.");
                }
            }
        });
    }

    public static void alertDialogShow(Context context, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        Button bq = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        bq.setBackgroundColor(Color.BLACK);
    }
    @Override
    protected void onStop() {
        if(grid.thread.getStatus()== AsyncTask.Status.RUNNING)
            grid.thread.cancel(true);
        super.onStop();
    }
}