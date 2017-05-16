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
import android.widget.ImageView;
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
    TextView txtURL;
    TextView txtCampus;
    TextView txtRoom;

    ListView listView;
    boolean isVisible;
    public ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list, ListView listView){
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
           // txtFourth=(TextView) convertView.findViewById(R.id.owner);
            txtDescription = (TextView) convertView.findViewById((R.id.description));
            txtURL = (TextView) convertView.findViewById((R.id.url));
            txtCampus = (TextView) convertView.findViewById((R.id.campus_name));
            txtRoom = (TextView) convertView.findViewById((R.id.location_place_room));

           // listView = (ListView) convertView.findViewById(R.id.your_event_list);


        }


        final HashMap<String, String> map=list.get(position);
        txtFirst.setTextColor(Color.DKGRAY);
        txtSecond.setTextColor(Color.DKGRAY);
        txtThird.setTextColor(Color.DKGRAY);


        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));




        //txtFourth.setText(map.get(FOURTH_COLUMN));
        //txtDescription.setText(map.get(DESCRIPTION));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                System.out.println(item);

                String myDescription = item.get("Description");
                String myUrl = item.get("Url");
                String myCampus = item.get("campus_name");
                String myRoom = item.get("campus_location_name");

                System.out.println("My location" + myCampus);
                System.out.println("My room" + myRoom);


                txtDescription = (TextView) view.findViewById((R.id.description));
                txtURL = (TextView) view.findViewById((R.id.url));
                txtCampus = (TextView) view.findViewById((R.id.campus_name));
                txtRoom = (TextView) view.findViewById((R.id.location_place_room));


                txtDescription.setTextColor(Color.DKGRAY);
                txtDescription.setText("Description: " + myDescription + "     " + myUrl );
                //txtURL.setText(myUrl);
                //txtURL.setLinkTextColor(Color.DKGRAY);

                txtCampus.setTextColor(Color.DKGRAY);
                txtCampus.setText("Campus: " +myCampus);

                txtRoom.setTextColor(Color.DKGRAY);
                txtRoom.setText("Location: " +myRoom);

               /* if (myUrl!= ""){

                    txtURL.setText(myUrl);
                }

                else {
                    txtURL.setText(" ");
                }*/

                if ( (txtDescription.getVisibility() == View.VISIBLE)  )
                {

                    txtDescription.setVisibility(View.GONE);
                  // txtURL.setVisibility(View.GONE);
                    txtCampus.setVisibility(View.GONE);
                    txtRoom.setVisibility(View.GONE);

                    txtDescription.invalidate();
                    txtCampus.invalidate();
                    txtRoom.invalidate();

                   // txtURL.invalidate();

                    view.setBackgroundColor(Color.WHITE);

                }
              else
                {
                    txtDescription.setVisibility(View.VISIBLE);
                   //txtURL.setVisibility(View.VISIBLE);
                    txtCampus.setVisibility(View.VISIBLE);
                    txtRoom.setVisibility(View.VISIBLE);

                   txtDescription.invalidate();
                   // txtURL.invalidate();
                    txtCampus.invalidate();
                    txtRoom.invalidate();


                    view.setBackgroundResource(R.color.very_light_grey);




                }

            }
        });


        return convertView;
    }

}

