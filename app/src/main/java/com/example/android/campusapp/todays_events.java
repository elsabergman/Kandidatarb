package com.example.android.campusapp;

import android.app.Activity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by elsabergman on 2017-03-31.
 */

public class todays_events  extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todays_events);

        TableLayout tableLayout = new TableLayout(getApplicationContext());
        TableRow tableRow;
        TextView textView;

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.toggle_menu);
            }
        });

        
        /*for (int i = 0; i < 4; i++) {
            textView = new TextView(getApplicationContext());
            textView.setText("test");
        }
       
        
        for (int i = 0; i < 4; i++) {
            tableRow = new TableRow(getApplicationContext());
            for (int j = 0; j < 3; j++) {
                textView = new TextView(getApplicationContext());
                textView.setText("test");
                textView.setPadding(20, 20, 20, 20);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
        setContentView(tableLayout);*/

    }
}

