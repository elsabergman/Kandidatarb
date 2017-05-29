package com.example.android.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class student_SlidingMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawer;
    Button btn;
    ProgressDialog dialog;
    private View navHeader;
    String first_name, campus_name;
    private TextView textName, textCampus;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_sliding_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = (Button) findViewById(R.id.logoutButton);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        textName = (TextView) navHeader.findViewById(R.id.header_logged_in);
        textCampus = (TextView) navHeader.findViewById(R.id.myChosenCampus);

           /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/
        Callback myCallback = new Callback();

        try {

            String status = (myCallback.execution_Get("http://212.25.147.115:8000/profile/", token , "GET", "No JsonData"));

            JSONObject myProfile = new JSONObject(status);
            first_name = myProfile.getString("first_name");
            campus_name = myProfile.getJSONObject("campus").getString("campus_name");


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textName.setText(first_name);
        textCampus.setText(campus_name);


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(student_SlidingMenuActivity.this, login.class);
                startActivity(intent);

            }

        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            // Handle the camera action

            Intent intent = new Intent(student_SlidingMenuActivity.this, todays_events.class);
            startActivity(intent);



        }
        else if (id == R.id.nav_favourites) {
            Intent intent = new Intent(student_SlidingMenuActivity.this, favorites.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_liveFeed) {
            Intent intent = new Intent(student_SlidingMenuActivity.this, student_livefeed.class);
            startActivity(intent); //Skicka vidare till live feed
        }
        else if (id == R.id.nav_campus) {
            Intent intent = new Intent(student_SlidingMenuActivity.this, student_campus_information.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_manage) {
            Intent intent = new Intent(student_SlidingMenuActivity.this, student_settings.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_support) {
            Intent intent = new Intent(student_SlidingMenuActivity.this, student_support_page.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
