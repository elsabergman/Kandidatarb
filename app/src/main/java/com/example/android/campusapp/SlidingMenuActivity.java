package com.example.android.campusapp;


/* Name: SlidingMenuActivity.java
* Author: Elsa Bergman
* Connects to: All pages in the system that an organisation user can see when the user is logged in.
*
* In this class, the sliding menu present on every page that the user can see when he or she is logged in is implemented. This class
* implements a sliding menu with items that the user can click in order to get redirected to new pages. The items are the names of the
* pages that an organisation user can see. At the bottom of the sliding menu, there is a log out button that the user can press to log
* out of the system. On top of the sliding menu, the first name of the user as well as the user's default campus is visible.
*
* All pages that the user can access the sliding menu from has an extension of the SlidingMenuActivity class in their java files.

 */

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class SlidingMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName, txtCampus;
    Button btn;
    private NavigationView navigationView;
    DrawerLayout drawer;
    private View navHeader;
    String first_name, campus_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sliding_menu);

        /*get Toolbar widget from app_bar_sliding_menu.xml */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* log out button implementation */
        Button btn = (Button) findViewById(R.id.logoutButton);

        /* implement navigationView in order to show first_name and default campus of user logged in  */
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.header_logged_in);
        txtCampus = (TextView) navHeader.findViewById(R.id.myChosenCampus);

           /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/
        Callback myCallback = new Callback();

        try {
            /*fetch profile information about user logged in */
            String status = (myCallback.execution_Get("http://130.243.180.38:8000/profile/",token , "GET", "No JsonData"));


            /* save profile information as Json object and retrieve first name and default campus of user logged in */
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

        /* display first name and default campus of user logged in */
        txtName.setText(first_name);
        txtCampus.setText(campus_name);

        /* if user clicks on log out button, redirect to login page */
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(SlidingMenuActivity.this, login.class);
                startActivity(intent);

            }

        });


        /* implement drawer (Sliding Menu) and navigation listener that will listen to see if navigation drawer is opened or not  */
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




    /* handles all items in navigation drawer and what happens if one of them is clicked */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_myEvents) {

            Intent intent = new Intent(SlidingMenuActivity.this, org_my_events.class);
            startActivity(intent);
        } else if (id == R.id.nav_addEvents) {
            Intent intent = new Intent(SlidingMenuActivity.this, add_event.class);
            startActivity(intent);
        } else if (id == R.id.nav_campus) {
            Intent intent = new Intent(SlidingMenuActivity.this, org_campus_information.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(SlidingMenuActivity.this, org_settings.class);
            startActivity(intent);
        } else if (id == R.id.nav_support) {
            Intent intent = new Intent(SlidingMenuActivity.this, org_support_page.class);
            startActivity(intent);
        }

        /*close drawer */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
