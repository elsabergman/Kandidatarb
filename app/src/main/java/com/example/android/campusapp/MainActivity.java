package com.example.android.campusapp;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by elsabergman on 2017-03-31.
 */

public class MainActivity extends AppCompatActivity{

        public void buttonOnClick(View v)
        {
            Intent launchactivity = new Intent(MainActivity.this, login.class);
            startActivity(launchactivity);
        }
    }