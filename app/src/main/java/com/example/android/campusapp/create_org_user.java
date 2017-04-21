package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anna on 2017-04-19.
 */

public class create_org_user extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_org_user);



        Spinner dropdown_uni = (Spinner)findViewById(R.id.uni_spinner);
        String[] items_uni = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter_uni = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_uni);
        dropdown_uni.setAdapter(adapter_uni);

        Spinner dropdown_campus = (Spinner)findViewById(R.id.campus_spinner);
        String[] items_campus = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter_campus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_campus);
        dropdown_campus.setAdapter(adapter_campus);


    create_org_user();

    }

    void create_org_user(){
         /*Input fields for creating a user*/
        final EditText orgname_Edit = (EditText) findViewById(R.id.input_orgname);
        final EditText firstname_Edit = (EditText) findViewById(R.id.input_firstname);
        final EditText lastname_Edit = (EditText) findViewById(R.id.input_lastname);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);

        final EditText username_Edit = (EditText) findViewById(R.id.input_username);
        final EditText password_Edit = (EditText) findViewById(R.id.input_password);

        //  final EditText phone = (EditText) findViewById(R.id.phone);
        final TextView txtViewNotComplete = (TextView) findViewById(R.id.wrongInputUser);



        Button button = (Button) findViewById(R.id.createUserBtn);

        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String orgname = orgname_Edit.getText().toString();
                String firstname = firstname_Edit.getText().toString();
                String lastname = lastname_Edit.getText().toString();
                String email = email_Edit.getText().toString();
                String email2 = email_Edit.getText().toString();
                String username = username_Edit.getText().toString();
                String password = password_Edit.getText().toString();
                // String phone = phone.getText().toString();




                JSONObject post_dict = new JSONObject();

                try {
                    post_dict.put("org_name" , orgname);
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

                    new SendToDatabase().execute(post_dict.toString(), "http://212.25.154.105:8000/users/register/");

                }


                // txtViewNotComplete.setText("fill in all fields");

                Toast.makeText(create_org_user.this, "User sucessfully created", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(create_org_user.this, login.class);
                startActivity(intent);



            }
        });
    }
}