package com.example.android.campusapp;
/* Creates the page between the login-page and the create user-page. At this page, the user only chooses which kind of user
 * he or she wants to be. So this code has only two click listeners, one which gets the user to the create student-page
   * and one that gets the user to the create org-page. These buttons are connected to the XML-file's buttons*/
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class create_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        final Button student_button = (Button) findViewById(R.id.create_stud_button);
        final Button org_button = (Button) findViewById(R.id.create_organization_button);

        student_button.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
           Intent Intents = new Intent(create_user.this, create_student_user.class);
           startActivity(Intents);
            setContentView(R.layout.create_student_user);
                 }

            });

            org_button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent Intents = new Intent(create_user.this, create_org_user.class);
                    startActivity(Intents);
                    setContentView(R.layout.create_org_user);
                }
            });
    }
}




