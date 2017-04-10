package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import static android.R.attr.name;


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

            public void onClick(View v) {

                String email = email_Edit.getText().toString();

                String pwd = pwd_Edit.getText().toString();



                if (("kandidat".equals(email)) && ("kand123".equals(pwd))) {
                    Intent intent = new Intent(login.this, SlidingMenuActivity.class);
                    startActivity(intent);


                } else {
                    txtView.setText("wrong password and/or email address");

                }


            }

        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, todays_events.class);
                startActivity(intent);
            }
        });


    }
}
// }
//}










