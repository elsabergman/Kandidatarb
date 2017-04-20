package com.example.android.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;



import static com.example.android.campusapp.R.id.user_buttons;

/**
 * Created by Anna on 2017-04-19.
 */

public class create_student_user extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_user);
        final RadioButton org_button = (RadioButton) findViewById(R.id.organization_button);
        final RadioButton student_button = (RadioButton) findViewById(R.id.student_button);



        Spinner dropdown_uni = (Spinner)findViewById(R.id.uni_spinner);
        String[] items_uni = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter_uni = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_uni);
        dropdown_uni.setAdapter(adapter_uni);

        Spinner dropdown_campus = (Spinner)findViewById(R.id.campus_spinner);
        String[] items_campus = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter_campus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_campus);
        dropdown_campus.setAdapter(adapter_campus);


        student_button.setChecked(true);

        org_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent Intents= new Intent(create_student_user.this, create_org_user.class); // <----- START "BEACHES" ACTIVITY
                startActivity(Intents);
                setContentView(R.layout.create_org_user);

            }
        });


    }
}
