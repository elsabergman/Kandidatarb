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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.R.id.organization_nameInput;
import static com.example.android.campusapp.R.id.textView2;
import static com.example.android.campusapp.R.layout.nav_header_sliding_menu;


public class student_SlidingMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {






    DrawerLayout drawer;
    Button btn;
    ProgressDialog dialog;
    //TextView firstname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.nav_header_sliding_menu);
        setContentView(R.layout.student_sliding_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = (Button) findViewById(R.id.logoutButton);

        //TextView firstname = (TextView) findViewById(R.id.textView2);
        //System.out.println("firstname is "+firstname.getText().toString());


        /* ------START Arvid fixat här men problem så kommenterar ut 9/5. Tanken är att ladda in namn in i headern i sliding menuen*/

       /* LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View nameview = inflater.inflate(R.layout.nav_header_sliding_menu, null);*/

       /* TextView firstname = (TextView) nameview.findViewById(R.id.textView2);
        TextView lastname = (TextView) nameview.findViewById(R.id.textView);
        System.out.println(firstname);
        System.out.println(lastname);*/
        //firstname.setText("AWESOME!!!!");
        //lastname.setText("AWESOMESSON");




        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println("token inside oncreate is "+token);
        /*----------------------------------------------*/




         //-------------- HÄR har ARVID pausat att försöka komma åt  textView2 i nav_header_sliding_menu för att det går skit.  Problemet är att skapa firstname. Återkommer efter klar med settings! 9/5
        /*TextView firstname = (TextView) findViewById(R.id.textView27);
        //TextView firstname = ((TextView) findViewById(R.id.nametextview111).findViewById(R.id.textView2));

        System.out.println("firstname outside try  is: " + firstname );

        //-----------Here we get data from database to display once the page is loaded!-------------
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        Callback myCallback = new Callback();
        try {
            String status = (myCallback.execution_Get("http://130.238.242.71:8000/profile/", token, "GET", "No JsonData"));
            System.out.println("status is "+status);

            if (status == "false"){
                Toast.makeText(student_SlidingMenuActivity.this, "could not fetch user info from database", Toast.LENGTH_LONG).show();
            }
            else {
                //Here we get separate objects from json string
                JSONObject myInfoObject = new JSONObject(status);
                System.out.println("This row is just after myinfoarray is created");

                String firstnameJson = myInfoObject.getString("first_name");

                System.out.println("first_name is "+firstnameJson);
                System.out.println("firstname inside try is: " + firstname );

                firstname.setText(firstnameJson);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();

        /*------------------Slut hämta data databasen till namnet i toppen -------------*/
        /* ------SLUT Arvid fixat här men problem så kommenterar ut 9/5 */



        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                System.out.println("HEJ");
                Intent intent = new Intent(student_SlidingMenuActivity.this, login.class);
                startActivity(intent);



            }

        });

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
