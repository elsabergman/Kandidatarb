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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.Constants.FIRST_COLUMN;


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
    ArrayList<String> idList;
    ArrayList<String> nameList;
    JSONArray myCampusArray;
    ArrayList<String> nameCampusList;
    ArrayList<String> idCampusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.add_event, null);

        drawer.addView(contentView, 0);


         /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/


        Button btn = (Button) findViewById(R.id.create_event_button);
        final EditText event_name = (EditText) findViewById(R.id.input_add_event);
        final EditText company_name = (EditText) findViewById(R.id.input_company_name);
        final EditText relevant_links = (EditText) findViewById(R.id.input_links);
        final EditText description = (EditText) findViewById(R.id.eventDescription);
        final EditText date = (EditText) findViewById(R.id.date);
        final EditText starttime = (EditText) findViewById(R.id.start_time);
        final EditText stoptime = (EditText) findViewById(R.id.end_time);


         /*--spinner implementation--*/
        Callback myCallback = new Callback();
        try {
            String status = (myCallback.execution_Get("http://130.238.250.84:8000/university/", token, "GET", "No JsonData"));
            myUniArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();
            System.out.println(myUniArray);


           /* uniList=new ArrayList<HashMap<String,String>>();
            for(int i = 0; i < myUniArray.length(); i++) {
                uniList.add(new HashMap<String, String>());
            }*/


            for (int i = 0; i < myUniArray.length() ; i++) {
                JSONObject json_data = myUniArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameList.add(i,name);
                System.out.println(nameList);
                idList.add(i,id);


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


        final Spinner uni_spinner = (Spinner)findViewById(R.id.choose_university);
        String[] items_uni = new String[]{"Choose university", nameList.get(0)};
        ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        uniadapter.setDropDownViewResource(R.layout.spinner_layout);
        uni_spinner.setAdapter(uniadapter);
        uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                //Här inne är vad som sker när en grej i listan väljs

                chosen_uni = uni_spinner.getItemAtPosition(uni_spinner.getSelectedItemPosition()).toString();
                System.out.println(chosen_uni);
                if(chosen_uni != "Choose university") {



               for(int i = 0; i<myUniArray.length(); i++) {

                   if (uni_spinner.getSelectedItemPosition() ==  i);
                   {
                       theId = idList.get(i);
                       System.out.println(theId);
                   }
               }

                Callback myCallback = new Callback();
               try {
                   String all_campuses = (myCallback.execution_Get("http://130.238.250.84:8000/campus/?university="+theId, token, "GET", "No JsonData"));
                   myCampusArray = new JSONArray(all_campuses);
                   nameCampusList = new ArrayList<String>();
                   idCampusList = new ArrayList<String>();


                   for (int i = 0; i < myUniArray.length() ; i++) {
                       JSONObject json_data = myUniArray.getJSONObject(i);
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


        final Spinner spinner = (Spinner)findViewById(R.id.choose_campus);
        String[] items_campus = new String[]{"Choose Campus", nameCampusList.get(0)};
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                if (chosen_uni != "Choose Campus") {

                    for (int i = 0; i < myUniArray.length(); i++) {

                        if (uni_spinner.getSelectedItemPosition() == i) ;
                        {
                            theIdCampus = idCampusList.get(i);

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
                    post_dict.put("campus_location", chosen_campus);

                    System.out.println(post_dict);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {


                    Callback myCallback = new Callback();

                    try {
                        String status = (myCallback.execution_Post("http://130.238.250.84:8000/events/", token,"POST",post_dict.toString()));
                        System.out.println(status);
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





