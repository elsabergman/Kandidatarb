package com.example.android.campusapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;

/**
 * Created by Anna on 2017-05-10.
 */

public class student_ListViewAdapter {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    ToggleButton heart;
    TextView txtDescription;
    ListView listView;
    boolean isVisible;
    public student_ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list, ListView listView){
        super();
        this.activity=activity;
        this.list=list;
        this.listView = listView;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        final LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.column_rows, null);

            txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
            txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
            txtThird=(TextView) convertView.findViewById(R.id.startTime);

            heart=(ToggleButton) convertView.findViewById(R.id.fav_toggleButton);
            txtDescription = (TextView) convertView.findViewById((R.id.description));
            // listView = (ListView) convertView.findViewById(R.id.your_event_list);

        }
        final HashMap<String, String> map=list.get(position);



        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        heart.setText(map.get(FOURTH_COLUMN));
        //txtDescription.setText(map.get(DESCRIPTION));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);


                String myDescription = item.get("Description");
                System.out.println(myDescription);
                txtDescription = (TextView) view.findViewById((R.id.description));
                txtDescription.setText(myDescription);



                if ( txtDescription.getVisibility() == View.VISIBLE)
                {
                    txtDescription.setVisibility(View.GONE);
                    txtDescription.invalidate();

                }
                else
                {
                    txtDescription.setVisibility(View.VISIBLE);
                    txtDescription.invalidate();




                }

            }
        });


        return convertView;
    }

}
