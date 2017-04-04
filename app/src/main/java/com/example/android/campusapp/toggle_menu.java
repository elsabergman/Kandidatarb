package com.example.android.campusapp;


/**
 * Created by elsabergman on 2017-03-31.
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class toggle_menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toggle_menu);

        ImageView add_event_icon = (ImageView) findViewById(R.id.add_events);
        add_event_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.add_event);
            }
        });

        ImageView add_event_icon = (ImageView) findViewById(R.id.add_events);
        add_event_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.add_event);
            }
        });

    }



}



