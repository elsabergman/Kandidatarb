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

/**
 * Created by elsabergman on 2017-04-10.
 */

public class org_my_events extends SlidingMenuActivity {
    ListView firstRow;
    ListView secondRow;
    ListView thirdRow;

    ProgressDialog dialog;
    RecyclerView  mRecyclerView;
    String status;
    TextView textUser, descr, noEvents;
    JSONArray events;
    String serverUrl = "130.243.182.165";
    private Date dateTime;

    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> total_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_my_events, null);

        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println(token);

        /*----------------------------------------------*/

        TextView header = (TextView) findViewById(R.id.your_events);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();

        try { String status = (myCallback.execution_Get("http://"+serverUrl+":8000/events/my-events/", token, "GET", "No JsonData"));

            String default_options = (myCallback.execution_Get("http://"+serverUrl+":8000/profile/", token, "GET", "No JsonData"));

            JSONObject myInfoObject = new JSONObject(default_options);
            first_name = myInfoObject.getString("first_name");
            textUser = (TextView) findViewById(R.id.welcome);
            textUser.setText("Hello " + first_name + "!");
            events = myInfoObject.getJSONArray("my_events");

            descr = (TextView) findViewById(R.id.description_info);
            noEvents = (TextView) findViewById(R.id.description_list);
            if (events.length() > 0 ){
                descr.setText("Click on event to show further information");


            }
            else {
                noEvents.setText("Can't see any events below? Create your first event by clicking on 'Create Events' in the toggle menu!");
            }


            if (status == "false"){
                Toast.makeText(org_my_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray myEventsArray = new JSONArray(status);
                System.out.println("Events array " + myEventsArray);


                ListView listView = (ListView) findViewById(R.id.your_event_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                total_list = the list that will be displayed
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list=new ArrayList<HashMap<String,String>>();

                ListViewAdapter adapter;

                /*create as many hash maps as needed */
                for(int i = 0; i < myEventsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }

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
                    System.out.println(list + " list");


                    if ( url != null) {

                        list.get(i).put(URL, url);

                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                        list.get(i).put(CAMPUS_NAME, campus_name);


                    }

                    else {

                        list.get(i).put(URL, url);
                        list.get(i).put(CAMPUS_NAME, campus_name);
                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                    }


                   /* Log.d(name, "name");
                    Log.d(date, "date");
                    Log.d(start_time,"start");
                    Log.d(end_time, "end");
                    Log.d(description,"description");
                    Log.d(url, "external_url");
                    Log.d(campus_name, "campus_name");
                    Log.d(location_name, "campus_location_name");*/

                   // Log.d(id, "id");


                }

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




        dialog.dismiss();

        }

}

