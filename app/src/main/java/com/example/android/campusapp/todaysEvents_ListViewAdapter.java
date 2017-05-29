package com.example.android.campusapp;

/**
 * Name: todaysEvents_listViewAdapter.java
 * Authors: Elsa Bergman and Frida Kornsäter
 *Connects to: todays_events.java (displays events retrieved from database in list form), Constants.java (handles all column rows), favorites.java
 *  (if user wants to add an event to his or her favorites)
 *
 *  This class is heavily connected to todays_events.java and displays all events that will happen at the user's preferred university.
 *  This class is called once for every event and will display the event information in different columns. It handles
 *  the on click functionality of the list row and thus displays further information when a list row is clicked.
 */

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;


public class todaysEvents_ListViewAdapter extends BaseAdapter {

    /* instance variables */
    public ArrayList<HashMap<String, String>> list;
    public ArrayList<HashMap<String, Integer>> imageList;
    Activity activity;
    TextView txtFirst, txtSecond, txtThird,txtDescription, txtFavorites, txtURL,txtCampus,txtRoom, txtFaved;
    ListView listView;
    String token;
    String serverURL = "130.243.181.70";
    public todaysEvents_ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list, ListView listView, String token){
        super();
        this.activity=activity;
        this.list=list;
        this.listView = listView;
        this.token = token;
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
    /*fetches the view of the clicked list row */
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final LayoutInflater inflater=activity.getLayoutInflater();
        if(convertView == null){
            convertView=inflater.inflate(R.layout.student_column_rows, null);
            txtFavorites = (TextView) convertView.findViewById((R.id.fav));
        }
       else {
            convertView = convertView;
        }
        /* The different texts that will be displayed as columns in the system */
        txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
        txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
        txtThird=(TextView) convertView.findViewById(R.id.Time);

        final HashMap<String, String> map=list.get(position); // hash map with information sent from org_my_events.java

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
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                String myDescription = item.get("Description");
                String myUrl = item.get("Url");
               final String id_event = item.get("id");
                String myCampus = item.get("campus_name");
                String myRoom = item.get("campus_location_name");
                String faved = item.get("favorites");

                /*texts that will be displayed when the user clicks on a specific list row */
                txtDescription = (TextView) view.findViewById((R.id.description));
                txtURL = (TextView) view.findViewById((R.id.url));
                txtFavorites = (TextView) view.findViewById(R.id.fav);
                txtFaved = (TextView) view.findViewById(R.id.faved);
                txtCampus = (TextView) view.findViewById((R.id.campus_name));
                txtRoom = (TextView) view.findViewById((R.id.location_place_room));
                txtDescription.setTextColor(Color.DKGRAY);
                txtDescription.setText("Description: " + myDescription);
                txtURL.setText(myUrl);

                /* underlines the clickable link to make it more obvious that it is clickable */
                if (faved.equals("Add to favorites")) {
                    SpannableString content = new SpannableString(item.get("favorites"));
                    content.setSpan(new UnderlineSpan(), 0, item.get("favorites").length(), 0);
                    txtFavorites.setText(content);
                }else {
                    txtFaved.setText(faved);
                }

                /* set text and color of text that will be visible when lis row is clicked */
                txtCampus.setTextColor(Color.DKGRAY);
                txtCampus.setText("Campus: " +myCampus);

                txtRoom.setTextColor(Color.DKGRAY);
                txtRoom.setText("Location: " +myRoom);


                 /* if the text is already visible, we want to hide the text on click and decrease the list row height */
                if ( (txtDescription.getVisibility() == View.VISIBLE)  )
                {

                    txtDescription.setVisibility(View.GONE);
                    txtURL.setVisibility(View.GONE);
                    txtFavorites.setVisibility(View.GONE);
                    txtFaved.setVisibility(View.GONE);
                    txtCampus.setVisibility(View.GONE);
                    txtRoom.setVisibility(View.GONE);
                    txtDescription.invalidate();

                    /* if the list only contains one event, the list will expand more on click and thus decrease more if clicked again
                    This is to make it easier for the user to read the content of the list */
                    if (list.size() < 2) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listView.getWidth(), 175);
                        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        lp.setMargins(0, 600, 0, 0);

                        listView.setLayoutParams(lp);
                    }


                    view.setBackgroundColor(Color.WHITE); // if list row doesn't show all information and thus isn't clicked, color of list row is white.

                }
                else
                { /* if the text is not visible, we want to show the text on click and increase the list row height */
                    txtDescription.setVisibility(View.VISIBLE);
                    txtURL.setVisibility(View.VISIBLE);
                    txtFavorites.setVisibility(View.VISIBLE);
                    txtFaved.setVisibility(View.VISIBLE);
                    txtCampus.setVisibility(View.VISIBLE);
                    txtRoom.setVisibility(View.VISIBLE);
                    txtDescription.invalidate();
                    view.setBackgroundResource(R.color.very_light_grey); // if list row is clicked, list row becomes grey.

                     /* if the list only contains one event, the list will expand more on click and thus decrease more if clicked again
                    This is to make it easier for the user to read the content of the list */
                    if (list.size() < 2) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(listView.getWidth(), 320);
                        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        lp.setMargins(0, 600, 0, 0);
                        listView.setLayoutParams(lp);
                    }

                    /* if the "add to favorites" link is clicked, we want to make a request to the database to add the clicked event
                    to the user's favorites */
                        txtFavorites.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                JSONObject post_dict = new JSONObject(); //creates Json object

                                Callback myCallback = new Callback();

                                try {
                                    /* add event id to json object, this is needed in order to let the database know what event the
                                    user wants to favorite
                                     */
                                    post_dict.put("event_id", id_event);

                                    /* make call to database */
                                    String status = (myCallback.execution_Post("http://" + serverURL + ":8000/events/my-favourites/add/", token, "POST", post_dict.toString()));

                                    if (status == "true") {
                                        Toast.makeText(v.getContext(), "Event was added to your favorites", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(v.getContext(), "Event could not be added to favorites", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                }
        });
        return convertView;
    }
}


