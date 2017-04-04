package com.example.android.campusapp;


import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLOutput;

import static com.example.android.campusapp.R.layout.activity_login;


public class login extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        /*login click button */
        Button btn = (Button) findViewById(R.id.loginButton);
        TextView createUser = (TextView) findViewById(R.id.createUser);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);
        final EditText pwd_Edit = (EditText) findViewById(R.id.input_pwd);
        final TextView txtView = (TextView) findViewById(R.id.wrongInput);


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = email_Edit.getText().toString();

                String pwd = pwd_Edit.getText().toString();
                System.out.println(email);


                if (("kandidat".equals(email)) || ("kand123".equals(pwd))) {
                    setContentView(R.layout.todays_events);
                } else {
                    txtView.setText("wrong password and/or email address");

                }


            }

        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.create_user);
            }
        });


    }
}
   // }
//}






