package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by Anna on 2017-04-04.
 */


public class add_event extends SlidingMenuActivity {

    String chosen_campus;
    private ArrayList<HashMap<String, String>> uniList;
    String chosen_uni;
    JSONArray myUniArray;
    String theId;
    String theIdCampus;
    String theIdRoom;
    String chosen_room;
    ArrayList<String> idList;
    ArrayList<String> nameList;
    JSONArray myCampusArray;
    ArrayList<String> nameCampusList;
    ArrayList<String> idCampusList;
    ArrayList<String> nameRoomList;
    ArrayList<String> idRoomList;
    JSONArray myRoomArray;
     EditText event_name;
    EditText company_name;
    EditText relevant_links;
    EditText description;
    EditText date;
    EditText starttime;
    EditText stoptime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.add_event, null);

        drawer.addView(contentView, 0);


         /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/



 /*----GET UNIVERSITY ---*/

         /*--spinner implementation--*/
        Callback myCallback = new Callback();
        try {
            String status = (myCallback.execution_Get("http://130.242.107.7:8000/university/", token, "GET", "No JsonData"));
            myUniArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();
            System.out.println(myUniArray);


           /* uniList=new ArrayList<HashMap<String,String>>();
            for(int i = 0; i < myUniArray.length(); i++) {
                uniList.add(new HashMap<String, String>());
            }*/


            for (int i = 0; i < myUniArray.length(); i++) {
                JSONObject json_data = myUniArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameList.add(i, name);
                idList.add(i, id);


            }

            System.out.println(nameList);
            System.out.println(idList);
            System.out.println(nameList.get(0));


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> items_uni = new ArrayList<String>();
        items_uni.add("Choose University...");
        for (int i=0; i<nameList.size(); i++) {
            items_uni.add(nameList.get(i));
        }
        final Spinner uni_spinner = (Spinner) findViewById(R.id.choose_university);

        ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        uniadapter.setDropDownViewResource(R.layout.spinner_layout);
        uni_spinner.setAdapter(uniadapter);
        uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_uni = uni_spinner.getItemAtPosition(uni_spinner.getSelectedItemPosition()).toString();

                if (chosen_uni != "Choose university...") {


                    for (int i = 0; i < myUniArray.length(); i++) {

                        if (uni_spinner.getSelectedItemPosition() == i) ;
                        {
                            theId = idList.get(i);
                            System.out.println("my ID" + theId);
                            ChooseMyCampus(theId, token);


                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*----GET CAMPUS ----*/

    void ChooseMyCampus(String theId, final String token) {

                    Callback myCallback = new Callback();
                    try {
                        String all_campuses = (myCallback.execution_Get("http://130.242.107.7:8000/campus/?university="+theId, token, "GET", "No JsonData"));
                        myCampusArray = new JSONArray(all_campuses);
                        nameCampusList = new ArrayList<String>();
                        idCampusList = new ArrayList<String>();
                        System.out.println("all campuses " + all_campuses);


                        for (int i = 0; i < myCampusArray.length() ; i++) {
                            JSONObject json_data = myCampusArray.getJSONObject(i);
                            String nameCampus = json_data.getString("name");
                            String idCampus = json_data.getString("id");
                            nameCampusList.add(i,nameCampus);
                            idCampusList.add(i,idCampus);


                        }
                        System.out.println(nameCampusList + "name");
                        System.out.println(nameCampusList.get(0));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


        final Spinner spinner = (Spinner)findViewById(R.id.choose_campus);
      //  String[] items_campus = new String[]{"Choose Campus"};
        ArrayList<String> items_campus = new ArrayList<String>();
        items_campus.add("Choose Campus...");
        for (int i=0; i<nameCampusList.size(); i++) {
          items_campus.add(nameCampusList.get(i));
        }

        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                if (chosen_campus != "Choose Campus...") {

                    for (int i = 0; i < myCampusArray.length(); i++) {

                        if (spinner.getSelectedItemPosition()  == i+1) //Den kallar på ChooseRoom två gånger! fixa detta!
                        {
                            theIdCampus = idCampusList.get(i);
                            System.out.println(theIdCampus);
                            System.out.println(spinner.getSelectedItemPosition() + "the position");
                            ChooseRoom(theIdCampus, token);

                        }
                    }
                }


                    /**  @Override public void onAttach(Activity context) {
                    super.onAttach(context);

                    }

                    /**  public interface OnFragmentInteractionListener {
                    // TODO: Update argument type and name
                    void onFragmentInteraction(Uri uri);
                    }

                     */

                }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });

    }

    /*-----GET ROOM -----*/

    void ChooseRoom(String campusId, final String token) {

        Callback myCallback = new Callback();
        try {
            String all_rooms = (myCallback.execution_Get("http://130.242.107.7:8000/campus-location/?campus="+campusId, token, "GET", "No JsonData"));
            myRoomArray = new JSONArray(all_rooms);
            nameRoomList = new ArrayList<String>();
            idRoomList = new ArrayList<String>();


            for (int i = 0; i < myRoomArray.length() ; i++) {
                JSONObject json_data = myRoomArray.getJSONObject(i);
                String nameRoom = json_data.getString("name");
                String idRoom = json_data.getString("id");
                nameRoomList.add(i,nameRoom);
                idRoomList.add(i,idRoom);


            }
            System.out.println(nameRoomList + "id");
            System.out.println(nameRoomList + "name");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> items_room = new ArrayList<String>();
        items_room.add("Choose Room...");
        for (int i=0; i<nameRoomList.size(); i++) {
            items_room.add(nameRoomList.get(i));
        }
        final Spinner spinner_room = (Spinner)findViewById(R.id.choose_room);
        ArrayAdapter<String> roomadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_room);
        roomadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_room.setAdapter(roomadapter);

        spinner_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_room = spinner_room.getItemAtPosition(spinner_room.getSelectedItemPosition()).toString();
                if (chosen_room != "Choose Room...") {

                    for (int i = 0; i < myRoomArray.length(); i++) {

                        if (spinner_room.getSelectedItemPosition() == i) ;
                        {
                            theIdRoom = idRoomList.get(i);

                            CreateMyEvent(theIdRoom, token);

                        }
                    }


                    /**  @Override public void onAttach(Activity context) {
                    super.onAttach(context);

                    }

                    /**  public interface OnFragmentInteractionListener {
                    // TODO: Update argument type and name
                    void onFragmentInteraction(Uri uri);
                    }

                     */

                }
            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });

    }


    /*----CREATE EVENT ----*/

    void CreateMyEvent(final String roomId, final String token) {

        Button btn = (Button) findViewById(R.id.create_event_button);

        final EditText event_name = (EditText) findViewById(R.id.input_add_event);
        final EditText company_name = (EditText) findViewById(R.id.input_company_name);
        final EditText relevant_links = (EditText) findViewById(R.id.input_links);
        final EditText description = (EditText) findViewById(R.id.eventDescription);
        final EditText date = (EditText) findViewById(R.id.date);
        final EditText starttime = (EditText) findViewById(R.id.start_time);
        final EditText stoptime = (EditText) findViewById(R.id.end_time);

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                String eventname = event_name.getText().toString(); //saves email input from user

                String companyname = company_name.getText().toString(); //saves password input from user

                String relevantlinks = relevant_links.getText().toString(); //saves password input from user

                String eventdescription = description.getText().toString(); //saves password input from user

                String dateEvent = date.getText().toString(); //saves password input from user
                String start_time = starttime.getText().toString(); //saves password input from user
                String stop_time = stoptime.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object


                try {
                    post_dict.put("type_event", "Lunch Lecture");
                    post_dict.put("name_event", eventname);
                    post_dict.put("description", eventdescription);
                    post_dict.put("date", dateEvent);
                    post_dict.put("start_time", start_time);
                    post_dict.put("stop_time", stop_time);
                    post_dict.put("external_url", relevantlinks);
                    post_dict.put("campus_location", roomId);

                    System.out.println(post_dict);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {


                    Callback myCallback = new Callback();

                    try {
                        String status = (myCallback.execution_Post("http://130.242.107.7:8000/events/", token,"POST",post_dict.toString()));
                        if (status == "true") {
                            Toast.makeText(add_event.this, "Event created successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(add_event.this, org_my_events.class);
                            startActivity(intent);
                        }if(status == "false"){
                            Toast.makeText(add_event.this, "event could not be created", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }



}








