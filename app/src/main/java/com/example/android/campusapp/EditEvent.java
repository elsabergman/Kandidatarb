package com.example.android.campusapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import org.apache.http.io.SessionOutputBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.R.id.campusesSpinnerSettings;

/**
 * Name: EditEvent.java
 * Author: Elsa Bergman
 * Connected to: edit_event.xml
 *
 * This class allows an organization user to edit events that they have previously made. The user can also choose to delete an event
 * on this page. The page looks very similar to add_event.java in terms of design and functionality. The user can edit all input fields
 * that they could add information to in add_event.java, as well as edit the location of the event by choosing different options
 * in the spinners. The user can also edit the text description. This class extends SlidingMenuActivity which means that the user can
 * access the Sliding Menu from this page. It implements DatePickerDialog and TimePickerDialog to enable the user picking a date and time
 * from a calendar and time picker.
 */

public class EditEvent extends SlidingMenuActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    /*instance variables */
    String serverURL = "212.25.151.161";
    String id_event, name, url, theId,chosen_type,theIdCampus,theIdRoom, chosen_room,chosen_campus,chosen_uni, location,event_date, event_time_to, event_time_from;;
    EditText event_name, date_event, company_visiting, start_time, end_time, relevant_links, description;
    JSONArray myUniArray,myRoomArray,myCampusArray;
    ArrayList<String> idList,nameList,nameCampusList,idCampusList,nameRoomList,idRoomList;
    String universityJson;
    String campusJson;
    Button button_edit;
    private DatePickerDialog date_picker;
    private TimePickerDialog time_picker;
    private boolean fromEdit, fromTime;
    private EditText from;
    private EditText time_from, time_to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* launch layout with sliding menu */
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.edit_event, null);

        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);

        /*----------------------------------------------*/

          /*---Fonts for our Logo---*/
        TextView header = (TextView) findViewById(R.id.edit_text);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);
        /*--------------------------*/

          /* ----- Date picker and Time picker implementation ----- */
        Intent intent = getIntent();

        /* id:s from edit_event.xml, which handles the design of the input fields */
        from = (EditText) findViewById(R.id.datefrom);
        time_from = (EditText) findViewById(R.id.edit_start_time);
        time_to = (EditText) findViewById(R.id.edit_end_time);
        Calendar cal = Calendar.getInstance();

        /* date picker is originally set to today's date */
        date_picker = new DatePickerDialog(this, R.style.DialogTheme, this,  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
         /* position of date picker */
        Window window = date_picker.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        /* focus listener */
        from.setOnFocusChangeListener(focusListener);
        from.setInputType(InputType.TYPE_NULL);

        /* Time picker is originally set to real time */
        time_picker = new TimePickerDialog(this, R.style.TimeTheme, this, cal.get(Calendar.HOUR_OF_DAY),(Calendar.MINUTE),true);
           /* position of date picker */
        Window window2 = time_picker.getWindow();
        window2.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window2.setGravity(Gravity.CENTER);

        time_from.setOnFocusChangeListener(focusListener_time);
        time_to.setOnFocusChangeListener(focusListener_time);
        time_from.setInputType(InputType.TYPE_NULL);
        time_to.setInputType(InputType.TYPE_NULL);


        /* Array list with all types of events that the user can choose from */
        ArrayList<String> type = new ArrayList<String>();
        type.add("Choose Type...");
        type.add("Lunch Lecture");
        type.add("Evening Lecture");
        type.add("Case Event");
        type.add("Promoting Event");
        type.add("Other");

        /* Spinner that lets the user pick a type of event, the design of the spinner is implemented in edit_event.xml */
        final Spinner spinner_type = (Spinner)findViewById(R.id.choose_type);
        ArrayAdapter<String> locationadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, type);
        locationadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_type.setAdapter(locationadapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //What happens when an item in the spinner list is chosen

                chosen_type = spinner_type.getItemAtPosition(spinner_type.getSelectedItemPosition()).toString();

            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });

        /* get the id of the event that is being edited from org_my_events.java, which is where the user will click on
        a link in order to access this class. */
        Bundle b = getIntent().getExtras();

        if (b != null)
            id_event = b.getString("ID"); // id of the event being edited

        /* a button that lets the user delete an event */
        Button btn = (Button) findViewById(R.id.remove_event_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback myCallback = new Callback();

                try {

                    /* connection to the database with a delete request */
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


        /* variables for input fields implemented in edit_event.xml */
        event_name = (EditText) findViewById(R.id.edit_name_event);
        company_visiting = (EditText) findViewById(R.id.edit_company_name);
        date_event = (EditText) findViewById(R.id.datefrom);
        start_time = (EditText) findViewById(R.id.edit_start_time);
        end_time = (EditText) findViewById(R.id.edit_end_time);
        relevant_links = (EditText) findViewById(R.id.edit_links);
        description = (EditText) findViewById(R.id.edit_eventDescription);

        Callback myCallback = new Callback();
        try {

            /* get information about the user that is logged in, in order to get the preferred university and campus and thus
            know what university and campuses that should be added to the spinner lists below * /
             */
            String profile = (myCallback.execution_Get("http://"+serverURL+":8000/profile/", token, "GET", "No JsonData"));
            JSONObject myInfoObject = new JSONObject(profile);
            universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
            campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

            /* fetches all information about the event that is being edited */
            String status = (myCallback.execution_Get("http://"+serverURL+":8000/events/"+id_event+"/", token, "GET", "No JsonData"));

            /* Saves all json objects (information about the event) from the string that the database returns to front-end */
            JSONObject json_data = new JSONObject(status);

                String date = json_data.getString("date");
                String name = json_data.getString("name_event");
                String start_time_string = json_data.getString("start_time");
                String end_time_string = json_data.getString("stop_time");
                String description_string = json_data.getString("description");
                String url = json_data.getString("external_url");
                String company = json_data.getString("visiting_organisation");
                location = json_data.getString("campus_name");

            /* displays the saved strings in the different input fields */
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

        /*----GET UNIVERSITY INFORMATION ---*/

        try {

            /* fetch university information */
            String status = (myCallback.execution_Get("http://" + serverURL + ":8000/university/", token, "GET", "No JsonData"));

            /* list with all universities */
            myUniArray = new JSONArray(status);
            /* list that will store names and id:s of all universities */
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();

            /* stores names and id:s of universities */
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

        /*add universities to spinner list, with default university as the first element */
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
                id_uni.add(String.valueOf(k+1));
            }
        }

           /* Spinner that lets the user pick a university, the design of the spinner is implemented in edit_event.xml */
        final Spinner uni_spinner = (Spinner) findViewById(R.id.edit_university);
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
    }


    /*----GET CAMPUS INFORMATION ----*/

    void ChooseMyCampus(String theId, final String token) {

        Callback myCallback = new Callback();
        try {

            String all_campuses = (myCallback.execution_Get("http://"+serverURL+":8000/campus/?university="+theId, token, "GET", "No JsonData"));

            /* list with all campuses */
            myCampusArray = new JSONArray(all_campuses);
            /* saves names and id:s of campuses of chosen university */
            nameCampusList = new ArrayList<String>();
            idList = new ArrayList<String>();

            /* stores names and id:s in arraylist */
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


        final Spinner spinner = (Spinner)findViewById(R.id.edit_campus);
        boolean resultOfComparison_campus;
        final ArrayList<String> items_campus = new ArrayList<String>();
        final ArrayList<String> id_campus = new ArrayList<String>();

        /* -- if array of campuses at chosen university also contains the default campus,
         the default campus should be added to the top of the spinner list.
         After that the other campuses beloning to the chosen university should be listed.
          */
        if (myCampusArray.toString().contains("\"name\":\""+location+"\"")) {
            items_campus.add(location.toString());

            String campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(0)) + 1);
            id_campus.add(campus_id); //fel med id för campus ultuna

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

        /* Spinner that lets the user pick a campus, the design of the spinner is implemented in edit_event.xml */
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

         /* -- When item in spinner is chosen -- */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {


                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                for (int i = 0; i < myCampusArray.length(); i++) {

                    if (chosen_campus == items_campus.get(i))
                    {
                        theIdCampus = id_campus.get(i);

                        ChooseRoom(theIdCampus, token); // call ChooseRoom with the chosen campus id and the token for the user.

                    }
                }
            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });

    }

    /*-----GET ROOM INFORMATION-----*/

    void ChooseRoom(String campusId, final String token) {

        Callback myCallback = new Callback();
        try {

            /* fetches all rooms for the chosen campus */
            String all_rooms = (myCallback.execution_Get("http://" + serverURL + ":8000/campus-location/?campus=" + campusId, token, "GET", "No JsonData"));

              /* list with all rooms for the chosen campus */
            myRoomArray = new JSONArray(all_rooms);
            /*implement array lists for room names and id:s */
            nameRoomList = new ArrayList<String>();
            idRoomList = new ArrayList<String>();


            /* store all room names and id:s in array lists */
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
        /* adds room names to spinner list */
        final ArrayList<String> items_room = new ArrayList<String>();
        boolean resultOfComparison_room;
        items_room.add("Choose Room...");
        for (int k = 0; k < nameRoomList.size(); k++) {
            resultOfComparison_room = nameRoomList.get(k).equals(items_room.get(0));
            if (resultOfComparison_room == false) {
                items_room.add(nameRoomList.get(k));
            }
        }
        /* Spinner that lets the user pick a room, the design of the spinner is implemented in edit_event.xml */
        final Spinner spinner_room = (Spinner) findViewById(R.id.edit_room);
        ArrayAdapter<String> roomadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_room);
        roomadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_room.setAdapter(roomadapter);

         /* -- When item in spinner is chosen -- */
        spinner_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                chosen_room = spinner_room.getItemAtPosition(spinner_room.getSelectedItemPosition()).toString();
                if (chosen_room != "Choose Room...") {


                    for (int i = 0; i < items_room.size(); i++) {

                        if (chosen_room == items_room.get(i)) {
                            theIdRoom = idRoomList.get(i-1);

                            CreateMyEvent(theIdRoom, token); // call CreteMyEvent with the chosen room id and the token for the user.
                        }

                    }
                }


            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });

    }

    /* Handles the final connection to the database where the new information that the user has edited is sent to the database */
    void CreateMyEvent(final String roomId, final String token) {




        /* variables for input fields implemented in edit_event.xml */
        final EditText event_name = (EditText) findViewById(R.id.edit_name_event);
        final EditText company_name = (EditText) findViewById(R.id.edit_company_name);
        final EditText relevant_links = (EditText) findViewById(R.id.edit_links);
        final EditText description = (EditText) findViewById(R.id.edit_eventDescription);
        final EditText date = (EditText) findViewById(R.id.datefrom);
        final EditText starttime = (EditText) findViewById(R.id.edit_start_time);
        final EditText stoptime = (EditText) findViewById(R.id.edit_end_time);


        /* Button that can be clicked when the user wants to save all changes made to event */
        button_edit = (Button) findViewById(R.id.edit_event_button);
        button_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String eventname = event_name.getText().toString(); //saves email input from user

                String companyname = company_name.getText().toString(); //saves password input from user

                String relevantlinks = relevant_links.getText().toString(); //saves password input from user

                String eventdescription = description.getText().toString(); //saves password input from user

                String dateEvent = date.getText().toString(); //saves password input from user
                String start_time = starttime.getText().toString(); //saves password input from user
                String stop_time = stoptime.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object

                /* add the values that the user changed to a Json object */
                try {
                    post_dict.put("type_event", chosen_type);
                    post_dict.put("name_event", eventname);
                    post_dict.put("description", eventdescription);
                    post_dict.put("date", dateEvent);
                    post_dict.put("start_time", start_time);
                    post_dict.put("stop_time", stop_time);
                    post_dict.put("campus_location", roomId);
                    post_dict.put("external_url", relevantlinks);
                    if (companyname.equals(null)){
                        post_dict.put("visiting_organisation"," ");
                    } else{
                    post_dict.put("visiting_organisation", companyname);}



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {


                    Callback myCallback = new Callback();

                    try {


                        /* makes a PATCH request to the database which means that the original information is overwritten by the new information */
                        String status = (myCallback.execution_Post("http://"+serverURL+":8000/events/"+id_event+"/", token,"PATCH",post_dict.toString()));

                        if (status == "true") {
                            Toast.makeText(EditEvent.this, "Event updated successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EditEvent.this, org_my_events.class);
                            startActivity(intent);
                        }if(status == "false"){
                            Toast.makeText(EditEvent.this, "Event could not be updated", Toast.LENGTH_LONG).show();
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

    /* DatePicker, that lets the user pick the date of the event from a calendar */
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
                EditText edit = (EditText) v;
                int len = edit.getText().toString().length();


                    fromEdit = v.getId() == R.id.datefrom;
                    date_picker.show();
            }
        }
    };

    /* TimePicker, that lets the user pick the time of the event from a time picker widget */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String minute_string;
        String hour_string;
        /* checks if the user wants to change the start time of the event or the end time */
        if (fromTime) {
            /* saves time of event in correct format */
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
            event_time_from = hour_string + ":" + minute_string; //save time in HH:MM format
            time_from.setText(event_time_from); // display chosen time

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
            event_time_to= hour_string + ":" + minute_string; //save time in HH:MM format.
            time_to.setText(event_time_to); // display chosen time
        }
    }



    private View.OnFocusChangeListener focusListener_time = new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (hasFocus)
            {

                EditText edit = (EditText) v;
                int len = edit.getText().toString().length();


                    fromTime = v.getId() == R.id.edit_start_time;
                    time_picker.show();
            }
        }
    };

}



