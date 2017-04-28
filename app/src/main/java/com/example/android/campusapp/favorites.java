package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

/**
 * Created by fridakornsater on 2017-04-19.
 */

public class favorites extends student_SlidingMenuActivity {
    ListView firstRow;
    ListView secondRow;
    ListView thirdRow;

    String url = "http://www.thecrazyprogrammer.com/example_data/fruits_array.json";
    ProgressDialog dialog;
    RecyclerView  mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.favorites, null);

        drawer.addView(contentView, 0);


        firstRow = (ListView)findViewById(R.id.most_favorite_list);
      /* secondRow = (ListView)findViewById(R.id.lista2);
        thirdRow = (ListView)findViewById(R.id.lista3);*/
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(favorites.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray favoritesArray = object.getJSONArray("fruits");
            ArrayList al1 = new ArrayList();
            ArrayList al2 = new ArrayList();
            ArrayList al3 = new ArrayList();



            for(int i = 0; i <favoritesArray.length(); ++i) {
                al1.add(favoritesArray.getString(i));

            }

          for(int i = 0; i < 3; ++i) {
                al2.add(favoritesArray.getString(i));
            }
            System.out.println(al2);



            for(int i = 0; i < 3; ++i) {
                al3.add(favoritesArray.getString(i));
            }
            System.out.println(al2);

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al1);
            firstRow.setAdapter(adapter);



           ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al2);
            secondRow.setAdapter(adapter2);

            ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al3);
            thirdRow.setAdapter(adapter3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

}

