package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


import static com.example.android.campusapp.Constants.CAMPUS_LOCATION_NAME;
import static com.example.android.campusapp.Constants.CAMPUS_NAME;
import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.EDIT;
import static com.example.android.campusapp.Constants.FAVORITES;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.ID;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;
import static com.example.android.campusapp.Constants.URL;

/** Name: org_my_events.java
 * Author: Elsa Bergman and Frida Kornsäter
 * Connects to: ListViewAdapter.java
 *
 * This class fetches all events that an organisation user has made and allows the user to see the events that he or she has made in
 * order by when in time they will happen. This class adds all information that is going to be displayed in lists to hash maps and
 * later calls ListViewAdapter.java with the hash maps as a parameter */

public class org_my_events extends SlidingMenuActivity {

    /* instance variables */
    String status;
    TextView textUser, descr, noEvents;
    JSONArray events;

    String serverUrl = "130.243.180.38";
    private ArrayList<HashMap<String, String>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        /* launch layout with sliding menu */
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_my_events, null);

        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/

        /* ---- Font implementation for header ---- */
        TextView header = (TextView) findViewById(R.id.your_events);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);
        /*----------------------------------------------*/


        /* fetch all events that the user has created on his or her account */
        Callback myCallback = new Callback();
        try { String status = (myCallback.execution_Get("http://"+serverUrl+":8000/events/my-events/", token, "GET", "No JsonData"));

            /* fetch profile information about the user */
            String default_options = (myCallback.execution_Get("http://"+serverUrl+":8000/profile/", token, "GET", "No JsonData"));

            /* save profile information as json object and retrieve first name in order to display the user's first name on top
            of the page */
            JSONObject myInfoObject = new JSONObject(default_options);
            first_name = myInfoObject.getString("first_name");
            textUser = (TextView) findViewById(R.id.welcome);
            textUser.setText("Hello " + first_name + "!");
            events = myInfoObject.getJSONArray("my_events");

            /*  This code checks what type of message should be displayed on the page. If the user has not created any events,
            a text telling them how to create an event will appear. Otherwise, a text that tells the user how to show more information
            about an event will appear.
             */
            descr = (TextView) findViewById(R.id.description_info);
            noEvents = (TextView) findViewById(R.id.description_list);
            if (events.length() > 0 ){
                descr.setText("Click on event to show further information");

            }
            else {
                noEvents.setText("Can't see any events below? Create your first event by clicking on 'Create Events' in the toggle menu!");
            }



            /* if the DatabaseManager class returns false, we want to let the user know that the events could not be fetched */
            if (status == "false"){
                Toast.makeText(org_my_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray myEventsArray = new JSONArray(status);

                ListView listView = (ListView) findViewById(R.id.your_event_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list=new ArrayList<HashMap<String,String>>();

                ListViewAdapter adapter; // the adapter that displays the events in different columns.

                /*create as many hash maps as needed */
                for(int i = 0; i < myEventsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }
                /* add information to the hashmap, one hashmap for each event */
                for (int i = 0; i < myEventsArray.length(); i++) {
                    JSONObject json_data = myEventsArray.getJSONObject(i);
                    String date = json_data.getString("date");
                    String name = json_data.getString("name_event");
                    String start_time = json_data.getString("start_time");
                    String end_time = json_data.getString("stop_time");
                    String owner = json_data.getString("owner");
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    String location_name = json_data.getString("campus_location_name");
                    String campus_name = json_data.getString("campus_name");
                    String id_event =json_data.getString("id");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "-" +end_time );
                    list.get(i).put(THIRD_COLUMN,name);
                    list.get(i).put(DESCRIPTION, description);
                    list.get(i).put(EDIT,"Edit or remove event");
                    list.get(i).put(ID,id_event);
                    list.get(i).put(URL, url);
                    list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                    list.get(i).put(CAMPUS_NAME, campus_name);


                    }

                /* connect to listViewAdapter which will display all events in the way specified in ListViewAdapter.java */
                adapter=new ListViewAdapter(this, list, listView);
                listView.setAdapter(adapter);

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        }

}

