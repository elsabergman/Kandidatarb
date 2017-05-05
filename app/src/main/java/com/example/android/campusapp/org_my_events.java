package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;

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


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();
        try { String status = (myCallback.execution_Get("http://130.238.250.84:8000/events/my-events/", token, "GET", "No JsonData"));

            if (status == "false"){
                Toast.makeText(org_my_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray myEventsArray = new JSONArray(status);


                ListView listView = (ListView) findViewById(R.id.your_event_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                total_list = the list that will be displayed
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list=new ArrayList<HashMap<String,String>>();
                total_list=new ArrayList<HashMap<String,String>>();
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
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,owner );
                    list.get(i).put(FOURTH_COLUMN, name );
                    list.get(i).put(DESCRIPTION, description);
                    total_list.add(list.get(i));



                    Log.d(name, "name");
                    Log.d(date, "date");
                    Log.d(start_time,"start");
                    Log.d(end_time, "end");
                    Log.d(description,"description");


                }

                adapter=new ListViewAdapter(this, list, listView);
                System.out.println(list);
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

