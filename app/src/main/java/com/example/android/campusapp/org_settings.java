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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.languageSpinnerSettings;
import static com.example.android.campusapp.R.id.organization_nameInput;
import static com.example.android.campusapp.R.id.universitySpinnerSettings;

/**
 * Created by elsabergman on 2017-04-07.
 */

public class org_settings extends SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;

    EditText orgnameInput;
    EditText orgemailInput;
    EditText orgusernameInput;
    EditText orgfirstnameInput;
    EditText orglastnameInput;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_settings, null);
        drawer.addView(contentView, 0);


        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
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

        //Here vi initiate the spinners and then makes the value selected by the user the "default" in all spinners
        final Spinner spinnerSetCampus = (Spinner) findViewById(campusesSpinnerSettings);
        final Spinner spinnerSetUni = (Spinner) findViewById(universitySpinnerSettings);
        final Spinner spinnerSetLanguage = (Spinner) findViewById(languageSpinnerSettings);
        final SharedPreferences sharedPref3 = getSharedPreferences("spinnerSettings", Context.MODE_PRIVATE);
        final SharedPreferences sharedPref4 = getSharedPreferences("spinnerSettings2", Context.MODE_PRIVATE);
        final SharedPreferences sharedPref5 = getSharedPreferences("spinnerSettings3", Context.MODE_PRIVATE);
        int spinnerPositionCampus = (int) sharedPref3.getInt("campusSpinnerText", 0);
        int spinnerPositionUni = (int) sharedPref4.getInt("uniSpinnerText", 0);
        int spinnerPositionLanguage = (int) sharedPref5.getInt("languageSpinnerText", 0);
        spinnerSetCampus.setSelection(spinnerPositionCampus);
        spinnerSetUni.setSelection(spinnerPositionUni);
        spinnerSetLanguage.setSelection(spinnerPositionLanguage);

        //Here we take care of the spinner that selects default campus and puts it to selected campus by the user.
        spinnerSetCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                SharedPreferences sharedPref3 = getSharedPreferences("spinnerSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorCampus = sharedPref3.edit();

                editorCampus.putInt("campusSpinnerText", i);
                editorCampus.apply();

                int spinnerPositionCampus = (int) sharedPref3.getInt("campusSpinnerText", 0);
                spinnerSetCampus.setSelection(spinnerPositionCampus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });
        //Here we take care of the spinner that selects default university and puts it to selected campus by the user.
        spinnerSetUni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                SharedPreferences sharedPref4 = getSharedPreferences("spinnerSettings2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorUni = sharedPref4.edit();

                editorUni.putInt("uniSpinnerText", i);
                editorUni.apply();

                int spinnerPositionUni = (int) sharedPref4.getInt("uniSpinnerText", 0);
                spinnerSetUni.setSelection(spinnerPositionUni);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

        //Here we take care of the spinner that selects default university and puts it to selected campus by the user.
        spinnerSetLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                SharedPreferences sharedPref5 = getSharedPreferences("spinnerSettings3", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorLanguage = sharedPref5.edit();

                editorLanguage.putInt("languageSpinnerText", i);
                editorLanguage.apply();

                int spinnerPositionLanguage = (int) sharedPref5.getInt("languageSpinnerText", 0);
                spinnerSetLanguage.setSelection(spinnerPositionLanguage);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });


        orgnameInput = (EditText) findViewById(organization_nameInput);
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
            String status = (myCallback.execution_Get("http://130.242.109.166:8000/users/profile/", token, "GET", "No JsonData"));
            System.out.println("status is " + status);

            if (status == "false") {
                Toast.makeText(org_settings.this, "could not fetch user info", Toast.LENGTH_LONG).show();
            } else {
                //Here we get separate objects from json string
                JSONObject myInfoObject = new JSONObject(status);
                System.out.println("This row is just after myinfoarray is created");

                String usernameJson = myInfoObject.getString("username");
                String emailJson = myInfoObject.getString("email");
                // Denna rad kommer in när ordnat i databasen Arvid 8/5  String orgnameJson = myInfoObject.getString("org_name");
                String firstnameJson = myInfoObject.getString("first_name");
                String lastnameJson = myInfoObject.getString("last_name");

                System.out.println("emailJson is "+emailJson);
                System.out.println("username is " + usernameJson);
                // Denna rad kommer in när ordnat i databasen Arvid 8/5  System.out.println("orgname is"+orgnameJson);

                // Denna rad kommer in när ordnat i databasen Arvid 8/5  orgnameInput.setText(orgnameJson, TextView.BufferType.EDITABLE);
                orgemailInput.setText(emailJson, TextView.BufferType.EDITABLE);
                orgusernameInput.setText(usernameJson, TextView.BufferType.EDITABLE);
                orgfirstnameInput.setText(firstnameJson, TextView.BufferType.EDITABLE);
                orglastnameInput.setText(lastnameJson, TextView.BufferType.EDITABLE);

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        dialog.dismiss();


    }


    //Edit info on My Profile(when you click edit). this makes it clickable and the text becomes white
    public void editInfo(View view) {
        orgfirstnameInput.setFocusableInTouchMode(true);
        orglastnameInput.setFocusableInTouchMode(true);
        orgfirstnameInput.setTextColor(this.getResources().getColor(R.color.white));
        orglastnameInput.setTextColor(this.getResources().getColor(R.color.white));
    }


    ///Save info on My Profile(when you click save). Also set the text so it is not editable and dark.
    public void saveInfo(View view) {
        orgfirstnameInput.setFocusable(false);
        orgfirstnameInput.setClickable(false);
        orglastnameInput.setFocusable(false);
        orglastnameInput.setClickable(false);
        orgfirstnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
        orglastnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));

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
                String status = (myCallback.execution_Post("http://130.242.109.166:8000/users/profile/", token, "PATCH", post_dict.toString()));
                System.out.println("status in save is " + status);
                System.out.println("token inside saveInfo is " + token);
                if (status == "true") {
                    //Intent intent = new Intent(org_settings.this, org_settings.class);
                    //startActivity(intent);
                    Toast.makeText(org_settings.this, "My profile sucessfully edited", Toast.LENGTH_LONG).show();
                }
                if (status == "false") {
                    Toast.makeText(org_settings.this, "User could not be edited", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

                System.out.println("Could not create suer");
            }


        }

    }


    //---------This function should send the user to a page for changing password
    public void editPassword(View view) {
        Toast.makeText(org_settings.this, "You clicked change password button! WOHO!", Toast.LENGTH_LONG).show();
    }
}













