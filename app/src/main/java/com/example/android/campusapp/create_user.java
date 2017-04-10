package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by elsabergman on 2017-03-31.
 */

public class create_user  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_user);

        Button button = (Button) findViewById(R.id.createUserButton);
        /*Input fields for creating a user*/
        final EditText firstname_Edit = (EditText) findViewById(R.id.input_firstname);
        final EditText lastname_Edit = (EditText) findViewById(R.id.input_lastname);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);
        final EditText phone_Edit = (EditText) findViewById(R.id.input_phone);
        final EditText username_Edit = (EditText) findViewById(R.id.input_username);
        final EditText password_Edit = (EditText) findViewById(R.id.input_password);

        final TextView txtViewNotComplete = (TextView) findViewById(R.id.wrongInputUser);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String firstname = firstname_Edit.getText().toString();

                String lastname = lastname_Edit.getText().toString();

                String email = email_Edit.getText().toString();

                String phone = phone_Edit.getText().toString();

                String username = username_Edit.getText().toString();

                String password = password_Edit.getText().toString();

                if ((firstname.equals("")) || (lastname.equals("")) || (email.equals(""))
                        || (phone.equals("")) || (username.equals("")) || (password.equals(""))) {

                    txtViewNotComplete.setText("fill in all fields");

            } else {

                    Toast.makeText(create_user.this,"User sucessfully created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(create_user.this, login.class);
                    startActivity(intent);


            }


        }



    });
    }
}

