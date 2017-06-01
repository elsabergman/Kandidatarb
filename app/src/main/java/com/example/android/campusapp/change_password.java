package com.example.android.campusapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by angelicaelvin on 2017-05-16.
 */
/*
Name: change_password.java
Author: Angelica Elvin

This class is used in order for a user to change password.
It is called from settings class.
 */

public class change_password extends AppCompatActivity {

    URLConnection urlConn;
    DataOutputStream printout;
    DataInputStream input;

    String url = "130.238.243.228";

    String group_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);


        /*----- if forgot password button is clicked, redirect to forgot password page --*/
        TextView forgotPwd = (TextView) findViewById(R.id.forgotPwd);

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(change_password.this, forgot_password.class);
                startActivity(intent1);
            }
        });



        /*----- if save password button is clicked,
        save new password and redirect to either student_settings or org_settings page ----*/

        final Button btn = (Button) findViewById(R.id.savePassword);

        btn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                final EditText old_password = (EditText) findViewById(R.id.input_oldPassword);
                String old_pwd = old_password.getText().toString();

                final EditText new_password = (EditText) findViewById(R.id.input_newPassword);
                String pwd = new_password.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object
                try {
                    post_dict.put("new_password", pwd);
                    post_dict.put("old_password", old_pwd);

                    if (post_dict.length() > 0) {


                        Callback myCallback = new Callback();

                        try {


                            String status = (myCallback.execution_Post("http://" + url + ":8000/update-pwd/", token, "PATCH", post_dict.toString()));

                            if (status == "true") {
                                Toast.makeText(change_password.this, "Password was successfully saved!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(change_password.this, login.class);
                                startActivity(intent);
                            }
                            if (status == "false") {
                                Toast.makeText(change_password.this, "Password could not be saved", Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*--- if statement which checks if the user is an organisation or student
                This decides which settings page the user should be ridirected to after changing password.
                 */

                if (group_name.equals("Organisation")){
                    Intent intent1 = new Intent(change_password.this, org_settings.class);
                    startActivity(intent1);
                }
                else{
                    Intent intent1 = new Intent(change_password.this, student_settings.class);
                    startActivity(intent1);
                }

            }
        });

                /* --- GET PROFILE INFORMATION ---- */

                Callback myCallback = new Callback();
                try {
                    String default_options = (myCallback.execution_Get("http://" + url + ":8000/profile/", token, "GET", "No JsonData"));

                    JSONObject myInfoObject = new JSONObject(default_options);
                    group_name = myInfoObject.getString("group_name");


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

}



