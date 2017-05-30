package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.android.volley.Request.Method.HEAD;
import static com.android.volley.Request.Method.POST;


/** This page have one function, to create a student user. It has several fields which the user has to fill in and
 * then two spinners where the user can choose university and campus. Depending on which university the user chooses, it will
 * change the campuses in the spinners as well.
 * Created by Anna on 2017-04-19.
 */

public class create_student_user extends AppCompatActivity {

    JSONArray myUniArray;
    ArrayList<String> nameList;
    ArrayList<String> idList;
    String chosen_uni;
    String chosen_campus;
    String theId;
    ArrayList<String> nameCampusList;
    ArrayList<String> idCampusList;
    JSONArray myCampusArray;
    String theIdCampus;

    String url = "212.25.151.161";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_user);

        Callback myCallback = new Callback();
        try {
            //gets information from database
            String status = (myCallback.execution_Get("http://" + url + ":8000/university/", "0", "GET", "No JsonData"));
            myUniArray = new JSONArray(status);
            nameList = new ArrayList<String>();
            idList = new ArrayList<String>();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //adds name and id to separate lists
        for (int i = 0; i < myUniArray.length(); i++) {
            JSONObject json_data = null;
            try {
                json_data = myUniArray.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                nameList.add(i, name);
                idList.add(i, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayList<String> items_uni = new ArrayList<String>();

        for (int i = 0; i < nameList.size(); i++) {
            items_uni.add(nameList.get(i));
        }

        //connects spinner to the id in XML
        final Spinner dropdown_uni = (Spinner) findViewById(R.id.uni_spinner);
        ArrayAdapter<String> adapter_uni = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_uni);
        dropdown_uni.setAdapter(adapter_uni);


        dropdown_uni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//choosing item in spiiner
                chosen_uni = dropdown_uni.getItemAtPosition(dropdown_uni.getSelectedItemPosition()).toString();


                    for (int i = 0; i < myUniArray.length(); i++) {
                          /* if the chosen uni equals the uni in place i+1 (add 1 because first place is "Choose Uni...") */
                        if (chosen_uni == items_uni.get(i) ){
                            theId = idList.get(i);

                            ChooseMyCampus(theId); //Call choose campus with the chosen university


                        }
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

        void ChooseMyCampus(String theID) {


            Callback myCallback = new Callback();
            try {
//gets info from database
                String all_campuses = (myCallback.execution_Get("http://" + url + ":8000/campus/?university=" + theId, "0", "GET", "No JsonData"));

                myCampusArray = new JSONArray(all_campuses);
                nameCampusList = new ArrayList<String>();
                idCampusList = new ArrayList<String>();

//adds name and is into separate lists
                for (int i = 0; i < myCampusArray.length(); i++) {
                    JSONObject json_data = myCampusArray.getJSONObject(i);
                    String nameCampus = json_data.getString("name");
                    String idCampus = json_data.getString("id");
                    nameCampusList.add(i, nameCampus);
                    idCampusList.add(i, idCampus);


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            final Spinner dropdown_campus = (Spinner) findViewById(R.id.campus_spinner);
            final ArrayList<String> items_campus = new ArrayList<String>();
            for (int i = 0; i < nameCampusList.size(); i++) {
                items_campus.add(nameCampusList.get(i));
            }

        ArrayAdapter<String> adapter_campus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_campus);
        dropdown_campus.setAdapter(adapter_campus);

            dropdown_campus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

            {
                @Override
                public void onItemSelected (AdapterView < ? > parent, View view,int position, long id) {
                // choosing item in spinner
                    chosen_campus = dropdown_campus.getItemAtPosition(dropdown_campus.getSelectedItemPosition()).toString();


                    if (chosen_campus != "Choose Campus...") {

                        for (int i = 0; i < myCampusArray.length(); i++) {

                        /* if the chosen campus equals the campus in place i+1 (add 1 because first place is "Choose Campus...") */
                            if (chosen_campus == items_campus.get(i)) //Den kallar på ChooseRoom två gånger! fixa detta!
                            {
                                theIdCampus = idCampusList.get(i);
                                create_student_user(theIdCampus);


                            }
                        }
                    }

                }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){
                }


            });


    }


    void create_student_user(final String idCampus) {

         /*Input fields for creating a user*/
        final EditText firstname_Edit = (EditText) findViewById(R.id.input_firstname);
        final EditText lastname_Edit = (EditText) findViewById(R.id.input_lastname);
        final EditText email_Edit = (EditText) findViewById(R.id.input_email);
        final EditText username_Edit = (EditText) findViewById(R.id.input_username);
        final EditText password_Edit = (EditText) findViewById(R.id.input_password);


        Button button = (Button) findViewById(R.id.createUserBtn);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String firstname = firstname_Edit.getText().toString();
                String lastname = lastname_Edit.getText().toString();
                String email = email_Edit.getText().toString();
                String email2 = email_Edit.getText().toString();
                String username = username_Edit.getText().toString();
                String password = password_Edit.getText().toString();

                JSONObject post_dict = new JSONObject();

                try {
                    post_dict.put("username", username);
                    post_dict.put("password", password);
                    post_dict.put("email", email);
                    post_dict.put("email2", email2);
                    post_dict.put("first_name", firstname);
                    post_dict.put("last_name", lastname);
                    post_dict.put("groups","Student");
                    post_dict.put("campus", idCampus);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    Callback myCallback = new Callback();

                    try {
                    //posting post_dict to database
                        String status = (myCallback.execution_Post("http://"+url+":8000/register/", "0" , "POST",post_dict.toString()));

                        if (status == "true") {
                            Intent intent = new Intent(create_student_user.this, login.class);
                            startActivity(intent);
                            Toast.makeText(create_student_user.this, "User sucessfully created", Toast.LENGTH_LONG).show();
                        }if (status =="false") {
                            Toast.makeText(create_student_user.this, "User could not be created", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e) {
                        System.out.println("Could not create user");
                    }

                }
            }
        });
    }
}