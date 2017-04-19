package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elsabergman on 2017-03-31.
 */

public class create_user extends FragmentActivity {

    FragmentTransaction ft;
    Fragment           fragment_student;
    Fragment           fragment_org;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_user);

        Button button = (Button) findViewById(R.id.createUserBtn);

        fragment_student = new fragment_student_info();
        fragment_org = new fragment_org_info();

        RadioButton student_button = (RadioButton) findViewById(R.id.student_button);
        RadioButton org_button = (RadioButton) findViewById(R.id.organization_button);
        student_button.setChecked(true);


        /*Input fields for creating a user*/

        final EditText firstname_Edit = (EditText) findViewById(R.id.input_firstname);
        final EditText lastname_Edit = (EditText) findViewById(R.id.input_lastname);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);

        final EditText username_Edit = (EditText) findViewById(R.id.input_username);
        final EditText password_Edit = (EditText) findViewById(R.id.input_password);
      //  final EditText phone = (EditText) findViewById(R.id.phone);
        final TextView txtViewNotComplete = (TextView) findViewById(R.id.wrongInputUser);




       button.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                String firstname = firstname_Edit.getText().toString();
                String lastname = lastname_Edit.getText().toString();
                String email = email_Edit.getText().toString();
                String email2 = email_Edit.getText().toString();
                String username = username_Edit.getText().toString();
                String password = password_Edit.getText().toString();
               // String phone = phone.getText().toString();

                JSONObject post_dict = new JSONObject();

                try {
                    post_dict.put("username" , username);
                    post_dict.put("password", password);
                    post_dict.put("email", email);
                    post_dict.put("email2", email2);
                    post_dict.put("first_name", firstname);
                    post_dict.put("last_name", lastname);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    new SendToDatabase().execute(post_dict.toString(), "http://130.243.198.5:8000/users/register/");

                }



                   // txtViewNotComplete.setText("fill in all fields");

                    Toast.makeText(create_user.this, "User sucessfully created", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(create_user.this, login.class);
                    startActivity(intent);



            }
        });


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment_student).commit();

        // set listener
        ((RadioGroup) findViewById(R.id.user_buttons)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ft = getSupportFragmentManager().beginTransaction();
                switch (checkedId) {
                    case R.id.student_button:
                        ft.replace(R.id.fragment_container, fragment_student);
                        break;
                    case R.id.organization_button:
                        ft.replace(R.id.fragment_container, fragment_org);
                        break;
                }
                ft.commit();
            }
        });


    }
}
