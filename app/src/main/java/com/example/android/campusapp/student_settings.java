package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by elsabergman on 2017-04-07.
 * This file loads data from back-end to front-end with user information to display in settings page. It then lets the user change this information and update the database.
 * This page is linked to student_settings.xml
 */

public class student_settings extends student_SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private Switch editSaveSwitch;


    EditText studemailInput,studusernameInput, studfirstnameInput, studlastnameInput;

    ProgressDialog dialog;

    String chosen_campus;
    private ArrayList<HashMap<String, String>> uniList;
    String chosen_uni;
    JSONArray myUniArray;

    String theId,theIdCampus,campusJson;

    String universityJson = "Change University?";


    //url for connection to database
    String url = "212.25.151.161";

    ArrayList<String> idList,nameList,nameCampusList;
    JSONArray myCampusArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_settings, null);
        drawer.addView(contentView, 0);


        /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/


        //---------------TOGGLESWITCH FOR EDIT SAVE info---------------
        //Create the switch for edit/save
        editSaveSwitch = (Switch) findViewById(R.id.editSaveSwitchStudent);

        //Here we make the switch to be on "save" and the text to display "Edit" when starting the page.
        Boolean switchValueEditSave = false;
        editSaveSwitch.setChecked(switchValueEditSave);
        //attach a listener to check for changes in state
        editSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean switchValueEditSave) {
                if (switchValueEditSave) {
                    //Here the user checks the switchbutton and enables the editing. the text chagnes to "Save"
                    editSaveSwitch.setChecked(true);
                    View editView = findViewById(R.id.editSaveSwitchStudent);
                    editInfoStudent(editView);
                    ((Switch) findViewById(R.id.editSaveSwitchStudent)).setText("Save");

                } else {
                    //here the user unchecks the switchbutton and the text is set to "Edit"
                    editSaveSwitch.setChecked(false);
                    View saveView = findViewById(R.id.editSaveSwitchStudent);
                    saveInfoStudent(saveView);
                    ((Switch) findViewById(R.id.editSaveSwitchStudent)).setText("Edit");
                }

            }
        });

        // ------------------END TOGGLESWITCH FOR EDIT SAVE INFO

        /*-------------------- SET MY PROFILE INFO ---------------*/


        studemailInput = (EditText) findViewById(R.id.organization_emailInput_student);
        studusernameInput = (EditText) findViewById(R.id.organization_username_input_student);
        studfirstnameInput = (EditText) findViewById(R.id.firstName_Input_student);
        studlastnameInput = (EditText) findViewById(R.id.lastName_Input_student);



        //Here we get data from database to display once the page is loaded!
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();
        try {

            String status = (myCallback.execution_Get("http://"+url+":8000/profile/", token, "GET", "No JsonData"));

            if (status == "false") {
                Toast.makeText(student_settings.this, "could not fetch user info", Toast.LENGTH_LONG).show();
            } else {
                //Here we get separate objects from json string
                JSONObject myInfoObject = new JSONObject(status);
                String usernameJson = myInfoObject.getString("username");
                String emailJson = myInfoObject.getString("email");
                String firstnameJson = myInfoObject.getString("first_name");
                String lastnameJson = myInfoObject.getString("last_name");
                universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
                campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

                //Fill the edittextfields with data from database.
                studemailInput.setText(emailJson, TextView.BufferType.EDITABLE);
                studusernameInput.setText(usernameJson, TextView.BufferType.EDITABLE);
                studfirstnameInput.setText(firstnameJson, TextView.BufferType.EDITABLE);
                studlastnameInput.setText(lastnameJson, TextView.BufferType.EDITABLE);

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        dialog.dismiss();


            /*----GET UNIVERSITY ---*/
            /*--spinner implementation--*/

        try {
            String status = (myCallback.execution_Get("http://" +url+ ":8000/university/", token, "GET", "No JsonData"));


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


        final Spinner uni_spinner = (Spinner) findViewById(R.id.universitySpinnerSettingsStud);

        ArrayAdapter<String> uniadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        uniadapter.setDropDownViewResource(R.layout.spinner_layout);
        uni_spinner.setAdapter(uniadapter);
        uni_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            /* -- When item in spinner is chosen, check position and call choose campus for that university -- */
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

        final Spinner spinner = (Spinner)findViewById(R.id.campusesSpinnerSettingsStud);
        boolean resultOfComparison_campus;
        final ArrayList<String> items_campus = new ArrayList<String>();
        final ArrayList<String> id_campus = new ArrayList<String>();

        /* -- if array of campuses at chosen university also contains the default campus,
         the default campus should be added to the top of the spinner list.
         After that the other campuses beloning to the chosen university should be listed.
          */
        if (myCampusArray.toString().contains("\"name\":\""+campusJson+"\"")) {
            items_campus.add(campusJson.toString());

            String campus_id = String.valueOf(nameCampusList.indexOf(items_campus.get(0)) + 1);
            id_campus.add(campus_id);

            for (int k = 0; k < nameCampusList.size(); k++) {
                resultOfComparison_campus = nameCampusList.get(k).equals(items_campus.get(0));

                if (resultOfComparison_campus == false) { //compare if default campus equals campus in list to avoid redundancy.
                    items_campus.add(nameCampusList.get(k));
                    campus_id = String.valueOf(k + 1);
                    id_campus.add(campus_id);

                }
            }
        }

        /* if the default campus is not part of the campuses at the chosen university,
        we want to display all campuses belonging to the chosen university and not the default campus. */
        else {

            for (int k = 0; k < nameCampusList.size(); k++) {

                items_campus.add(nameCampusList.get(k));
                String campus_id = String.valueOf(idList.get(k));
                id_campus.add(campus_id);

            }

        }

        //Creates adapter for spinner
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

        //Listens on the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                //In here is what happens when an item in the list is clicked.

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                for (int i = 0; i < myCampusArray.length(); i++) {

                        /* if the chosen campus equals the campus in place i+1 (add 1 because first place is "Choose Campus...") */
                    if (chosen_campus == items_campus.get(i)) {
                        theIdCampus = id_campus.get(i);

                    }
                }
                JSONObject post_dict = new JSONObject(); //creates Json object
                try {
                    //prepares to send the chosen campus to database
                    post_dict.put("campus", theIdCampus);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    Callback myCallback = new Callback();

                    try {
                        //Updates campus
                        String status = (myCallback.execution_Post("http://"+url+":8000/profile/update-campus/", token, "PATCH", post_dict.toString()));

                        if (status == "true") {
                            // Updates campus
                        }
                        if (status == "false") {
                            //Failed to update campus. Notifies the user.
                            Toast.makeText(student_settings.this, "Campus could not be updated", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }


            }
                @Override
                public void onNothingSelected (AdapterView < ? > parent){
                }


        });

    }



    /*-----------------EDIT AND SAVE MY PROFILE INFO ------------  */
    //Edit info on My Profile(when you click edit). this makes it clickable and the text becomes white
    public void editInfoStudent(View view) {
        studfirstnameInput.setFocusableInTouchMode(true);
        studlastnameInput.setFocusableInTouchMode(true);
        studfirstnameInput.setTextColor(this.getResources().getColor(R.color.white));
        studlastnameInput.setTextColor(this.getResources().getColor(R.color.white));
    }


    ///Save info on My Profile(when you click save). Also set the text so it is not editable and dark.
    public void saveInfoStudent(View view) {


        //Put to strings for JSON to handle
        String orgfirstname = studfirstnameInput.getText().toString();
        String orglastname = studlastnameInput.getText().toString();

         /*-----------remember token in saveInfoStudent method--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/


        //Here the connection to the database is managed to save the input made by the user.
        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("first_name" , orgfirstname);
            post_dict.put("last_name", orglastname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (post_dict.length() > 0) {

            Callback myCallback = new Callback();

            try {

                String status = (myCallback.execution_Post("http://"+url+":8000/profile/", token, "PATCH", post_dict.toString()));
                if (status == "true") {
                    //If we succeed to update database, the text is not more editable for the user.
                    studfirstnameInput.setFocusable(false);
                    studfirstnameInput.setClickable(false);
                    studlastnameInput.setFocusable(false);
                    studlastnameInput.setClickable(false);
                    studfirstnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
                    studlastnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));

                }
                if (status == "false") {
                    Toast.makeText(student_settings.this, "User could not be edited", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }


        }

    }


    //---------This function sends the user to a page for changing password
    public void editPasswordStud(View view) {
        Button btn = (Button) findViewById(R.id.change_password_button);
        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Intent intent1 = new Intent(student_settings.this, change_password.class);
                startActivity(intent1);
            }

        });
    }


}











