package com.example.android.campusapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.dateEvent;
import static com.example.android.campusapp.R.id.languageSpinnerSettings;
import static com.example.android.campusapp.R.id.myCampus;
import static com.example.android.campusapp.R.id.organization_nameInput;
import static com.example.android.campusapp.R.id.start_time;
import static com.example.android.campusapp.R.id.uni_spinner;
import static com.example.android.campusapp.R.id.universitySpinnerSettings;

/**
 * Created by elsabergman on 2017-04-07.
 */

public class org_settings extends SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private Switch editSaveSwitch;

    EditText orgnameInput;
    EditText orgemailInput;
    EditText orgusernameInput;
    EditText orgfirstnameInput;
    EditText orglastnameInput;

    ProgressDialog dialog;

    String chosen_campus;
    private ArrayList<HashMap<String, String>> uniList;
    String chosen_uni;
    JSONArray myUniArray;
    String theId;
    String theIdCampus;
    String theIdRoom;
    String chosen_room;
    String universityJson = "Change University?";
    String campusJson;
    String url = "130.243.181.70";


    ArrayList<String> idList;
    ArrayList<String> nameList;
    JSONArray myCampusArray;
    ArrayList<String> nameCampusList;
    ArrayList<String> idCampusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_settings, null);
        drawer.addView(contentView, 0);


        /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println("token inside oncreate is " + token);
        /*----------------------------------------------*/

        //Create the switch for notifications on/off
        switchStatus = (TextView) findViewById(R.id.notifications);
        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //Here we makes the app remember earlier decision of user for notifications settings
        final SharedPreferences sharedPref2 = getSharedPreferences("toggleExample", Context.MODE_PRIVATE);
        Boolean switchValue = sharedPref2.getBoolean("notification", false);
        mySwitch.setChecked(switchValue);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //Here we check/uncheck the switch and remember the value
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("toggleExample", MODE_PRIVATE).edit();
                    sharedPref2.edit().putBoolean("notification", true).apply();
                    mySwitch.setChecked(true);
                    Toast toast = Toast.makeText(org_settings.this, "Notifications on", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("toggleExample", MODE_PRIVATE).edit();
                    sharedPref2.edit().putBoolean("notification", false).apply();
                    mySwitch.setChecked(false);
                    Toast toast = Toast.makeText(org_settings.this, "Notifications off", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


        //--------------- START TOGGLESWITCH FOR EDIT SAVE info---------------

        editSaveSwitch = (Switch) findViewById(R.id.editSaveSwitch);
        //Here we make the switch to be on save on start of page
        Boolean switchValueEditSave = false;
        editSaveSwitch.setChecked(switchValueEditSave);
        //attach a listener to check for changes in state
        editSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean switchValueEditSave) {
                if (switchValueEditSave) {
                    editSaveSwitch.setChecked(true);
                    View editView = findViewById(R.id.editSaveSwitch);
                    editInfo(editView);
                    ((Switch) findViewById(R.id.editSaveSwitch)).setText("Save");

                } else {
                    editSaveSwitch.setChecked(false);
                    View saveView = findViewById(R.id.editSaveSwitch);
                    saveInfo(saveView);
                    ((Switch) findViewById(R.id.editSaveSwitch)).setText("Edit");
                }

            }
        });

        // ------------------END TOGGLESWITCH FOR EDIT SAVE INFO





        //Here vi initiate the spinners
        final Spinner spinnerSetLanguage = (Spinner) findViewById(languageSpinnerSettings);



        /*-------------------- SET MY PROFILE INFO ---------------*/

        orgnameInput = (EditText) findViewById(R.id.organization_nameInput);
        orgemailInput = (EditText) findViewById(R.id.organization_emailInput);
        orgusernameInput = (EditText) findViewById(R.id.organization_username_input);
        orgfirstnameInput = (EditText) findViewById(R.id.firstName_Input);
        orglastnameInput = (EditText) findViewById(R.id.lastName_Input);




        //Here we get data from database to display once the page is loaded!
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();
        try {

            String status = (myCallback.execution_Get("http://"+url+":8000/profile/", token, "GET", "No JsonData"));

            System.out.println("status is " + status);

            if (status == "false") {
                Toast.makeText(org_settings.this, "could not fetch user info", Toast.LENGTH_LONG).show();
            } else {
                //Here we get separate objects from json string
                JSONObject myInfoObject = new JSONObject(status);
                System.out.println("This row is just after myinfoarray is created");

                String usernameJson = myInfoObject.getString("username");
                String emailJson = myInfoObject.getString("email");
                String orgnameJson = myInfoObject.getString("org_name");
                String firstnameJson = myInfoObject.getString("first_name");
                String lastnameJson = myInfoObject.getString("last_name");
                universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
                campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");

                System.out.println("emailJson is "+emailJson);
                System.out.println("username is " + usernameJson);
                System.out.println("orgname is"+orgnameJson);
                System.out.println("universityname is "+universityJson);
                System.out.println("campusname is "+campusJson);

                orgnameInput.setText(orgnameJson, TextView.BufferType.EDITABLE);
                orgemailInput.setText(emailJson, TextView.BufferType.EDITABLE);
                orgusernameInput.setText(usernameJson, TextView.BufferType.EDITABLE);
                orgfirstnameInput.setText(firstnameJson, TextView.BufferType.EDITABLE);
                orglastnameInput.setText(lastnameJson, TextView.BufferType.EDITABLE);


                //------------Raden under ska sätta så användarens värde på university spinnern sätts till valda när den går in på sidan. Men setSelection funkar ej /Arvid 9/5
                //uni_spinner.setSelection(nameList.indexOf(universityJson));

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

        //items_uni.add("Change University?");
       /* for (int i=0; i<nameList.size(); i++) {
            items_uni.add(nameList.get(i));
        }*/

        /*------------------add universities to spinner list, with chosen uni as the first element */

        boolean resultOfComparisonUni;
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


        final Spinner uni_spinner = (Spinner) findViewById(R.id.universitySpinnerSettings);

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


    /*----GET CAMPUS ----*/

    void ChooseMyCampus(String theId, final String token) {

        Callback myCallback = new Callback();
        try {

            String all_campuses = (myCallback.execution_Get("http://"+url+":8000/campus/?university="+theId, token, "GET", "No JsonData"));


            myCampusArray = new JSONArray(all_campuses);
            nameCampusList = new ArrayList<String>();
            idList = new ArrayList<String>();



          //  nameCampusList.add(campusJson);


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


        final Spinner spinner = (Spinner)findViewById(R.id.campusesSpinnerSettings);
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
                String campus_id = String.valueOf(idList.get(k)); //prova köra med detta efter lunch
                id_campus.add(campus_id); //fel med id för campus ultuna

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

                    }
                }


                JSONObject post_dict = new JSONObject(); //creates Json object

                try {

                    post_dict.put("campus", theIdCampus);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    Callback myCallback = new Callback();

                    try {

                        String status = (myCallback.execution_Post("http://"+url+":8000/profile/update-campus/", token,"PATCH",post_dict.toString()));
                        if (status == "true") {
                            //Toast.makeText(org_settings.this, "Campus successfully updated", Toast.LENGTH_LONG).show();
                        }if(status == "false"){
                            Toast.makeText(org_settings.this, "Campus could not be updated", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
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






    /*-----------------EDIT AND SAVE MY PROFILE INFO ------------  */
    //Edit info on My Profile(when you click edit). this makes it clickable and the text becomes white
    public void editInfo(View view) {
        orgfirstnameInput.setFocusableInTouchMode(true);
        orglastnameInput.setFocusableInTouchMode(true);
        orgfirstnameInput.setTextColor(this.getResources().getColor(R.color.white));
        orglastnameInput.setTextColor(this.getResources().getColor(R.color.white));
    }


    ///Save info on My Profile(when you click save). Also set the text so it is not editable and dark.
    public void saveInfo(View view) {


        //Put to strings for JSON to handle
        String orgfirstname = orgfirstnameInput.getText().toString();
        String orglastname = orglastnameInput.getText().toString();

         /*-----------remember token in saveInfo method--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println("token inside saveInfo is " + token);
        /*----------------------------------------------*/


        //Här nedanför sker kopplingen till databasen med JASON osv
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
                System.out.println("post_dict is " + post_dict.toString());

                String status = (myCallback.execution_Post("http://"+url+":8000/profile/", token, "PATCH", post_dict.toString()));

                System.out.println("status in save is " + status);
                System.out.println("token inside saveInfo is " + token);
                if (status == "true") {

                    orgfirstnameInput.setFocusable(false);
                    orgfirstnameInput.setClickable(false);
                    orglastnameInput.setFocusable(false);
                    orglastnameInput.setClickable(false);
                    orgfirstnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
                    orglastnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));

                    //Toast.makeText(org_settings.this, "My profile sucessfully edited", Toast.LENGTH_LONG).show();
                }
                if (status == "false") {
                    Toast.makeText(org_settings.this, "User could not be edited", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

                System.out.println("Could not edit user info");
            }


        }

    }


    //---------This function should send the user to a page for changing password
    public void editPassword(View view) {
        Toast.makeText(org_settings.this, "You clicked change password button! WOHO!", Toast.LENGTH_LONG).show();
    }
}













