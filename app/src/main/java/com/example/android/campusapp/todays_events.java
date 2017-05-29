package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import static com.example.android.campusapp.Constants.CAMPUS_LOCATION_NAME;
import static com.example.android.campusapp.Constants.CAMPUS_NAME;
import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FAVORITES;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.ID;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;

import static com.example.android.campusapp.Constants.URL;


/**
 * Created by elsabergman on 2017-03-31.
 * This file connects to todays_events.xml and shows events coming up for student user.
 *
 * We also control the sorting spinners from here to sort what events are shown.
 * The types spinner is connected to todays_events_spinner_MyAdapterTypes.java and todays_events_StateVOTypes to create a multi choice spinner.
 * The sorting is done via different "GET" calls to the database depending on user input.
 */
public class todays_events extends student_SlidingMenuActivity {

    private todays_events_spinner_MyAdapterTypes activiateTypesSpinner;
    private ArrayList<HashMap<String, String>> list;
    MaterialBetterSpinner materialBetterSpinnerTypes;

    String[] SPINNER_DATA_TYPES = {/*"Type:",*/ "Lunch Lecture", "Promoting Event", "Evening Event","Case Event","Other"};
    String chosen_campuses;

    //url for connection with database
    String serverURL = "212.25.147.115";
    private String sendStringTypes = "";
    private String sendStringCampuses ="";

    private String universityJson,campusJson,universityIdDefault,chosen_campus;

    String University;
    private String token = null;
    private String theId = "";

    ArrayList<String> idList, nameList,nameListType,idListType,nameListUni;
    ArrayList<String> items_checkedTypesCopy = new ArrayList<String>();
    TextView textUni;
    JSONArray myCampArray,myTypeArray,myUniArray, favoritesItemsArray;

    ArrayList<todays_events_spinner_StateVOTypes> listVOsType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.todays_events, null);
        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);

        /*----------------------------------------------*/

        //Add empty string to copy of araylist of the checked types in the array to not get null
        items_checkedTypesCopy.add("");

        //Get the chosen university for the user and show it in header
        Callback myCallback = new Callback();

        String default_options = null;
        try {
            default_options = (myCallback.execution_Get("http://"+serverURL+":8000/profile/", token, "GET", "No JsonData"));
            JSONObject myInfoObject = new JSONObject(default_options);
            University = myInfoObject.getJSONObject("campus").getString("university_name");
            textUni = (TextView) findViewById(R.id.todays_events);
            textUni.setText("Find out what happens at " + University);

                 /*---Fonts for our Logo---*/
            TextView header = (TextView) findViewById(R.id.todays_events);
            Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
            header.setTypeface(custom_font);
        /*--------------------------*/

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get the users favourited events
        try { String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/my-favourites/", token, "GET", "No JsonData"));

                JSONArray myFavoritesArray = new JSONArray(status);

                JSONObject json_data = myFavoritesArray.getJSONObject(myFavoritesArray.length() - 1);

                 favoritesItemsArray = json_data.getJSONArray("favorites");
            } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Get all events on the users default university
        try {
            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/home-event/", token, "GET", "No JsonData"));

            if (status == "false") {
                Toast.makeText(todays_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            } else {

                JSONArray myEventsArray = new JSONArray(status);


                ListView listView = (ListView) findViewById(R.id.todays_events_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                total_list = the list that will be displayed
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list = new ArrayList<HashMap<String, String>>();
                todaysEvents_ListViewAdapter adapter;

                /*create as many hash maps as needed */
                for (int i = 0; i < myEventsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }

                //Put the information of the event in right places in the objects of the list of events
                for (int i = 0; i < myEventsArray.length(); i++) {
                    JSONObject json_data = myEventsArray.getJSONObject(i);
                    String date = json_data.getString("date");
                    String name = json_data.getString("name_event");
                    String start_time = json_data.getString("start_time");
                    String end_time = json_data.getString("stop_time");
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    String id_event = json_data.getString("id");
                    String location_name = json_data.getString("campus_location_name");
                    String campus_name = json_data.getString("campus_name");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,name);
                    list.get(i).put(DESCRIPTION, description);
                    list.get(i).put(URL,url);
                    if (favoritesItemsArray.toString().contains("\"name_event\":\""+name+"\"")) {
                        list.get(i).put(FAVORITES,"Saved to favorites");
                    }else {
                        list.get(i).put(FAVORITES,"Add to favorites");
                    }

                    list.get(i).put(ID,id_event);

                    if ( url != null) {

                        list.get(i).put(URL, url);

                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                        list.get(i).put(CAMPUS_NAME, campus_name);


                    }

                    else {

                        list.get(i).put(URL, url);
                        list.get(i).put(CAMPUS_NAME, campus_name);
                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                    }

                }

                adapter = new todaysEvents_ListViewAdapter(this, list, listView, token);
                listView.setAdapter(adapter);

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

  //------------------GET CAMPUS ID TO USE WHEN GET UNIVERSITY ID-----
        Callback myCallback2 = new Callback();

        try {
            String default_options2 = (myCallback2.execution_Get("http://"+serverURL+":8000/profile/", token, "GET", "No JsonData"));
                JSONObject myInfoObject = new JSONObject(default_options2);
                universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
                campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




 /*----GET USERS DEFAULT UNIVERSITY ---*/
            try {
                String status = (myCallback.execution_Get("http://" + serverURL + ":8000/university/", token, "GET", "No JsonData"));
                myUniArray = new JSONArray(status);
                nameListUni = new ArrayList<String>();

                for (int i = 0; i < myUniArray.length(); i++) {
                    JSONObject json_data = myUniArray.getJSONObject(i);
                    String name = json_data.getString("name");
                    String id = json_data.getString("id");
                    nameListUni.add(i, name);
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        //makes two lists of names and id of university for spinner. Also makes default university as default in position 0.
        boolean resultOfComparison_uni;
        final ArrayList<String> items_uni = new ArrayList<String>();
        final ArrayList<String> id_uni = new ArrayList<String>();
        items_uni.add(universityJson.toString());
        String uni_id = String.valueOf(nameListUni.indexOf(items_uni.get(0))+1);
        id_uni.add(uni_id);
        for (int k=0; k<nameListUni.size(); k++) {
            resultOfComparison_uni = nameListUni.get(k).equals(items_uni.get(0));
            if (resultOfComparison_uni == false) {
                items_uni.add(nameListUni.get(k));
                id_uni.add(String.valueOf(nameListUni.indexOf(items_uni.get(k))));
            }
        }

        universityIdDefault = id_uni.get(0);


        /*----GET CAMPUSES on users default university---*/

         /*--spinner implementation--*/
        Callback myCallbackCampuses = new Callback();
        try {
            //Get the campuses and names of these campuses
            String status = (myCallbackCampuses.execution_Get("http://"+serverURL+":8000/campus/?university="+universityIdDefault, token, "GET", "No JsonData"));
            myCampArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();

            for (int i = 0; i < myCampArray.length(); i++) {
                JSONObject json_data = myCampArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameList.add(i, name);
                idList.add(i, id);

            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> items_camp = new ArrayList<String>();
        items_camp.add("All Campuses");
        for (int i = 0; i < nameList.size(); i++) {
            items_camp.add(nameList.get(i));
        }

        //make the spinner
        final Spinner spinnerCampuses = (Spinner) findViewById(R.id.material_spinner_campuses);

        String[] campusesStringArray = new String[items_camp.size()];
        campusesStringArray = items_camp.toArray(campusesStringArray);

        //------------------------Campuses spinner start. Populates the spinner------------------------------


        final Spinner uni_spinner = (Spinner) findViewById(R.id.material_spinner_campuses);

        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, campusesStringArray);
        //ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        uni_spinner.setAdapter(campusadapter);
        uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Here we control what happens in the campuses spinner

                chosen_campus = uni_spinner.getItemAtPosition(uni_spinner.getSelectedItemPosition()).toString();


                for (int i = 0; i < myCampArray.length(); i++) {

                         /* if the chosen uni equals the uni in place i+1 (add 1 because first place is "ALL universities") */
                    if (chosen_campus.equals(items_camp.get(i+1))) {
                        theId = "";
                        //Make the id url-friendly for requests
                        theId = "campus_id="+idList.get(i)+"&";
                        //calls the function sendInfoToDatabaseType to GET again the sorted events
                        sendInfoToDatabaseType(items_checkedTypesCopy);
                    }
                    /*else*/ if(chosen_campus.equals("All Campuses")){
                        theId = "";
                        //calls the function sendInfoToDatabaseType to GET again the unsorted events
                        sendInfoToDatabaseType(items_checkedTypesCopy);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //------------------------campuses SPINNER STOP--------------------------------

        //--------STOP GET CAMPUSES--------

                /*----GET TYPES from database ---*/

         /*--types spinner implementation, get info to populate types spinner--*/
        Callback myCallbackType = new Callback();
        try {

            String statustype = (myCallbackType.execution_Get("http://"+serverURL+":8000/campus/?university="+universityIdDefault, token, "GET", "No JsonData"));

            myTypeArray = new JSONArray(statustype);
            nameListType = new ArrayList<String>();
            idListType = new ArrayList<String>();


            for (int i = 0; i < myTypeArray.length(); i++) {
                JSONObject json_data = myTypeArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameListType.add(i, name);
                idListType.add(i, id);


            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> items_type = new ArrayList<String>();
        items_type.add("Change Type?");
        for (int i = 0; i < nameListType.size(); i++) {
            items_type.add(nameListType.get(i));

        }
        final MaterialBetterSpinner materialBetterSpinnerTypes = (MaterialBetterSpinner) findViewById(R.id.material_spinner_type);
        String[] typesStringArray = new String[items_type.size()];
        typesStringArray = items_type.toArray(typesStringArray);

        //------------------------Types spinner start------------------------------
        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA_TYPES);

        materialBetterSpinnerTypes.setAdapter(typeadapter);
        //populate and make multi choice spinner with checkboxes not checked
        for (int i = 0; i < /*typesStringArray*/SPINNER_DATA_TYPES.length; i++) {
            todays_events_spinner_StateVOTypes todayseventsspinnerStateVO = new todays_events_spinner_StateVOTypes();
            todayseventsspinnerStateVO.setTitle(/*typesStringArray*/SPINNER_DATA_TYPES[i]);
            todayseventsspinnerStateVO.setSelected(false);
            listVOsType.add(todayseventsspinnerStateVO);
        }
        todays_events_spinner_MyAdapterTypes todayseventsspinnerMyAdapterType = new todays_events_spinner_MyAdapterTypes(todays_events.this, 0, listVOsType);
        materialBetterSpinnerTypes.setAdapter(todayseventsspinnerMyAdapterType);

            //------------------------Types spinner end-------------------------------

        //-------------STOP GET TYPES-------------
}

//---------------------GET FROM DATABASE WITH FILTERING, this is called from todays_events_spinner_MyAdapterTypes.java -------------
    public void sendInfoToDatabaseType(ArrayList<String> items_checkedTypes) {
        items_checkedTypesCopy =  items_checkedTypes;

        //Here we makethe checked types to a string in the right format to send to database
        sendStringTypes = "";
        boolean resultOfComparison;

        //This for loop makes the info from checked types into a url-friendly string!
        for (int k=0; k<items_checkedTypes.size(); k++) {
            resultOfComparison=items_checkedTypes.get(k).equals(items_checkedTypes.get(k));
            sendStringTypes = sendStringTypes+((items_checkedTypes.get(k))+",");
            sendStringTypes = sendStringTypes.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\ ","%20");
            if(resultOfComparison == false) {
                items_checkedTypes.add(items_checkedTypes.get(k));
            }
        }
        if (sendStringTypes != null && sendStringTypes.length() > 0 && sendStringTypes.charAt(sendStringTypes.length()-1)==',') {
            sendStringTypes = sendStringTypes.substring(0, sendStringTypes.length()-1);
        }

        Callback myCallback = new Callback();


        try {
            //makes a new get to only get the sorted events! Both sorted on campus and on types
            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/?"+theId+"type_event__in="+sendStringTypes, token, "GET", "No JsonData"));


            if (status == "false") {
                Toast.makeText(todays_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            } else {

                JSONArray myEventsArray = new JSONArray(status);
                ListView listView = (ListView) findViewById(R.id.todays_events_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                total_list = the list that will be displayed
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list = new ArrayList<HashMap<String, String>>();
                todaysEvents_ListViewAdapter adapter;

                /*create as many hash maps as needed */
                for (int i = 0; i < myEventsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }

                for (int i = 0; i < myEventsArray.length(); i++) {
                    JSONObject json_data = myEventsArray.getJSONObject(i);
                    String date = json_data.getString("date");
                    String name = json_data.getString("name_event");
                    String start_time = json_data.getString("start_time");
                    String end_time = json_data.getString("stop_time");
                    String owner = json_data.getString("owner");
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    String id_event =json_data.getString("id");

                    //Insert info on right place in list
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,name);
                    list.get(i).put(ID,id_event);
                    list.get(i).put(DESCRIPTION, description);
                    if (favoritesItemsArray.toString().contains("\"name_event\":\""+name+"\"")) {
                        list.get(i).put(FAVORITES,"Saved to favorites");
                    }else {
                        list.get(i).put(FAVORITES,"Add to favorites");
                    }

                    String location_name = json_data.getString("campus_location_name");
                    String campus_name = json_data.getString("campus_name");


                    if ( url != null) {

                        list.get(i).put(URL, url);

                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                        list.get(i).put(CAMPUS_NAME, campus_name);


                    }

                    else {

                        list.get(i).put(URL, url);
                        list.get(i).put(CAMPUS_NAME, campus_name);
                        list.get(i).put(CAMPUS_LOCATION_NAME, location_name);
                    }

                }

                adapter = new todaysEvents_ListViewAdapter(this, list, listView,token);
                listView.setAdapter(adapter);


            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //------------END RELOADING LIST OF EVENTS

    }

}
