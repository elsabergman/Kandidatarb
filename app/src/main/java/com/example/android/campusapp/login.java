package com.example.android.campusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLOutput;

import static com.example.android.campusapp.R.layout.activity_login;

public class login extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*login click button */
        Button btn = (Button) findViewById(R.id.loginButton);
        final EditText email_Edit   = (EditText)findViewById(R.id.input_email);
        final EditText pwd_Edit   = (EditText)findViewById(R.id.input_pwd);
        final TextView txtView = (TextView)findViewById(R.id.wrongInput);


        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {

               String email = email_Edit.getText().toString();

                String pwd = pwd_Edit.getText().toString();
                System.out.println( email);


                if (("kandidat".equals(email))  || ("kand123".equals(pwd))) {
                    setContentView(R.layout.todays_events);
                }
                else {
                    txtView.setText("wrong password and/or email address");

                }

            }

        });
    }



    }
   // }
//}






