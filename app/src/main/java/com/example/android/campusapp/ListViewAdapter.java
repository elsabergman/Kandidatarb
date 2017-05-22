package com.example.android.campusapp;

/**
 * Created by elsabergman on 2017-05-02.
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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        final LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.column_rows, null);


           // listView = (ListView) convertView.findViewById(R.id.your_event_list)
        } else {
            convertView = convertView;
        }

        txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
        txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
        txtThird=(TextView) convertView.findViewById(R.id.Time);
        txtFourth=(TextView) convertView.findViewById(R.id.owner);
        txtDescription = (TextView) convertView.findViewById((R.id.description));
        txtURL = (TextView) convertView.findViewById((R.id.url));
        txtCampus = (TextView) convertView.findViewById((R.id.campus_name));
        txtRoom = (TextView) convertView.findViewById((R.id.location_place_room));
        txtEdit = (TextView) convertView.findViewById((R.id.fav));
        final HashMap<String, String> map=list.get(position);

        txtFirst.setTextColor(Color.DKGRAY);
        txtSecond.setTextColor(Color.DKGRAY);
        txtThird.setTextColor(Color.DKGRAY);

        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

                String myDescription = item.get("Description");
                String myName = item.get("Third");
                String myUrl = item.get("Url");
                final String id_event = item.get("id");
                String myCampus = item.get("campus_name");
                String myRoom = item.get("campus_location_name");
                System.out.println(myName + " name of event");
              /*  System.out.println(myRoom);
                System.out.println("My location" + myCampus);
                System.out.println("My room" + myRoom);*/


                txtDescription = (TextView) view.findViewById((R.id.description));
                txtURL = (TextView) view.findViewById((R.id.url));
                txtCampus = (TextView) view.findViewById((R.id.campus_name));
                txtRoom = (TextView) view.findViewById((R.id.location_place_room));
                txtEdit = (TextView) view.findViewById(R.id.edit);

                SpannableString content = new SpannableString(item.get("edit"));
                content.setSpan(new UnderlineSpan(), 0, item.get("edit").length(), 0);
                txtEdit.setText(content);
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
                    txtEdit.setVisibility(View.GONE);
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
                    txtEdit.setVisibility(View.VISIBLE);
                   txtDescription.invalidate();
                   // txtURL.invalidate();
                    txtCampus.invalidate();
                    txtRoom.invalidate();
                    txtEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, EditEvent.class);
                            Bundle b = new Bundle();
                            b.putString("ID", id_event);
                            intent.putExtras(b);
                            activity.startActivity(intent);
                            activity.finish();
                          // activity.startActivity((new Intent(activity, EditEvent.class)));

                        }
                    });


                    view.setBackgroundResource(R.color.very_light_grey);




                }

            }
        });


        return convertView;
    }

}

