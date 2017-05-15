package com.example.android.campusapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class login extends Activity {


    URLConnection urlConn;
    DataOutputStream printout;
    DataInputStream input;

    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        /*---Fonts for our Logo---*/
        TextView header = (TextView) findViewById(R.id.title);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);
        TextView at = (TextView) findViewById(R.id.at);
        Typeface custom_font2 = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        at.setTypeface(custom_font2);
        /*--------------------------*/

        loginFunctions(); //Handles all login code

        /*----if create user button is clicked, redirect to create user page --*/
        TextView createUser = (TextView) findViewById(R.id.createUser);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, create_user.class);
                startActivity(intent);
            }
        });
    }

    /*---handles all login function --*/
    void loginFunctions() {
        Button btn = (Button) findViewById(R.id.loginButton);
        TextView createUser = (TextView) findViewById(R.id.createUser);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);
        final EditText pwd_Edit = (EditText) findViewById(R.id.input_pwd);


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String email = email_Edit.getText().toString(); //saves email input from user

                String pwd = pwd_Edit.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object


                try {
                    post_dict.put("username", email);
                    post_dict.put("password", pwd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {
                    new GetTokenLogin(login.this).execute(post_dict.toString(), "http://130.243.199.160:8000/auth/token/");



                }
            }
        });


    }

    /*---This function is called from GetTokenLogin and holds information about whether access to login was granted or not---*/

    void LoginAccessGranted(String message, String got_token, String got_group) {
        final TextView wrongLogin = (TextView) findViewById(R.id.wrongInput);


        if (message.equals("error")) {
            wrongLogin.setText("Username and/or password is incorrect");
        }
        if (message.equals("access granted")) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("token", got_token).commit();


            if(got_group.equals("Organisation")) {


                Intent intent = new Intent(login.this, org_my_events.class);    //ÄNDRA TILL ORG_MY_EVENT.CLASS
                startActivity(intent);
            }
            else{


              Intent intent = new Intent(login.this, todays_events.class);
                startActivity(intent);

            }




        }


    }
}













