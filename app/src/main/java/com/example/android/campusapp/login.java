package com.example.android.campusapp;

/* Name: login.java
Author: Elsa Bergman
Connects to: GetTokenLogin.java

This class handles log in functionality and connects to GetTokenLogin which connects to the database and gives the user that wants to
log in a token that the user can use to access all pages in the system. By connecting to GetTokenLogin, this class will also know
if the user who wants to log in is a student user or an organization user.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;



public class login extends Activity {


    /* instance variables */
    DataInputStream input;
    String serverURL = "212.25.151.161";


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

        loginFunctions(); // method with all code concerning the actual log in functionality.

        /*----- if forgot password button is clicked, redirect to forgot password page --*/
        TextView forgotPwd = (TextView) findViewById(R.id.forgotPwd);
        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(login.this, forgot_password.class);
                startActivity(intent1);
            }
        });

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

    /*---handles all functionality concerning the user log in --*/

    void loginFunctions() {
        Button btn = (Button) findViewById(R.id.loginButton);

        /* the two input fields that the user needs to fill in when logging in */
        final EditText username_Edit = (EditText) findViewById(R.id.input_email);
        final EditText pwd_Edit = (EditText) findViewById(R.id.input_pwd);

        /* when clicking on log in button a connection to the database will be implemented */
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String email = username_Edit.getText().toString(); //saves username input from user

                String pwd = pwd_Edit.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object


                /* adds username and password to json object */
                try {
                    post_dict.put("username", email);
                    post_dict.put("password", pwd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    /* calls GetTokenLogin.java which will handle the acutal connection to the database */
                    new GetTokenLogin(login.this).execute(post_dict.toString(), "http://"+serverURL+":8000/auth/token/");

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
        /* saves the token received from the database if access is granted to PreferenceManager which allows the token to be fetched
         * from every page as it is needed for every request that is made to the database  */
        if (message.equals("access granted")) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("token", got_token).commit();

            /* checks if the user is an organisation or a student and redirects the user to the correct page */

            if(got_group.equals("Organisation")) {


                Intent intent = new Intent(login.this, org_my_events.class);
                startActivity(intent);
            }
            else{


              Intent intent = new Intent(login.this, todays_events.class);
                startActivity(intent);

            }




        }


    }
}













