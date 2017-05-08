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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.R.id.list;
import static com.android.volley.Request.Method.GET;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;
import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.end_time;
import static com.example.android.campusapp.R.id.languageSpinnerSettings;
import static com.example.android.campusapp.R.id.organization_nameInput;
import static com.example.android.campusapp.R.id.start_time;
import static com.example.android.campusapp.R.id.universitySpinnerSettings;
import static com.example.android.campusapp.R.id.username;


/**
 * Created by elsabergman on 2017-04-07.
 */

public class org_settings extends SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;

    EditText orgnameInput;
    EditText orgemailInput;
    EditText orgusernameInput;
    EditText orgpasswordInput1;
    ProgressDialog dialog;
    String token;
    String status;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> total_list;
    //EditText orgpasswordInput2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_settings, null);
        drawer.addView(contentView, 0);


        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println("token inside oncreate is "+token);
        /*----------------------------------------------*/



        TextView myProfile = (TextView) findViewById(R.id.my_profile);
        TextView org = (TextView) findViewById(R.id.organization);
        // TextView org_name = (TextView)findViewById(R.id.organization_name);
        TextView email = (TextView) findViewById(R.id.email);
        // TextView org_email= (TextView)findViewById(R.id.organization_email);
        //TextView username = (TextView) findViewById(username);
        // TextView org_username = (TextView)findViewById(R.id.organization_username);
        TextView password = (TextView) findViewById(R.id.password);
        // TextView org_password = (TextView)findViewById(R.id.organization_password);

        TextView campus_settings = (TextView) findViewById(R.id.campus_settings);
        TextView default_campus = (TextView) findViewById(R.id.default_campus);
        //  TextView default_campus_choice = (TextView)findViewById(R.id.default_campus_choice);
        TextView other_settings = (TextView) findViewById(R.id.other_settings);
        TextView notifications = (TextView) findViewById(R.id.notifications);
        TextView mySwitch_text = (TextView) findViewById(R.id.mySwitch);
        TextView switchStatus2 = (TextView) findViewById(R.id.switchStatus);

        TextView language_settings = (TextView) findViewById(R.id.language_settings);
        // TextView language_choice = (TextView) findViewById(R.id.language_choice);

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Slabo27px-Regular.ttf");

        myProfile.setTypeface(custom_font);
        org.setTypeface(custom_font);
        //org_name.setTypeface(custom_font);
        email.setTypeface(custom_font);
        //org_email.setTypeface(custom_font);
        //username.setTypeface(custom_font);
        //org_username.setTypeface(custom_font);
        password.setTypeface(custom_font);
        //org_password.setTypeface(custom_font);

        campus_settings.setTypeface(custom_font);
        default_campus.setTypeface(custom_font);
        //default_campus_choice.setTypeface(custom_font);
        other_settings.setTypeface(custom_font);
        notifications.setTypeface(custom_font);
        mySwitch_text.setTypeface(custom_font);
        switchStatus2.setTypeface(custom_font);
        //language_choice.setTypeface(custom_font);
        language_settings.setTypeface(custom_font);

        //Create the switch for notifications on/off
        switchStatus = (TextView) findViewById(R.id.notifications);
        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
       // mySwitch.setChecked(true);


        //Here we makes the app remember earlier decision of user for notifications settings
        final SharedPreferences sharedPref2 = getSharedPreferences("toggleExample", Context.MODE_PRIVATE);
        Boolean switchValue = sharedPref2.getBoolean("notification",false);
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
        orgpasswordInput1 = (EditText) findViewById(R.id.organization_password_input1);
       // orgemailInput = (EditText) findViewById(R.id.student_emailInput);
       // orgusernameInput = (EditText) findViewById(R.id.student_username_input);
       // orgpasswordInput1 = (EditText) findViewById(R.id.student_password_input1);


       /* Here we have sharedpref stuff - delete after database is up
        //Här under sätter vi så att de ihågkomna värdena sätts in i My Profile när vi går in på Settings
        SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

        String orgName = sharedPref.getString("orgName", "");
        String orgEmail = sharedPref.getString("orgEmail", "");
        String orgUserName = sharedPref.getString("orgUserName", "");
        String orgPassword1 = sharedPref.getString("orgPassword1", "");
        String orgPassword2 = sharedPref.getString("orgPassword2", "");*/






        //Here we get data from database to display once the page is loaded!
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();


        Callback myCallback = new Callback();
        try {
            String status = (myCallback.execution_Get("http://212.25.150.89:8000/users/profile/", token, "GET", "No JsonData"));
            System.out.println("status is "+status);

            if (status == "false"){
                Toast.makeText(org_settings.this, "could not fetch user info", Toast.LENGTH_LONG).show();
            }
            else {
                //Here we get separate objects from json string
                JSONObject myInfoObject = new JSONObject(status);
                System.out.println("This row is just after myinfoarray is created");

                String usernameJson = myInfoObject.getString("username");
                //String emailJson = myInfoObject.getString("email");
                //String orgnameJson = myInfoObject.getString("org_name");

                //System.out.println("emailJson is "+emailJson);
                System.out.println("username is "+usernameJson);
                //System.out.println("orgname is"+orgnameJson);

                //orgnameInput.setText(orgnameJson, TextView.BufferType.EDITABLE);
                //orgemailInput.setText(emailJson, TextView.BufferType.EDITABLE);
                orgusernameInput.setText(usernameJson, TextView.BufferType.EDITABLE);
                //we might now change password here because it is difficult to load in from database due to security reasons
                //orgpasswordInput1.setText("hej123456", TextView.BufferType.EDITABLE);

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        dialog.dismiss();


            //Här nere sätter vi vad edittextfields blir när vi startar sidan. sker numera inne i jsonanropet. Kan tas bort när databaskoppling funkis
        //EditText newOrgName = (EditText) findViewById(R.id.organization_nameInput);
       // orgnameInput.setText("arv", TextView.BufferType.EDITABLE);
        //orgemailInput.setText("arv@cool.com", TextView.BufferType.EDITABLE);
        //orgusernameInput.setText("arv", TextView.BufferType.EDITABLE);
        //orgpasswordInput1.setText("hej123456", TextView.BufferType.EDITABLE);
        //orgpasswordInput2.setText("testpass2", TextView.BufferType.EDITABLE);

    }



        //Edit info on My Profile(when you click edit). this makes it clickable and the text becomes white
    public void editInfo(View view) {
        orgnameInput.setFocusableInTouchMode(true);
        orgemailInput.setFocusableInTouchMode(true);
        orgusernameInput.setFocusableInTouchMode(true);
        orgpasswordInput1.setFocusableInTouchMode(true);
        orgnameInput.setTextColor(this.getResources().getColor(R.color.white));
        orgemailInput.setTextColor(this.getResources().getColor(R.color.white));
        orgusernameInput.setTextColor(this.getResources().getColor(R.color.white));
        orgpasswordInput1.setTextColor(this.getResources().getColor(R.color.white));
    }


    ///Save info on My Profile(when you click save). Also set the text so it is not editable and dark.
    public void saveInfo(View view) {

            orgnameInput.setFocusable(false);
            orgnameInput.setClickable(false);
            orgemailInput.setFocusable(false);
            orgemailInput.setClickable(false);
            orgusernameInput.setFocusable(false);
            orgusernameInput.setClickable(false);
            orgpasswordInput1.setFocusable(false);
            orgpasswordInput1.setClickable(false);
            orgnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgemailInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgusernameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgpasswordInput1.setTextColor(this.getResources().getColor(R.color.darkest_blue));


            //Put to strings for JSON to handle
            String orgname = orgnameInput.getText().toString();
            String orgemail = orgemailInput.getText().toString();
            String orgusername = orgusernameInput.getText().toString();
            String orgpassword = orgpasswordInput1.getText().toString();


         /*-----------remember token in saveInfo method--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println("token inside saveInfo is "+token);
        /*----------------------------------------------*/


            //Här nedanför sker kopplingen till databasen med JASON osv
            JSONObject post_dict = new JSONObject();

            try {
                //post_dict.put("org_name" , orgname);
                post_dict.put("username" , orgusername);
                //We will see how password is saved or edited due to security reasons Arvid 4/5
                //post_dict.put("password", orgpassword);
                //We are waiting with email because it is hard to change in the database Arvid 4/5
                //post_dict.put("email", orgemail);
                //post_dict.put("groups","Organisation");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (post_dict.length() > 0) {

                Callback myCallback = new Callback();

                try {

                    //This does not work to update info to database but we are working on it Arvid 8/5

                    System.out.println("post_dict is " + post_dict.toString());
                    String status = (myCallback.execution_Post("http://212.25.150.89:8000/users/profile/", token , "PATCH",post_dict.toString()));
                    System.out.println("status in save is "+status);
                    System.out.println("token inside saveInfo is " + token);
                    if (status == "true") {
                        //Intent intent = new Intent(org_settings.this, org_settings.class);
                        //startActivity(intent);
                        Toast.makeText(org_settings.this, "My profile sucessfully edited", Toast.LENGTH_LONG).show();
                    }if (status =="false") {
                        Toast.makeText(org_settings.this, "User could not be edited", Toast.LENGTH_LONG).show();
                    }
                }

                catch (Exception e) {

                    System.out.println("Could not create suer");
                }


            }


            /*I det utkommenterade nedanför sköts massa sharedpreference-grejer som inte är nödvändigt med databasen. Ska raderas när databaskopplingen är fungerande.
            /*
            SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("orgName", orgnameInput.getText().toString());
            editor.putString("orgEmail", orgemailInput.getText().toString());
            editor.putString("orgUserName", orgusernameInput.getText().toString());
            editor.putString("orgPassword1", orgpasswordInput1.getText().toString());

            editor.apply();

            String orgName = sharedPref.getString("orgName", "");
            String orgEmail = sharedPref.getString("orgEmail", "");
            String orgUserName = sharedPref.getString("orgUserName", "");
            String orgPassword1 = sharedPref.getString("orgPassword1", "");
            String orgPassword2 = sharedPref.getString("orgPassword2", "");

            orgnameInput.setText(orgName, TextView.BufferType.EDITABLE);
            orgemailInput.setText(orgEmail, TextView.BufferType.EDITABLE);
            orgusernameInput.setText(orgUserName, TextView.BufferType.EDITABLE);
            orgpasswordInput1.setText(orgPassword1, TextView.BufferType.EDITABLE);
            */

    }


}













