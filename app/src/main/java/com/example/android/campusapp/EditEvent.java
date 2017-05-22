package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.io.SessionOutputBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.R.id.campusesSpinnerSettings;

/**
 * Created by elsabergman on 2017-05-17.
 */

public class EditEvent extends SlidingMenuActivity {
    String serverURL = "130.243.182.165";
    String id_event, name, url, theId,chosen_type,theIdCampus,theIdRoom, chosen_room,chosen_campus,chosen_uni, location;
    EditText event_name, date_event, company_visiting, start_time, end_time, relevant_links, description;
    JSONArray myUniArray,myRoomArray,myCampusArray;
    ArrayList<String> idList,nameList,nameCampusList,idCampusList,nameRoomList,idRoomList;
    String universityJson = "Change University?";
    String campusJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.edit_event, null);

        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println(token);

        /*----------------------------------------------*/

          /*---Fonts for our Logo---*/
        TextView header = (TextView) findViewById(R.id.edit_text);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);
        /*--------------------------*/


        ArrayList<String> type = new ArrayList<String>();
        type.add("Choose Type...");
        type.add("Lunch Lecture");
        type.add("Evening Lecture");
        type.add("Case Event");
        type.add("Promoting Event");
        type.add("Other");

        final Spinner spinner_type = (Spinner)findViewById(R.id.choose_type);
        ArrayAdapter<String> locationadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, type);
        locationadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_type.setAdapter(locationadapter);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_type = spinner_type.getItemAtPosition(spinner_type.getSelectedItemPosition()).toString();


                Log.d("chosen location", chosen_type);
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
        Bundle b = getIntent().getExtras();

        if (b != null)
            id_event = b.getString("ID");
        Button btn = (Button) findViewById(R.id.remove_event_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback myCallback = new Callback();

                try {

                    String status = (myCallback.execution_Get("http://" + serverURL + ":8000/events/my-events/delete/" + id_event + "/", token, "DELETE", "No JsonData"));

                    if (status == "false") {
                        Toast.makeText(EditEvent.this, "could not fetch delete event", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditEvent.this, "Event successfully removed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditEvent.this, org_my_events.class);
                        startActivity(intent);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


        event_name = (EditText) findViewById(R.id.edit_name_event);
        company_visiting = (EditText) findViewById(R.id.edit_company_name);
        date_event = (EditText) findViewById(R.id.edit_date);
        start_time = (EditText) findViewById(R.id.edit_start_time);
        end_time = (EditText) findViewById(R.id.edit_end_time);
        relevant_links = (EditText) findViewById(R.id.edit_links);
        description = (EditText) findViewById(R.id.edit_eventDescription);
        Callback myCallback = new Callback();
        try {

            String profile = (myCallback.execution_Get("http://"+serverURL+":8000/profile/", token, "GET", "No JsonData"));
            JSONObject myInfoObject = new JSONObject(profile);


            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/"+id_event+"/", token, "GET", "No JsonData"));

            JSONObject json_data = new JSONObject(status);

                String date = json_data.getString("date");
                String name = json_data.getString("name_event");
                String start_time_string = json_data.getString("start_time");
                String end_time_string = json_data.getString("stop_time");
                String description_string = json_data.getString("description");
                String url = json_data.getString("external_url");
                String company = json_data.getString("visiting_organisation");//kolla om detta stämmer
                location = json_data.getString("campus_name");

            event_name.setText(name, TextView.BufferType.EDITABLE);
            company_visiting.setText(company, TextView.BufferType.EDITABLE);
            date_event.setText(date, TextView.BufferType.EDITABLE);
            start_time.setText(start_time_string, TextView.BufferType.EDITABLE);
            end_time.setText(end_time_string, TextView.BufferType.EDITABLE);
            relevant_links.setText(url, TextView.BufferType.EDITABLE);
            description.setText(description_string, TextView.BufferType.EDITABLE);

            } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        /*----GET UNIVERSITY ---*/

         /*--spinner implementation--*/
        Callback myCallbackUni = new Callback();
        try {

            String status = (myCallbackUni.execution_Get("http://"+serverURL+":8000/university/", token, "GET", "No JsonData"));


            myUniArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();
            System.out.println(myUniArray);




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

        final ArrayList<String> items_uni = new ArrayList<String>();

        //items_uni.add("Change University?");
       /* for (int i=0; i<nameList.size(); i++) {
            items_uni.add(nameList.get(i));
        }*/

        /*------------------add campuses to spinner list, with chosen campus as the first element */

        boolean resultOfComparisonUni;
        items_uni.add(universityJson.toString());
        for (int k=0; k<nameList.size(); k++) {
            resultOfComparisonUni=nameList.get(k).equals(items_uni.get(0));
            System.out.println("resultOfComparisonUni is "+resultOfComparisonUni);
            if(resultOfComparisonUni == false) {
                items_uni.add(nameList.get(k));
            }

        }

        //-------------------------------------------------------------------------------



        final Spinner uni_spinner = (Spinner) findViewById(R.id.edit_university);

        System.out.println("items_uni is "+items_uni);

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
                          /* if the chosen uni equals the uni in place i+1 (add 1 because first place is "Choose Uni...") */
                    if (chosen_uni == items_uni.get(i/*+1*/)) {
                        theId = idList.get(i);

                        ChooseMyCampus(theId, token); //Call choose campus with the chosen university


                    }

                    {



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



            String all_campuses = (myCallback.execution_Get("http://"+serverURL+":8000/campus/?university="+theId, token, "GET", "No JsonData"));



            myCampusArray = new JSONArray(all_campuses);
            nameCampusList = new ArrayList<String>();
            idCampusList = new ArrayList<String>();



            for (int i = 0; i < myCampusArray.length() ; i++) {
                JSONObject json_data = myCampusArray.getJSONObject(i);
                String nameCampus = json_data.getString("name");
                String idCampus = json_data.getString("id");
                nameCampusList.add(i,nameCampus);
                idCampusList.add(i,idCampus);


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        final Spinner spinner = (Spinner)findViewById(R.id.edit_campus);
        //  String[] items_campus = new String[]{"Choose Campus"};
        System.out.println(nameCampusList);
        boolean resultOfComparison_campus;
        final ArrayList<String> items_campus = new ArrayList<String>();
        final ArrayList<String> id_campus = new ArrayList<String>();
        items_campus.add(location);
        System.out.println(items_campus + " default");
        String campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(0))+1); //+1 since arraylists start at 0
        id_campus.add(campus_id);
        System.out.println("first campus id" + id_campus);
        for (int k=0; k<nameCampusList.size(); k++) {
            resultOfComparison_campus = nameCampusList.get(k).equals(items_campus.get(0));

            if (resultOfComparison_campus == false) {
                items_campus.add(nameCampusList.get(k));
                campus_id = String.valueOf(k+1);
                id_campus.add(campus_id);
            }
        }
        System.out.println(id_campus + " all campus id");
        System.out.println(items_campus + "all items_campus");
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                System.out.println(chosen_campus);

                for (int i = 0; i < myCampusArray.length(); i++) {

                        /* if the chosen campus equals the campus in place i+1 (add 1 because first place is "Choose Campus...") */
                    if (chosen_campus == items_campus.get(i))
                    {
                        System.out.println(id_campus.get(i) +  " id_campus");
                        theIdCampus = id_campus.get(i);
                        System.out.println(theIdCampus);
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

            String all_rooms = (myCallback.execution_Get("http://" + serverURL+ ":8000/campus-location/?campus=" +campusId, token, "GET", "No JsonData"));


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
        final Spinner spinner_room = (Spinner) findViewById(R.id.edit_room);
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


                    for (int i = 0; i < items_room.size(); i++) {

                        if (chosen_room == items_room.get(i)) {
                            theIdRoom = idRoomList.get(i-1);
                            System.out.println(theIdRoom + " room id");
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
    void CreateMyEvent(final String roomId, final String token) {



        Button btn = (Button) findViewById(R.id.edit_event_button);

        final EditText event_name = (EditText) findViewById(R.id.edit_name_event);
        final EditText company_name = (EditText) findViewById(R.id.edit_company_name);
        final EditText relevant_links = (EditText) findViewById(R.id.edit_links);
        final EditText description = (EditText) findViewById(R.id.edit_eventDescription);
        final EditText date = (EditText) findViewById(R.id.edit_date);
        final EditText starttime = (EditText) findViewById(R.id.edit_start_time);
        final EditText stoptime = (EditText) findViewById(R.id.edit_end_time);

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

                System.out.println(relevantlinks);
                try {
                    post_dict.put("type_event", chosen_type);
                    post_dict.put("name_event", eventname);
                    post_dict.put("description", eventdescription);
                    post_dict.put("date", dateEvent);
                    post_dict.put("start_time", start_time);
                    post_dict.put("stop_time", stop_time);
                    if (relevantlinks.equals("null")){
                        post_dict.put("external_url", " ");
                    } else{
                    post_dict.put("external_url", relevantlinks);}

                    post_dict.put("campus_location", roomId);

                    System.out.println(post_dict);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {


                    Callback myCallback = new Callback();

                    try {


                        String status = (myCallback.execution_Post("http://"+serverURL+":8000/events/"+id_event+"/", token,"PATCH",post_dict.toString()));

                        if (status == "true") {
                            Toast.makeText(EditEvent.this, "Event created successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EditEvent.this, org_my_events.class);
                            startActivity(intent);
                        }if(status == "false"){
                            Toast.makeText(EditEvent.this, "event could not be created", Toast.LENGTH_LONG).show();
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



