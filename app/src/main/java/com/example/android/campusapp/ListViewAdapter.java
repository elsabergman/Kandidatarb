package com.example.android.campusapp;

/**
 * Created by elsabergman on 2017-05-02.
 */

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by elsabergman on 2017-05-02.
 */


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;


public class ListViewAdapter extends BaseAdapter {


    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    TextView txtDescription;
    ListView listView;
    boolean isVisible;
    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list, ListView listView){
        super();
        this.activity=activity;
        this.list=list;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        final LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.column_rows, null);

            txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
            txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
            txtThird=(TextView) convertView.findViewById(R.id.Time);
            txtFourth=(TextView) convertView.findViewById(R.id.owner);
            txtDescription = (TextView) convertView.findViewById((R.id.description));

           // listView = (ListView) convertView.findViewById(R.id.your_event_list);

        }
        final HashMap<String, String> map=list.get(position);



        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));
        //txtDescription.setText(map.get(DESCRIPTION));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

                String myDescription = item.get("Description");
                System.out.println(myDescription);
                txtDescription = (TextView) view.findViewById((R.id.description));
                txtDescription.setText("Description: " + myDescription);




                if ( txtDescription.getVisibility() == View.VISIBLE)
                {
                    txtDescription.setVisibility(View.GONE);
                    txtDescription.invalidate();
                    view.setBackgroundColor(Color.WHITE);

                }
              else
                {
                    txtDescription.setVisibility(View.VISIBLE);

                    txtDescription.invalidate();
                    view.setBackgroundResource(R.color.very_light_grey);




                }

            }
        });


        return convertView;
    }

}

