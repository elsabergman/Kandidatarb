package com.example.android.campusapp;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
/**
 * Created by elsabergman on 2017-04-07.
 */

public class org_settings extends SlidingMenuActivity {

    private TextView switchStatus;
    private Switch mySwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_settings, null);
        drawer.addView(contentView, 0);

        TextView myProfile = (TextView)findViewById(R.id.my_profile);
        TextView org = (TextView)findViewById(R.id.organization);
        TextView org_name = (TextView)findViewById(R.id.organization_name);
        TextView email = (TextView)findViewById(R.id.email);
        TextView org_email= (TextView)findViewById(R.id.organization_email);
        TextView username = (TextView)findViewById(R.id.username);
        TextView org_username = (TextView)findViewById(R.id.organization_username);
        TextView password = (TextView)findViewById(R.id.password);
        TextView org_password = (TextView)findViewById(R.id.organization_password);

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
        org_name.setTypeface(custom_font);
        email.setTypeface(custom_font);
        org_email.setTypeface(custom_font);
        username.setTypeface(custom_font);
        org_username.setTypeface(custom_font);
        password.setTypeface(custom_font);
        org_password.setTypeface(custom_font);

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
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    switchStatus.setText("ON");
                }else{
                    switchStatus.setText("OFF");
                }

            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setText("ON");
        }
        else {
            switchStatus.setText("OFF");
        }
    }


}



