package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
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

import static com.android.volley.Request.Method.HEAD;


/**
 * Created by Anna on 2017-04-04.
 */


public class add_event extends SlidingMenuActivity {

    String chosen_campus;
    private ArrayList<HashMap<String, String>> uniList;
    String chosen_uni;
    String universityJson;
    String campusJson;
    JSONArray myUniArray;
    String theId;
    String theIdCampus;
    String theIdRoom;
    String chosen_room;
    String chosen_location;
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
    String url = "130.243.199.160";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.add_event, null);

        drawer.addView(contentView, 0);


         /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/

        Callback myCallback = new Callback();
        try {
            String default_options = (myCallback.execution_Get("http://"+url+":8000/profile/", token, "GET", "No JsonData"));


            JSONObject myInfoObject = new JSONObject(default_options);
            universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
            campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

 /*----GET UNIVERSITY ---*/

         /*--spinner implementation--*/

            try {

                String status = (myCallback.execution_Get("http://" + url + ":8000/university/", token, "GET", "No JsonData"));

                myUniArray = new JSONArray(status);
                nameList = new ArrayList<String>();



                for (int i = 0; i < myUniArray.length(); i++) {
                    JSONObject json_data = myUniArray.getJSONObject(i);
                    String name = json_data.getString("name");
                    String id = json_data.getString("id");
                    nameList.add(i, name);

                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*add campuses to spinner list, with default campus as the first element */
            boolean resultOfComparison_uni;
            final ArrayList<String> items_uni = new ArrayList<String>();
            final ArrayList<String> id_uni = new ArrayList<String>();
            items_uni.add(universityJson.toString());
            String uni_id = String.valueOf(nameList.indexOf(items_uni.get(0))+1);
            id_uni.add(uni_id);
            for (int k=0; k<nameList.size(); k++) {
                resultOfComparison_uni = nameList.get(k).equals(items_uni.get(0));
                System.out.println(resultOfComparison_uni);
                if (resultOfComparison_uni == false) {
                    items_uni.add(nameList.get(k));
                    id_uni.add(String.valueOf(nameList.indexOf(items_uni.get(k))));
                }
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

                        for (int i = 0; i < myUniArray.length(); i++) {
                          /* if the chosen uni equals the uni in place i */
                            if (chosen_uni == items_uni.get(i)) {
                                theId = id_uni.get(i);
                                System.out.println(theId);
                                System.out.println(chosen_uni);

                                ChooseMyCampus(theId, token); //Call choose campus with the chosen university


                            }
                        }
                    }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*----GET CAMPUS ----*/

    void ChooseMyCampus(String theId, final String token) {

                    Callback myCallback = new Callback();
                    try {

                        String all_campuses = (myCallback.execution_Get("http://"+url+":8000/campus/?university="+theId, token, "GET", "No JsonData"));


                        myCampusArray = new JSONArray(all_campuses);
                        nameCampusList = new ArrayList<String>();



                        nameCampusList.add(campusJson);


                        for (int i = 0; i < myCampusArray.length() ; i++) {
                            JSONObject json_data = myCampusArray.getJSONObject(i);

                            String nameCampus = json_data.getString("name");
                            String idCampus = json_data.getString("id");
                            if (nameCampus != nameCampusList.get(0)) {
                                nameCampusList.add(i, nameCampus);

                            }


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


        final Spinner spinner = (Spinner)findViewById(R.id.choose_campus);
      //  String[] items_campus = new String[]{"Choose Campus"};

        boolean resultOfComparison_campus;
        final ArrayList<String> items_campus = new ArrayList<String>();
        final ArrayList<String> id_campus = new ArrayList<String>();
        items_campus.add(campusJson.toString());
        String campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(0))+1);
        id_campus.add(campus_id);
        for (int k=0; k<nameCampusList.size(); k++) {
            resultOfComparison_campus = nameCampusList.get(k).equals(items_campus.get(0));

            if (resultOfComparison_campus == false) {
                items_campus.add(nameCampusList.get(k));
                campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(k))+1);
                System.out.println("campus_id " + campus_id);
                id_campus.add(campus_id);
            }
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

                    for (int i = 0; i < myCampusArray.length(); i++) {

                        /* if the chosen campus equals the campus in place i+1 (add 1 because first place is "Choose Campus...") */
                            if (chosen_campus == items_campus.get(i))
                            {
                                theIdCampus = id_campus.get(i);

                                ChooseRoom(theIdCampus, token);

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

            String all_rooms = (myCallback.execution_Get("http://" + url + ":8000/campus-location/?campus=" + campusId, token, "GET", "No JsonData"));

            myRoomArray = new JSONArray(all_rooms);
            nameRoomList = new ArrayList<String>();
            idRoomList = new ArrayList<String>();


            for (int i = 0; i < myRoomArray.length(); i++) {
                JSONObject json_data = myRoomArray.getJSONObject(i);
                String nameRoom = json_data.getString("name");
                String idRoom = json_data.getString("id");
                nameRoomList.add(i, nameRoom);
                idRoomList.add(i, idRoom);


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<String> items_room = new ArrayList<String>();
        boolean resultOfComparison_room;

        items_room.add("Choose Room...");
        for (int k = 0; k < nameRoomList.size(); k++) {
            resultOfComparison_room = nameRoomList.get(k).equals(items_room.get(0));
            if (resultOfComparison_room == false) {
                items_room.add(nameRoomList.get(k));
            }
        }
        final Spinner spinner_room = (Spinner) findViewById(R.id.choose_room);
        ArrayAdapter<String> roomadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_room);
        roomadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_room.setAdapter(roomadapter);

        spinner_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_room = spinner_room.getItemAtPosition(spinner_room.getSelectedItemPosition()).toString();
                if (chosen_room != "Choose Room...") {


                    for (int i = 0; i < myRoomArray.length(); i++) {

                        if (chosen_room == items_room.get(i)) {
                            theIdRoom = idRoomList.get(i);
                            CreateMyEvent(theIdRoom, token);
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


    /*----CREATE EVENT ----*/

    void CreateMyEvent(final String roomId, final String token) {
        ArrayList<String> type = new ArrayList<String>();
        type.add("Choose Type...");
        type.add("Lunch Event");
        type.add("Evening Event");
        type.add("Case Event");
        type.add("Promoting Event");

        final Spinner spinner_type = (Spinner)findViewById(R.id.choose_type);
        ArrayAdapter<String> locationadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, type);
       locationadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_type.setAdapter(locationadapter);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_location = spinner_type.getItemAtPosition(spinner_type.getSelectedItemPosition()).toString();
                if (chosen_location != "Choose Type...") {

                    Log.d("chosen location", chosen_location);
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

                        String status = (myCallback.execution_Post("http://"+url+":8000/events/", token,"POST",post_dict.toString()));

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








