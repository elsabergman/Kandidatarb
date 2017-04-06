package com.example.android.campusapp;

import android.app.Activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;

import static android.R.attr.button;


/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends Activity{

    boolean clicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todays_events);

        TableLayout tableLayout = new TableLayout(getApplicationContext());
        TableRow tableRow;
        TextView textView;
        ImageView menu = (ImageView) findViewById(R.id.menu);
        final Intent intent = new Intent(this, toggle_menu.class);


        menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {

                                    @Override

                                    public void onClick(View arg0) {

                                        /*startActivity(intent);
                                        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);*/
                                    }
                                });



        /*menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.toggle_menu);


            }
        });*/

        
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

