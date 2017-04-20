package com.example.android.campusapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    EditText orgpasswordInput2;
    TextView testinputShower;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_settings, null);
        drawer.addView(contentView, 0);

        TextView myProfile = (TextView)findViewById(R.id.my_profile);
        TextView org = (TextView)findViewById(R.id.organization);
       // TextView org_name = (TextView)findViewById(R.id.organization_name);
        TextView email = (TextView)findViewById(R.id.email);
       // TextView org_email= (TextView)findViewById(R.id.organization_email);
        TextView username = (TextView)findViewById(R.id.username);
       // TextView org_username = (TextView)findViewById(R.id.organization_username);
        TextView password = (TextView)findViewById(R.id.password);
       // TextView org_password = (TextView)findViewById(R.id.organization_password);

        TextView campus_settings = (TextView)findViewById(R.id.campus_settings);
        TextView default_campus = (TextView)findViewById(R.id.default_campus);
        TextView default_campus_choice = (TextView)findViewById(R.id.default_campus_choice);
        TextView other_settings = (TextView)findViewById(R.id.other_settings);
        TextView notifications = (TextView)findViewById(R.id.notifications);
        TextView mySwitch_text = (TextView)findViewById(R.id.mySwitch);
        TextView switchStatus2 = (TextView)findViewById(R.id.switchStatus);

        TextView language_settings = (TextView)findViewById(R.id.language_settings);
        TextView language_choice = (TextView)findViewById(R.id.language_choice);

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/Slabo27px-Regular.ttf");

        myProfile.setTypeface(custom_font);
        org.setTypeface(custom_font);
        //org_name.setTypeface(custom_font);
        email.setTypeface(custom_font);
        //org_email.setTypeface(custom_font);
        username.setTypeface(custom_font);
        //org_username.setTypeface(custom_font);
        password.setTypeface(custom_font);
        //org_password.setTypeface(custom_font);

        campus_settings.setTypeface(custom_font);
        default_campus.setTypeface(custom_font);
        default_campus_choice.setTypeface(custom_font);
        other_settings.setTypeface(custom_font);
        notifications.setTypeface(custom_font);
        mySwitch_text.setTypeface(custom_font);
        switchStatus2.setTypeface(custom_font);
        language_choice.setTypeface(custom_font);
        language_settings.setTypeface(custom_font);



        switchStatus = (TextView) findViewById(R.id.notifications);
        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                  //  switchStatus.setText("ON");
                    Toast toast = Toast.makeText(org_settings.this, "Settings on", Toast.LENGTH_SHORT);
                    toast.show();



                }else{
                   // switchStatus.setText("OFF");
                    Toast toast = Toast.makeText(org_settings.this, "Settings off", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //check the current state before we display the screen
        //*if(mySwitch.isChecked()){
        //    switchStatus.setText("ON");
        //}
        //else {
         //   switchStatus.setText("OFF");
        //}

        orgnameInput = (EditText) findViewById(R.id.organization_nameInput);
        orgemailInput = (EditText) findViewById(R.id.organization_emailInput);
        orgusernameInput = (EditText) findViewById(R.id.organization_username_input);
        orgpasswordInput1 = (EditText) findViewById(R.id.organization_password_input1);
        orgpasswordInput2 = (EditText) findViewById(R.id.organization_password_input2);
        testinputShower = (TextView) findViewById(R.id.testDisplayText);




    }



    //Edit info on My Profile(when you click edit)
    public void editInfo(View view){
        orgnameInput.setFocusableInTouchMode(true);
        orgemailInput.setFocusableInTouchMode(true);
        orgusernameInput.setFocusableInTouchMode(true);
        orgpasswordInput1.setFocusableInTouchMode(true);
        orgpasswordInput2.setFocusableInTouchMode(true);
        orgnameInput.setTextColor(this.getResources().getColor(R.color.white));
        orgemailInput.setTextColor(this.getResources().getColor(R.color.white));
        orgusernameInput.setTextColor(this.getResources().getColor(R.color.white));
        orgpasswordInput1.setTextColor(this.getResources().getColor(R.color.white));
        orgpasswordInput2.setTextColor(this.getResources().getColor(R.color.white));



    }


    ///Save info on My Profile(when you click save)
    public void saveInfo(View view){




        if ( orgpasswordInput1.getText().toString().equals(orgpasswordInput2.getText().toString())) {
            orgnameInput.setFocusable(false);
            orgnameInput.setClickable(false);
            orgemailInput.setFocusable(false);
            orgemailInput.setClickable(false);
            orgusernameInput.setFocusable(false);
            orgusernameInput.setClickable(false);
            orgpasswordInput1.setFocusable(false);
            orgpasswordInput1.setClickable(false);
            orgpasswordInput2.setFocusable(false);
            orgpasswordInput2.setClickable(false);

            orgnameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgemailInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgusernameInput.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgpasswordInput1.setTextColor(this.getResources().getColor(R.color.darkest_blue));
            orgpasswordInput2.setTextColor(this.getResources().getColor(R.color.darkest_blue));



            SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("orgName", orgnameInput.getText().toString());
            editor.putString("orgEmail", orgemailInput.getText().toString());
            editor.putString("orgUserName", orgusernameInput.getText().toString());
            editor.putString("orgPassword1", orgpasswordInput1.getText().toString());
            editor.putString("orgPassword2", orgpasswordInput2.getText().toString());
            editor.apply();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        }


        else
            Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();









       // SharedPreferences sharedPref = getSharedPreferences("orgInfo", Context.MODE_PRIVATE);

        //String orgname = sharedPref.getString("orgName", "");
        //String orgemail = sharedPref.getString("orgEmail", "");
        //testinputShower.setText(orgname + " " + orgemail);




    }


}



