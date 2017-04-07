package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.example.android.campusapp.R.attr.windowActionBar;

/**
 * Created by Anna on 2017-04-04.
 */

public class add_event extends SlidingMenuActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(add_event.this, add_event.class);
        startActivity(intent);

        }
    }

