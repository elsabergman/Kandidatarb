package com.example.android.campusapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class create_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user2);
        final RadioButton student_button = (RadioButton) findViewById(R.id.student_button);
        final RadioButton org_button = (RadioButton) findViewById(R.id.organization_button);


        student_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (student_button.isChecked()) {
                    Intent Intents= new Intent(create_user.this, create_student_user.class);
                    startActivity(Intents);
                    setContentView(R.layout.create_student_user);
                }

            }
        });

        org_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (org_button.isChecked()) {
                    Intent Intents= new Intent(create_user.this, create_org_user.class);
                    startActivity(Intents);
                    setContentView(R.layout.create_org_user);
                }

            }
        });

    }
}
