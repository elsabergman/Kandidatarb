package com.example.android.campusapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.campusapp.R.id.campusesSpinnerSettings;
import static com.example.android.campusapp.R.id.languageSpinnerSettings;
import static com.example.android.campusapp.R.id.student_nameInput;

//import static com.example.android.campusapp.R.id.campusesSpinner;

import static com.example.android.campusapp.R.id.universitySpinnerSettings;


/**
 * Created by elsabergman on 2017-04-07.
 */

public class student_settings extends student_SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;

    EditText studentnameInput;
    EditText studentemailInput;
    EditText studentusernameInput;
    EditText studentpasswordInput1;
    EditText studentpasswordInput2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_settings, null);
        drawer.addView(contentView, 0);

        TextView myProfile = (TextView) findViewById(R.id.my_profile);
        TextView stud = (TextView) findViewById(R.id.name);
        // TextView org_name = (TextView)findViewById(R.id.organization_name);
        TextView email = (TextView) findViewById(R.id.email);
        // TextView org_email= (TextView)findViewById(R.id.organization_email);
        TextView username = (TextView) findViewById(R.id.username);
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
        stud.setTypeface(custom_font);
        //org_name.setTypeface(custom_font);
        email.setTypeface(custom_font);
        //org_email.setTypeface(custom_font);
        username.setTypeface(custom_font);
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
                    Toast toast = Toast.makeText(student_settings.this, "Notifications on", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("toggleExample", MODE_PRIVATE).edit();
                    sharedPref2.edit().putBoolean("notification", false).apply();
                    mySwitch.setChecked(false);
                    Toast toast = Toast.makeText(student_settings.this, "Notifications off", Toast.LENGTH_SHORT);
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






        studentnameInput = (EditText) findViewById(student_nameInput);
        studentemailInput = (EditText) findViewById(R.id.student_emailInput);
        studentusernameInput = (EditText) findViewById(R.id.student_username_input);
        studentpasswordInput1 = (EditText) findViewById(R.id.student_password_input1);
        studentpasswordInput2 = (EditText) findViewById(R.id.student_password_input2);

        //Här under sätter vi så att de ihågkomna värdena sätts in i My Profile när vi går in på Settings
        SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

        String studName = sharedPref.getString("orgName", "");
        String studEmail = sharedPref.getString("orgEmail", "");
        String studUserName = sharedPref.getString("orgUserName", "");
        String studPassword1 = sharedPref.getString("orgPassword1", "");
        String studPassword2 = sharedPref.getString("orgPassword2", "");

        //EditText newOrgName = (EditText) findViewById(R.id.student_nameInput);
        studentnameInput.setText(studName, TextView.BufferType.EDITABLE);
        studentemailInput.setText(studEmail, TextView.BufferType.EDITABLE);
        studentusernameInput.setText(studUserName, TextView.BufferType.EDITABLE);
        studentpasswordInput1.setText(studPassword1, TextView.BufferType.EDITABLE);
        studentpasswordInput2.setText(studPassword2, TextView.BufferType.EDITABLE);



    }



    //Edit info on My Profile(when you click edit). this makes it clickable and the text becomes white

    public void editInfo(View view) {
        studentnameInput.setFocusableInTouchMode(true);
        studentemailInput.setFocusableInTouchMode(true);
        studentusernameInput.setFocusableInTouchMode(true);
        studentpasswordInput1.setFocusableInTouchMode(true);
        studentpasswordInput2.setFocusableInTouchMode(true);
        studentnameInput.setTextColor(this.getResources().getColor(R.color.white));
        studentemailInput.setTextColor(this.getResources().getColor(R.color.white));
        studentusernameInput.setTextColor(this.getResources().getColor(R.color.white));
        studentpasswordInput1.setTextColor(this.getResources().getColor(R.color.white));
        studentpasswordInput2.setTextColor(this.getResources().getColor(R.color.white));

    }


    ///Save info on My Profile(when you click save). Also set the text so it is not editable and dark.
    public void saveInfo(View view) {

        if (studentpasswordInput1.getText().toString().equals(studentpasswordInput2.getText().toString())) {
            studentnameInput.setFocusable(false);
            studentnameInput.setClickable(false);
            studentemailInput.setFocusable(false);
            studentemailInput.setClickable(false);
            studentusernameInput.setFocusable(false);
            studentusernameInput.setClickable(false);
            studentpasswordInput1.setFocusable(false);
            studentpasswordInput1.setClickable(false);
            studentpasswordInput2.setFocusable(false);
            studentpasswordInput2.setClickable(false);

            studentnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            studentemailInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            studentusernameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            studentpasswordInput1.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            studentpasswordInput2.setTextColor(this.getResources().getColor(R.color.darkest_blue));

            SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("orgName", studentnameInput.getText().toString());
            editor.putString("orgEmail", studentemailInput.getText().toString());
            editor.putString("orgUserName", studentusernameInput.getText().toString());
            editor.putString("orgPassword1", studentpasswordInput1.getText().toString());
            editor.putString("orgPassword2", studentpasswordInput2.getText().toString());
            editor.apply();

            String studName = sharedPref.getString("orgName", "");
            String studEmail = sharedPref.getString("orgEmail", "");
            String studUserName = sharedPref.getString("orgUserName", "");
            String studPassword1 = sharedPref.getString("orgPassword1", "");
            String studPassword2 = sharedPref.getString("orgPassword2", "");

            studentnameInput.setText(studName, TextView.BufferType.EDITABLE);
            studentemailInput.setText(studEmail, TextView.BufferType.EDITABLE);
            studentusernameInput.setText(studUserName, TextView.BufferType.EDITABLE);
            studentpasswordInput1.setText(studPassword1, TextView.BufferType.EDITABLE);
            studentpasswordInput2.setText(studPassword2, TextView.BufferType.EDITABLE);

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(this, "Passwords does not match, please try again", Toast.LENGTH_SHORT).show();

    }


}













