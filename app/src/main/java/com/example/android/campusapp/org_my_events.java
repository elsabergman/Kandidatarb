package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by elsabergman on 2017-04-10.
 */

public class org_my_events extends SlidingMenuActivity {
    ListView firstRow;
    ListView secondRow;
    ListView thirdRow;
    String url = "http://www.thecrazyprogrammer.com/example_data/fruits_array.json";
    ProgressDialog dialog;
    RecyclerView  mRecyclerView;
    String status;


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

        firstRow = (ListView)findViewById(R.id.your_event_list);
      /* secondRow = (ListView)findViewById(R.id.lista2);
        thirdRow = (ListView)findViewById(R.id.lista3);*/
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();



        Callback myCallback = new Callback();
        try {

            String status = (myCallback.execution_Get("http://130.243.134.165/events/my-events/", token, "GET", "No JsonData"));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



         /*   JSONObject object = new JSONObject(status);
            JSONArray myEventsArray = object.getJSONArray(status);
            ArrayList al1 = new ArrayList();
            ArrayList al2 = new ArrayList();
            ArrayList al3 = new ArrayList();



            for(int i = 0; i <myEventsArray.length(); ++i) {
                al1.add(myEventsArray.getString(i));
            }

         /*   for(int i = 0; i < 3; ++i) {
                al2.add(myEventsArray.getString(i));
            }
            System.out.println(al2);

            for(int i = 0; i < 3; ++i) {
                al3.add(myEventsArray.getString(i));
            }
            System.out.println(al2);*/

           /* ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al1);
            firstRow.setAdapter(adapter); */


          /*  ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al2);
            secondRow.setAdapter(adapter2);

            ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al3);
            thirdRow.setAdapter(adapter3);*/

        dialog.dismiss();
    }

}

