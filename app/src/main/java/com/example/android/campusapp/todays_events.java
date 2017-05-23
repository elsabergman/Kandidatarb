package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.HEAD;
import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FAVORITES;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.ID;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;

import static com.example.android.campusapp.Constants.URL;



/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends student_SlidingMenuActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> total_list;

    //private String token;


    MaterialBetterSpinner materialBetterSpinnerTypes;

    String[] SPINNER_DATA_CAMPUSES = {"Campus:", "Ångström", "Engelska Parken", "ITC", "Ekonomikum"};
    String[] SPINNER_DATA_TYPES = {"Type:", "Lunch Event", "Promoting Event", "Evening Event","Case Event","Other"};
    String chosen_campuses;
    String theId;
    //String token;
    String serverURL = "130.243.182.165";
    private String sendStringTypes = "";
    private String sendStringCampuses ="";
    String University;

    private String token = null;

    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> nameListType;
    ArrayList<String> idListType;
    TextView textUni;


    JSONArray myCampArray;
    JSONArray myTypeArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.todays_events, null);
        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println(token);

        /*----------------------------------------------*/






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





        try {

            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/", token, "GET", "No JsonData"));


            System.out.println(status);
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
                total_list = new ArrayList<HashMap<String, String>>();
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
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    String id_event = json_data.getString("id");
                    System.out.println("event name " + name);
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,name);
                    list.get(i).put(DESCRIPTION, description);
                    list.get(i).put(URL,url);
                    list.get(i).put(FAVORITES,"Add to favorites");
                    list.get(i).put(ID,id_event);

                    if ( url != null) {

                        list.get(i).put(URL, url);
                    }

                    else {

                        list.get(i).put(URL, " ");
                    }

                    Log.d(name, "name");
                    Log.d(date, "date");
                    Log.d(start_time, "start");
                    Log.d(end_time, "end");
                    Log.d(description, "description");
                    // Log.d(id, "id");


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


        /*----GET CAMPUSES ---*/

         /*--spinner implementation--*/
        Callback myCallbackUni = new Callback();
        try {

            String status = (myCallbackUni.execution_Get("http://"+serverURL+":8000/campus/?university=1", token, "GET", "No JsonData"));

            myCampArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();
            System.out.println(myCampArray);


            for (int i = 0; i < myCampArray.length(); i++) {
                JSONObject json_data = myCampArray.getJSONObject(i);
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

        final ArrayList<String> items_camp = new ArrayList<String>();
        items_camp.add("Change Campus?");
        for (int i = 0; i < nameList.size(); i++) {
            items_camp.add(nameList.get(i));
        }
        //final Spinner camp_spinner = (Spinner) findViewById(R.id.material_spinner_campuses);
        final MaterialBetterSpinner materialBetterSpinnerCampuses = (MaterialBetterSpinner) findViewById(R.id.material_spinner_campuses);
        //String SPINNER_DATA_TESTCAMPUS = items_camp.toArray();

        String[] campusesStringArray = new String[items_camp.size()];
        campusesStringArray = items_camp.toArray(campusesStringArray);

        System.out.println("items_camp is " + items_camp);

        //------------------------Campusese SPINNER START!!!------------------------------
        ArrayAdapter<String> campadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, campusesStringArray);

        materialBetterSpinnerCampuses.setAdapter(campadapter);

        ArrayList<todays_events_spinner_StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < campusesStringArray.length; i++) {
            todays_events_spinner_StateVO todayseventsspinnerStateVO = new todays_events_spinner_StateVO();
            todayseventsspinnerStateVO.setTitle(campusesStringArray[i]);
            todayseventsspinnerStateVO.setSelected(false);
            listVOs.add(todayseventsspinnerStateVO);
        }
        todays_events_spinner_MyAdapter todayseventsspinnerMyAdapter = new todays_events_spinner_MyAdapter(todays_events.this, 0, listVOs);
        materialBetterSpinnerCampuses.setAdapter(todayseventsspinnerMyAdapter);


        //------------------------campuses SPINNER!!!!! STOP--------------------------------

        //--------STOP GET CAMPUSES--------






                /*----GET TYPES ---*/

         /*--spinner implementation--*/
        Callback myCallbackType = new Callback();
        try {

            String statustype = (myCallbackType.execution_Get("http://"+serverURL+":8000/campus/?university=1", token, "GET", "No JsonData"));

            myTypeArray = new JSONArray(statustype);
            nameListType = new ArrayList<String>();
            idListType = new ArrayList<String>();
            System.out.println(myTypeArray);


            for (int i = 0; i < myTypeArray.length(); i++) {
                JSONObject json_data = myTypeArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameListType.add(i, name);
                idListType.add(i, id);


            }

            System.out.println(nameListType);
            System.out.println(idListType);
            System.out.println(nameListType.get(0));


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

      //  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       //     //Här inne är vad som sker när en grej i listan väljs
       //     Toast toast = Toast.makeText(todays_events.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
       //     toast.show();
            /**Denna toast visar i en liten ruta vilken man valt*/

        }
        //final Spinner camp_spinner = (Spinner) findViewById(R.id.material_spinner_campuses);
        final MaterialBetterSpinner materialBetterSpinnerTypes = (MaterialBetterSpinner) findViewById(R.id.material_spinner_type);
        //String SPINNER_DATA_TESTCAMPUS = items_camp.toArray();

        String[] typesStringArray = new String[items_type.size()];
        typesStringArray = items_type.toArray(typesStringArray);

        System.out.println("items_type is " + items_type);

        //------------------------Types SPINNER START!!!------------------------------
        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA_TYPES/*typesStringArray*/);

        materialBetterSpinnerTypes.setAdapter(typeadapter);

        ArrayList<todays_events_spinner_StateVOTypes> listVOsType = new ArrayList<>();

        for (int i = 0; i < /*typesStringArray*/SPINNER_DATA_TYPES.length; i++) {
            todays_events_spinner_StateVOTypes todayseventsspinnerStateVO = new todays_events_spinner_StateVOTypes();
            todayseventsspinnerStateVO.setTitle(/*typesStringArray*/SPINNER_DATA_TYPES[i]);
            todayseventsspinnerStateVO.setSelected(false);
            listVOsType.add(todayseventsspinnerStateVO);
        }
        todays_events_spinner_MyAdapterTypes todayseventsspinnerMyAdapterType = new todays_events_spinner_MyAdapterTypes(todays_events.this, 0, listVOsType);
        materialBetterSpinnerTypes.setAdapter(todayseventsspinnerMyAdapterType);


        //------------------------Types SPINNER!!!!! STOP--------------------------------

        //-------------STOP GET TYPES-------------




        //  materialBetterSpinnerCampuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()



    //System.out.println(todays_events_spinner_MyAdapter.items_checkedCampuses);
        /*----------------REMOVE THIS COMMENT SO SOMETHING HAPPENS WHEN CLICKING?! /ARVID 12/5 *//*materialBetterSpinnerCampuses.addTextChangedListener(new




    TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s,int start, int count, int after){
            System.out.println("YOU ARE IN BEFORETEXTCHANGED");



        }

        @Override
        public void onTextChanged (CharSequence s,int start, int before, int count){
            System.out.println("YOU ARE IN ONTEXTCHANGED");

        }

        @Override
        public void afterTextChanged (Editable s){
            System.out.println("YOU ARE IN AFTERTEXTCHANGED");

        }






    });
*/


}




//-----------------------------GET CAMPUS SORTING TO DATABASE----------------
    public void sendInfoToDatabase(ArrayList<String> items_checkedCampuses) {
        System.out.println("We now send this CAMPUSES to database from todays_events: "+items_checkedCampuses);


        sendStringCampuses = "";
        boolean resultOfComparison;
        //items_checkedTypes.add(items_checkedTypes.toString());
        for (int k=0; k<items_checkedCampuses.size(); k++) {
            resultOfComparison=items_checkedCampuses.get(k).equals(items_checkedCampuses.get(k));
            sendStringCampuses = sendStringCampuses+((items_checkedCampuses.get(k))+"&");
            sendStringCampuses = sendStringCampuses.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\ ","&20").replaceAll("\\,","");
            if(resultOfComparison == false) {
                items_checkedCampuses.add(items_checkedCampuses.get(k));
            }
        }


        System.out.println("We try in senfInfoToDatabase to send url: "+"http://"+serverURL+":8000/events/?type_event="+sendStringCampuses);


    }


//---------------------GET FROM DATABASE WITH FILTERING -------------
    public void sendInfoToDatabaseType(ArrayList<String> items_checkedTypes) {

        //Here we makethe checked types to a string in the right format to send to database
        sendStringTypes = "";
        boolean resultOfComparison;
        //items_checkedTypes.add(items_checkedTypes.toString());
        for (int k=0; k<items_checkedTypes.size(); k++) {
            resultOfComparison=items_checkedTypes.get(k).equals(items_checkedTypes.get(k));
            sendStringTypes = sendStringTypes+((items_checkedTypes.get(k))+"&");
            sendStringTypes = sendStringTypes.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\ ","&20").replaceAll("\\,","");
            if(resultOfComparison == false) {
                items_checkedTypes.add(items_checkedTypes.get(k));
            }
        }



        System.out.println("We now send this TYPES to database from todays_events: "+sendStringTypes);
        System.out.println("token inside sendInfoTodatbaseType is " + token);
        System.out.println("We try in sendInfoToDatabaseTypes to send url: "+"http://"+serverURL+":8000/events/?type_event="+sendStringTypes);

        //---------------------TESTING RELOADING LIST OFEVENTS
    /*    Callback myCallback = new Callback();

        try {



            String lunchlecturetry = "Lunch Lecture";
            System.out.println("TOKEN IS: "+token);

            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/?type_event="+sendStringTypes, token, "GET", "No JsonData"));
            System.out.println("STATUS IS "+status);


            if (status == "false") {
                Toast.makeText(todays_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            } else {

                JSONArray myEventsArray = new JSONArray(status);


                ListView listView = (ListView) findViewById(R.id.todays_events_list);


                /* --- create hash map that all Json objects are inserted to --- */
      /*          list = new ArrayList<HashMap<String, String>>();
                total_list = new ArrayList<HashMap<String, String>>();
                ListViewAdapter adapter;

                /*create as many hash maps as needed */
      /*          for (int i = 0; i < myEventsArray.length(); i++) {
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
                    //    String id =json_data.getString("id");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN, start_time + "- " + end_time);
                    list.get(i).put(THIRD_COLUMN, owner);
                    list.get(i).put(FOURTH_COLUMN, name);
                    list.get(i).put(DESCRIPTION, description);
                    total_list.add(list.get(i));

                    Log.d(name, "name");
                    Log.d(date, "date");
                    Log.d(start_time, "start");
                    Log.d(end_time, "end");
                    Log.d(description, "description");
                    // Log.d(id, "id");


                }

                adapter = new ListViewAdapter(this, list, listView);
                listView.setAdapter(adapter);


            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        //---------------------STOP TESTING RELOADING LIST OF EVENTS--



    }


}
