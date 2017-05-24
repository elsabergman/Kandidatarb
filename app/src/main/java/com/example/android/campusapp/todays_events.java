package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.media.CamcorderProfile.get;
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
//import static com.example.android.campusapp.todays_events_spinner_MyAdapterTypes.items_checkedTypes;


/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends student_SlidingMenuActivity {

    private todays_events_spinner_MyAdapterTypes activiateTypesSpinner;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private Button mButton;
    private PopupWindow mPopupWindow;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> total_list;
    MaterialBetterSpinner materialBetterSpinnerTypes;

    String[] SPINNER_DATA_CAMPUSES = {"Campus:", "Ångström", "Engelska Parken", "ITC", "Ekonomikum"};
    String[] SPINNER_DATA_TYPES = {/*"Type:",*/ "Lunch Lecture", "Promoting Event", "Evening Event","Case Event","Other"};
    String chosen_campuses;

    //String token;
    
    String serverURL = "130.243.182.165";
    private String sendStringTypes = "";
    private String sendStringCampuses ="";

    private String universityJson;
    private String campusJson;
    private String universityIdDefault;
    private String chosen_campus;
    String University;
    private String token = null;
    private String theId = "";
    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> nameListType;
    ArrayList<String> idListType;
    ArrayList<String> items_checkedTypesCopy = new ArrayList<String>();
    TextView textUni;
    JSONArray myCampArray;
    JSONArray myTypeArray;
    JSONArray myUniArray;
    ArrayList<String> nameListUni;

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

        //Add empty string to araylist to not get null
        items_checkedTypesCopy.add("");

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

            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/home-event/", token, "GET", "No JsonData"));



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

  //------------------GET UNIVEFSITY ID TO USE WHEN GET CAMPUS


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


        boolean resultOfComparison_uni;
        final ArrayList<String> items_uni = new ArrayList<String>();
        final ArrayList<String> id_uni = new ArrayList<String>();
        items_uni.add(universityJson.toString());
        String uni_id = String.valueOf(nameListUni.indexOf(items_uni.get(0))+1);
        id_uni.add(uni_id);
        for (int k=0; k<nameListUni.size(); k++) {
            resultOfComparison_uni = nameListUni.get(k).equals(items_uni.get(0));
            System.out.println(resultOfComparison_uni);
            if (resultOfComparison_uni == false) {
                items_uni.add(nameListUni.get(k));
                id_uni.add(String.valueOf(nameListUni.indexOf(items_uni.get(k))));
            }
        }
        System.out.println("id_uni is: "+id_uni);

        universityIdDefault = id_uni.get(0);


        /*----GET CAMPUSES on users default university---*/

         /*--spinner implementation--*/
        Callback myCallbackCampuses = new Callback();
        try {
            //Get
            String status = (myCallbackCampuses.execution_Get("http://"+serverURL+":8000/campus/?university="+universityIdDefault, token, "GET", "No JsonData"));

            myCampArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();
            System.out.println("myCampArray is: "+myCampArray);


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
        items_camp.add("All Campuses");
        for (int i = 0; i < nameList.size(); i++) {
            items_camp.add(nameList.get(i));
        }
        //final Spinner camp_spinner = (Spinner) findViewById(R.id.material_spinner_campuses);
        final Spinner spinnerCampuses = (Spinner) findViewById(R.id.material_spinner_campuses);
        //String SPINNER_DATA_TESTCAMPUS = items_camp.toArray();

        String[] campusesStringArray = new String[items_camp.size()];
        campusesStringArray = items_camp.toArray(campusesStringArray);

        System.out.println("items_camp is " + items_camp);

        //------------------------Campusese SPINNER START!!!------------------------------


        final Spinner uni_spinner = (Spinner) findViewById(R.id.material_spinner_campuses);

        System.out.println("items_uni is "+items_uni);

        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, campusesStringArray);
        //ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        uni_spinner.setAdapter(campusadapter);
        uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Här inne är vad som sker när en grej i listan väljs

                chosen_campus = uni_spinner.getItemAtPosition(uni_spinner.getSelectedItemPosition()).toString();
                System.out.println("creating chosen_campus and chosen_campus is: "+chosen_campus);
                System.out.println("items_camp.get(i1) is "+items_camp.get(1));
//                System.out.println("idList.get(1) is "+ idList.get(1));


                for (int i = 0; i < myCampArray.length(); i++) {


                    System.out.println("We compare "+chosen_campus+" with "+items_camp.get(i+1));


                         /* if the chosen uni equals the uni in place i+1 (add 1 because first place is "ALL universities") */
                    if (chosen_campus.equals(items_camp.get(i+1))/* == items_camp.get(i+1)*/) {
                        theId = "";
                        theId = idList.get(i)+"/";
                        System.out.println("theID in chosen campus is: "+theId);
                        //sendInfoToDatabaseOnlyId();
                        sendInfoToDatabaseType(items_checkedTypesCopy);
                        //activiateTypesSpinner.getCustomView(0, view, parent);
                    }
                    /*else*/ if(chosen_campus.equals("All Campuses")){
                        theId = "";
                        System.out.println("item selected not in items_camp");
                        System.out.println("theID in chosen campus else is: "+theId);
                        //sendInfoToDatabaseOnlyId();
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

         /*--spinner implementation--*/
        Callback myCallbackType = new Callback();
        try {

            String statustype = (myCallbackType.execution_Get("http://"+serverURL+":8000/campus/?university="+universityIdDefault, token, "GET", "No JsonData"));
            System.out.println("statustype is: "+statustype);

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



        }
        final MaterialBetterSpinner materialBetterSpinnerTypes = (MaterialBetterSpinner) findViewById(R.id.material_spinner_type);
        String[] typesStringArray = new String[items_type.size()];
        typesStringArray = items_type.toArray(typesStringArray);
        System.out.println("items_type is " + items_type);

        //------------------------Types SPINNER START!!!------------------------------
        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA_TYPES/*typesStringArray*/);

        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFF");

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

        materialBetterSpinnerTypes.setOnClickListener(new AdapterView.OnClickListener()

                                                             {
                                                                 @Override
                                                                 public void onClick(View v) {

                                                                     for (int i = 0; i < /*typesStringArray*/SPINNER_DATA_TYPES.length; i++) {
                                                                        if(items_checkedTypesCopy.contains(SPINNER_DATA_TYPES[i])){
                                                                         todays_events_spinner_StateVOTypes todayseventsspinnerStateVO = new todays_events_spinner_StateVOTypes();
                                                                         todayseventsspinnerStateVO.setTitle(/*typesStringArray*/SPINNER_DATA_TYPES[i]);
                                                                         todayseventsspinnerStateVO.setSelected(true);
                                                                            System.out.println("ONCLICKED  SPUINNER INSIDE!!!");
                                                                        }
                                                                     }


                                                                     System.out.println("ONCLICKED  SPUINNER!!!");
                                                                     //if (SPINNER_DATA_CAMPUSES.contains(items_checkedTypesCopy)


                                                                    /* System.out.println("KKKKKKKKK");
                                                                     ViewHolder temp = (ViewHolder) v.getTag();
                                                                     temp.mCheckBox.setChecked(!temp.mCheckBox.isChecked());

                                                                     int len = items_checkedTypes.size();
                                                                     for (int i = 0; i < len; i++)
                                                                     {
                                                                         System.out.println("HHHHHHHHHHHH");
                                                                         if (i == position)
                                                                         {
                                                                             (listState.get(position)).setSelected(!(listState.get(position)).isSelected());
                                                                             //Log.i("TAG", "On Click Selected : " + (listState.get(position)).getTitle() + " : " + (listState.get(position)).isSelected());
                                                                             System.out.println("EEEEEEEEEEEEEEEEEEEEEE");
                                                                             break;
                                                                         }
                                                                     }*/


                                                                 }


                                                                /* @Override
                                                                 public void onNothingSelected(AdapterView<?> parent) {

                                                                 }*/
                                                             });


       /* convertView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                System.out.println("KKKKKKKKK");
                ViewHolder temp = (ViewHolder) v.getTag();
                temp.mCheckBox.setChecked(!temp.mCheckBox.isChecked());

                int len = items_checkedTypes.size();
                for (int i = 0; i < len; i++)
                {
                    System.out.println("HHHHHHHHHHHH");
                    if (i == position)
                    {
                        (listState.get(position)).setSelected(!(listState.get(position)).isSelected());
                        //Log.i("TAG", "On Click Selected : " + (listState.get(position)).getTitle() + " : " + (listState.get(position)).isSelected());
                        System.out.println("EEEEEEEEEEEEEEEEEEEEEE");
                        break;
                    }
                }
            }
        });*/

            //------------------------Types SPINNER!!!!! STOP--------------------------------

        //-------------STOP GET TYPES-------------
}






//---------------------GET FROM DATABASE WITH FILTERING,  this is called from todays_events_spinner_MyAdapterTypes.java -------------
    public void sendInfoToDatabaseType(ArrayList<String> items_checkedTypes) {
        items_checkedTypesCopy =  items_checkedTypes;

        //Here we makethe checked types to a string in the right format to send to database
        sendStringTypes = "";
        boolean resultOfComparison;
        //items_checkedTypes.add(items_checkedTypes.toString());
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

        System.out.println("We now send this TYPES to database from todays_events: "+sendStringTypes);
        System.out.println("token inside sendInfoTodatbaseType is " + token);
        System.out.println("theID inside sendInfoTodatbaseType is " + theId);
        System.out.println("We try in sendInfoToDatabaseTypes to send url: "+"http://"+serverURL+":8000/events/"+theId+"?type_event__in="+sendStringTypes);


        //---------------------TESTING RELOADING LIST OFEVENTS
        Callback myCallback = new Callback();


        try {

            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/"+theId+"?type_event__in="+sendStringTypes, token, "GET", "No JsonData"));

            if (status == "false") {
                Toast.makeText(todays_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            } else {

                JSONArray myEventsArray = new JSONArray(status);
                System.out.println("myEventsArray in senddatatodatabasetype is "+myEventsArray);


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
                    String owner = json_data.getString("owner");
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    String id_event =json_data.getString("id");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );
                    list.get(i).put(THIRD_COLUMN,name);
                    list.get(i).put(ID,id_event);
                    list.get(i).put(DESCRIPTION, description);
                    list.get(i).put(FAVORITES,"add event to favorites");
                    total_list.add(list.get(i));
                    if ( url != null) {

                        list.get(i).put(URL, url);
                    }

                    else {

                        list.get(i).put(URL, " ");
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
