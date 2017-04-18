package com.example.android.campusapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.attr.name;


public class login extends Activity {


    URLConnection urlConn;
    DataOutputStream printout;
    DataInputStream input;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        TextView header = (TextView)findViewById(R.id.title);




        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/Shrikhand-Regular.ttf");

        header.setTypeface(custom_font);

        TextView at = (TextView)findViewById(R.id.at);

        Typeface custom_font2 = Typeface.createFromAsset(this.getAssets(),  "fonts/Shrikhand-Regular.ttf");

        at.setTypeface(custom_font2);

        loginFunctions();
        TextView createUser = (TextView) findViewById(R.id.createUser);


        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, create_user.class);
                startActivity(intent);
            }
        });
    }


    void loginFunctions() {
        Button btn = (Button) findViewById(R.id.loginButton);
        TextView createUser = (TextView) findViewById(R.id.createUser);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);
        final EditText pwd_Edit = (EditText) findViewById(R.id.input_pwd);
        final TextView txtView = (TextView) findViewById(R.id.wrongInput);


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String email = email_Edit.getText().toString();

                String pwd = pwd_Edit.getText().toString();

                JSONObject post_dict = new JSONObject();


                try {
                    post_dict.put("username" , email);
                    post_dict.put("password", pwd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    new SendToDatabase().execute( post_dict.toString(), "http://212.25.151.17:8000/auth/token/");

                }




                Intent intent = new Intent(login.this, org_my_events.class);
                startActivity(intent);

            }
        });
    }


}











