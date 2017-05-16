package com.example.android.campusapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;

import java.util.concurrent.ExecutionException;
import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.HEART;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;

/**
 * Created by fridakornsater on 2017-04-19.
 */
public class favorites extends student_SlidingMenuActivity {
    ListView firstRow;
    ListView secondRow;
    ListView thirdRow;

    ProgressDialog dialog;
    RecyclerView  mRecyclerView;
    String status;
    private Date dateTime;

    ImageView imageView;
    ImageView fav_heart;

    private ArrayList<HashMap<String, String>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.favorites, null);
        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println(token);

        /*----------------------------------------------*/


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();

        try { String status = (myCallback.execution_Get("http://130.243.199.160:8000/events/my-favourites/", token, "GET", "No JsonData"));

            if (status == "false"){
                Toast.makeText(favorites.this, "could not fetch events", Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray myFavoritesArray = new JSONArray(status);

                JSONObject json_data = myFavoritesArray.getJSONObject(myFavoritesArray.length()-1);

                JSONArray favoritesItemsArray = json_data.getJSONArray("favorites");

                System.out.println(favoritesItemsArray);


                ListView listView = (ListView) findViewById(R.id.favorite_list);


                /* --- create hash map that all Json objects are inserted to --- */
                list=new ArrayList<HashMap<String,String>>();

                student_ListViewAdapter adapter;

                /*create as many hash maps as needed */
                for(int i = 0; i < favoritesItemsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }


                for (int i = 0; i < favoritesItemsArray.length(); i++) {

                    JSONObject items = favoritesItemsArray.getJSONObject(i);
                    String date = items.getString("date");
                    String name = items.getString("name_event");
                    String start_time = items.getString("start_time");
                    String end_time = items.getString("stop_time");
                   // String owner = items.getString("owner");
                    String description = items.getString("description");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,name );

                    list.get(i).put(DESCRIPTION, description);


                }
                adapter=new student_ListViewAdapter(this, list, listView);
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

