package com.example.android.campusapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.android.volley.Request.Method.HEAD;


/**
 * Created by Anna on 2017-04-04.
 */


public class add_event extends SlidingMenuActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    String chosen_campus;
    private ArrayList<HashMap<String, String>> uniList;
    String chosen_uni,universityJson,campusJson;
    JSONArray myUniArray;
    String theId,theIdCampus,theIdRoom,chosen_room,chosen_type, event_date, event_time_to, event_time_from;
    ArrayList<String> idList;
    ArrayList<String> nameList;
    JSONArray myCampusArray;
    ArrayList<String> nameCampusList,idCampusList,nameRoomList,idRoomList;
    TextView textUser;
    Calendar myCalendar;
    JSONArray myRoomArray;
    private DatePickerDialog date_picker;
    private TimePickerDialog time_picker;
    private boolean fromEdit, fromTime;
    private EditText from;
    private EditText time_from, time_to;
    EditText event_name,company_name,relevant_links,description,datee,starttime,stoptime, edittext;
    String url = "130.243.182.165";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.add_event, null);

        drawer.addView(contentView, 0);


         /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);


        /*----------------------------------------------*/

        Intent intent = getIntent(); //gaunam
        //    User user = (User) intent.getSerializableExtra("user");

        from = (EditText) findViewById(R.id.datefrom);
        time_from = (EditText) findViewById(R.id.start_time);
        time_to = (EditText) findViewById(R.id.end_time);
        Calendar cal = Calendar.getInstance();

        date_picker = new DatePickerDialog(this, R.style.DialogTheme, this,  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        Window window = date_picker.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        from.setOnFocusChangeListener(focusListener);
        from.setInputType(InputType.TYPE_NULL);

        time_picker = new TimePickerDialog(this, R.style.TimeTheme, this, cal.get(Calendar.HOUR_OF_DAY),(Calendar.MINUTE),true);
        Window window2 = time_picker.getWindow();
        window2.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window2.setGravity(Gravity.CENTER);
        time_from.setOnFocusChangeListener(focusListener_time);
        time_to.setOnFocusChangeListener(focusListener_time);
        time_from.setInputType(InputType.TYPE_NULL);
        time_to.setInputType(InputType.TYPE_NULL);



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

            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });


        /* --- GET PROFILE INFORMATION ---- */

        Callback myCallback = new Callback();
        try {
            String default_options = (myCallback.execution_Get("http://"+url+":8000/profile/", token, "GET", "No JsonData"));

            JSONObject myInfoObject = new JSONObject(default_options);
            first_name = myInfoObject.getString("first_name");
            universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
            campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

            textUser = (TextView) findViewById(R.id.welcome);
            textUser.setText("Hello " + first_name + "!");


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
                if (resultOfComparison_uni == false) {
                    items_uni.add(nameList.get(k));
                    id_uni.add(String.valueOf(k+1));
                }
            }


            final Spinner uni_spinner = (Spinner) findViewById(R.id.choose_university);

            ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
            uniadapter.setDropDownViewResource(R.layout.spinner_layout);
            uni_spinner.setAdapter(uniadapter);
            uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

            {
                /* -- When item in spinner is chosen -- */
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    chosen_uni = uni_spinner.getItemAtPosition(uni_spinner.getSelectedItemPosition()).toString();

                        for (int i = 0; i < myUniArray.length(); i++) {
                          /* if the chosen uni equals the uni in place i */
                            if (chosen_uni == items_uni.get(i)) {
                                theId = id_uni.get(i);

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
                        idList = new ArrayList<String>();




                        for (int i = 0; i < myCampusArray.length() ; i++) {
                            JSONObject json_data = myCampusArray.getJSONObject(i);

                            String nameCampus = json_data.getString("name");
                            String idCampus = json_data.getString("id");
                                nameCampusList.add(i, nameCampus);
                                idList.add(i,idCampus);


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

        final Spinner spinner = (Spinner)findViewById(R.id.choose_campus);
        boolean resultOfComparison_campus;
        final ArrayList<String> items_campus = new ArrayList<String>();
        final ArrayList<String> id_campus = new ArrayList<String>();

        /* -- if array of campuses at chosen university also contains the default campus,
         the default campus should be added to the top of the spinner list.
         After that the other campuses belonging to the chosen university should be listed.
          */
        if (myCampusArray.toString().contains("\"name\":\""+campusJson+"\"")) {
            items_campus.add(campusJson.toString());

            String campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(0)) + 1);
            id_campus.add(campus_id); //fel med id för campus Ultuna

            for (int k = 0; k < nameCampusList.size(); k++) {
                resultOfComparison_campus = nameCampusList.get(k).equals(items_campus.get(0));

                if (resultOfComparison_campus == false) { //compare if default campus equals campus in list to avoid redundancy.
                    items_campus.add(nameCampusList.get(k));
                    campus_id = String.valueOf(k + 1);
                    id_campus.add(campus_id);


                }
            }
        } /* if the default campus is not part of the campuses at the chosen university,
        we want to display all campuses belonging to the chosen university and not the default campus. */
        else {

            for (int k = 0; k < nameCampusList.size(); k++) {

                items_campus.add(nameCampusList.get(k));
                String campus_id = String.valueOf(idList.get(k));
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
                //This is what happens when an item in the list is chosen

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                    for (int i = 0; i < myCampusArray.length(); i++) {

                        /* if the chosen campus equals the campus in place i+1 (add 1 because first place is "Choose Campus...") */
                            if (chosen_campus == items_campus.get(i))
                            {
                                theIdCampus = id_campus.get(i);
                                ChooseRoom(theIdCampus, token);

                        }
                    }

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
                //In here is what happens when an item in the list is selected

                chosen_room = spinner_room.getItemAtPosition(spinner_room.getSelectedItemPosition()).toString();
                if (chosen_room != "Choose Room...") {


                    for (int i = 0; i < items_room.size(); i++) {

                        if (chosen_room == items_room.get(i)) {
                            theIdRoom = idRoomList.get(i-1);
                            CreateMyEvent(theIdRoom, token);
                        }

                    }
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
        final EditText date = (EditText) findViewById(R.id.datefrom);
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
                    post_dict.put("type_event", chosen_type);
                    post_dict.put("name_event", eventname);
                    post_dict.put("description", eventdescription);
                    post_dict.put("date", event_date);
                    post_dict.put("start_time", event_time_from);
                    post_dict.put("stop_time", event_time_to);
                    post_dict.put("external_url", relevantlinks);
                    post_dict.put("campus_location", roomId);


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
                            startActivity(intent);}
                        if(status == "false"){
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


     @Override
     public void onDateSet(DatePicker dp, int y, int m, int d) {
          event_date = y + "-" + "0" + (m + 1) + "-" + d;


         if (fromEdit) {
             from.setText(event_date);
         }
     }

     private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener()
     {
         @Override
         public void onFocusChange(View v, boolean hasFocus)
         {
             if (hasFocus)
             {
                 //   \/ Optional \/
                 EditText edit = (EditText) v;
                 int len = edit.getText().toString().length();

                 if (len == 0)
                 {
                     //   /\ Optional /\

                     fromEdit = v.getId() == R.id.datefrom;
                     date_picker.show();

                     //   \/ Optional \/
                 }
                 else
                 {
                     edit.setSelection(0, len);
                 }
                 //   /\ Optional /\
             }
         }
     };

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String minute_string;
        String hour_string;
        if (fromTime) {
            event_time_from = hourOfDay + ":" + minute;
            if (minute < 10) {
                minute_string = "0" + minute;
            } else {
                minute_string = String.valueOf(minute);
            }
            if (hourOfDay < 10) {
                hour_string = "0" + hourOfDay;
            } else {
                hour_string = String.valueOf(hourOfDay);
            }
            event_time_from = hour_string + ":" + minute_string;
            time_from.setText(event_time_from);
        } else {
            event_time_to = hourOfDay + ":" + minute;
            if (minute < 10) {
                minute_string = "0" + minute;
            } else {
                minute_string = String.valueOf(minute);
            }
            if (hourOfDay < 10) {
                hour_string = "0" + hourOfDay;
            } else {
                hour_string = String.valueOf(hourOfDay);
            }
            event_time_to= hour_string + ":" + minute_string;
            time_to.setText(event_time_to);
        }
    }



    private View.OnFocusChangeListener focusListener_time = new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (hasFocus)
            {
                //   \/ Optional \/
                EditText edit = (EditText) v;
                int len = edit.getText().toString().length();

                if (len == 0)
                {
                    //   /\ Optional /\

                    fromTime = v.getId() == R.id.start_time;
                    time_picker.show();

                    //   \/ Optional \/
                }
                else
                {
                    edit.setSelection(0, len);
                }
                //   /\ Optional /\
            }
        }
    };
    }












