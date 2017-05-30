package com.example.android.campusapp;

/**
 * Name: ListViewAdapter.java
 * Authors: Elsa Bergman and Frida Kornsäter
 *Connects to: org_my_events.java (displays events retrieved from database in list form), Constants.java (handles all column rows), EditEvent.java
 *  (if user wants to edit event they are sent to this class)
 *
 *  This class is heavily connected to org_my_events.java and displays the events that the user has created in list form. This class
 *  is called once for every event that the user has created and will display the event information in different columns. It handles
 *  the on click functionality of the list row and thus displays further information when a list row is clicked.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;


public class ListViewAdapter extends BaseAdapter {

    /* instance variables */
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst,txtSecond, txtThird, txtFourth, txtDescription,txtURL,txtCampus,txtRoom, txtEdit;
    ListView listView;
    private static org_my_events Org_my_events;
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


    /*fetches the view of the clicked list row */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.column_rows, null);

        } else {
            convertView = convertView;
        }

        /* The different texts that will be displayed as columns in the system */
        txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
        txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
        txtThird=(TextView) convertView.findViewById(R.id.Time);
        txtFourth=(TextView) convertView.findViewById(R.id.owner);


        final HashMap<String, String> map=list.get(position); // hash map with information sent from org_my_events.java

        /* set color of text in columns */
        txtFirst.setTextColor(Color.DKGRAY);
        txtSecond.setTextColor(Color.DKGRAY);
        txtThird.setTextColor(Color.DKGRAY);

        /* display the text from the hash map */
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));

        /* if a list row is clicked, further information about the event will be displayed, as well as a clickable link that lets
        the user edit or remove the clicked event */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position); // the hash map for the clicked event

                /* fetch information that will be displayed on click from the hash map */
                String myDescription = item.get("Description");
                String myName = item.get("Third");
                String myUrl = item.get("Url");
                final String id_event = item.get("id");
                String myCampus = item.get("campus_name");
                String myRoom = item.get("campus_location_name");

                /*texts that will be displayed when the user clicks on a specific list row */
                txtDescription = (TextView) view.findViewById((R.id.description));
                txtURL = (TextView) view.findViewById((R.id.url));
                txtCampus = (TextView) view.findViewById((R.id.campus_name));
                txtRoom = (TextView) view.findViewById((R.id.location_place_room));
                txtEdit = (TextView) view.findViewById(R.id.edit);

                /* underlines the clickable link to make it more obvious that it is clickable */
                SpannableString content = new SpannableString(item.get("edit"));
                content.setSpan(new UnderlineSpan(), 0, item.get("edit").length(), 0);
                txtEdit.setText(content);

                /* set text and color of text that will be visible when lis row is clicked */
                txtDescription.setTextColor(Color.DKGRAY);
                txtDescription.setText("Description: " + myDescription + "     " + myUrl );
                txtCampus.setTextColor(Color.DKGRAY);
                txtCampus.setText("Campus: " +myCampus);
                txtRoom.setTextColor(Color.DKGRAY);
                txtRoom.setText("Location: " +myRoom);

                /* if the text is already visible, we want to hide the text on click and decrease the list row height */
                if ( (txtDescription.getVisibility() == View.VISIBLE)  )
                {

                    txtDescription.setVisibility(View.GONE);
                    txtCampus.setVisibility(View.GONE);
                    txtRoom.setVisibility(View.GONE);
                    txtEdit.setVisibility(View.GONE);
                    txtDescription.invalidate();
                    txtCampus.invalidate();
                    txtRoom.invalidate();
                    /* if the list only contains one event, the list will expand more on click and thus decrease more if clicked again
                    This is to make it easier for the user to read the content of the list */
                    if (list.size() < 2) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listView.getWidth(), 145);
                        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        lp.setMargins(0, 600, 0, 0);
                        listView.setLayoutParams(lp); // display the layout
                    }


                    view.setBackgroundColor(Color.WHITE); // if list row doesn't show all information and thus isn't clicked, color of list row is white.

                }
              else
                {   /* if the text is not visible, we want to show the text on click and increase the list row height */
                    txtDescription.setVisibility(View.VISIBLE);
                    txtCampus.setVisibility(View.VISIBLE);
                    txtRoom.setVisibility(View.VISIBLE);
                    txtEdit.setVisibility(View.VISIBLE);
                    txtDescription.invalidate();
                    txtCampus.invalidate();
                    txtRoom.invalidate();
                    /* if the list only contains one event, the list will expand more on click and thus decrease more if clicked again
                    This is to make it easier for the user to read the content of the list */
                    if (list.size() < 2) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listView.getWidth(), 395);
                        //  lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        lp.setMargins(0, 600, 0, 0);
                        listView.setLayoutParams(lp); //display the layout
                    }
                    /* if the "edit/remove event" link is clicked, we need to send the id of the event that the user wants to edit to EditEvent.java
                    where the user can actually edit or remove the event
                     */
                    txtEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, EditEvent.class);
                            Bundle b = new Bundle();
                            b.putString("ID", id_event);
                            intent.putExtras(b);
                            activity.startActivity(intent); // sends the user to the EditEvent.java class
                            activity.finish();

                        }
                    });


                    view.setBackgroundResource(R.color.very_light_grey); // if list row is clicked, list row becomes grey.




                }

            }
        });


        return convertView;
    }

}

