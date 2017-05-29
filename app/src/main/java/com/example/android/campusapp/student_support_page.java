package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.campusapp.R.id.supportSpinner;

/**
 * Created by Anna on 2017-05-08.
 *
 * This class is connected to the student_support_page.xml file. This file lets the user fill out a couple of fields that later creates a ready to send email opened in the users email application.
 *
 */

public class student_support_page extends student_SlidingMenuActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_support_page, null);

        drawer.addView(contentView, 0);

        //This line prevents keyboard to pop up in screens with edittexts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Button button = (Button) findViewById(R.id.send_support_button);

       /*Input fields for filling out user details*/
        final EditText organizationName_Edit = (EditText) findViewById(R.id.input_add_organization);
        final EditText emailAddress_Edit = (EditText) findViewById(R.id.input_email_address);
        final EditText phoneNumber_Edit = (EditText) findViewById(R.id.input_phone_number);
        final EditText messageText_Edit = (EditText) findViewById(R.id.support_text_field);

        //Spinner with what kind of errand the user wants support with
        final Spinner spinner = (Spinner) findViewById(supportSpinner);

        //Text if wrong input
        final TextView notCompleteFilledForm = (TextView) findViewById(R.id.inputSupportWrong);

        //What happens when the user clicks the send_support_button.
        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String organizationname = organizationName_Edit.getText().toString();
                String emailaddress = emailAddress_Edit.getText().toString();
                String phonenumber = phoneNumber_Edit.getText().toString();
                String messagetext = messageText_Edit.getText().toString();
                String spinnertext = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                String adminemail = "info.atcampus@gmail.com";


                if ((organizationname.equals("")) || (emailaddress.equals("")) || (phonenumber.equals("")) || (messagetext.equals(""))) {
                    //If any necessary field not filled in
                    notCompleteFilledForm.setText("Fill in all fields");

                } else {

                    Toast.makeText(student_support_page.this, "Please choose your preferred email client", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(student_support_page.this, student_support_page.class);
                    startActivity(intent);

                    //Create email with user input
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{adminemail});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Support Errand Regarding: "+spinnertext+" issues from "+organizationname);
                    i.putExtra(Intent.EXTRA_TEXT   , messagetext+"\n \n From: "+organizationname+" \nemail: "+emailaddress+" \nphone: "+phonenumber);
                    try {
                        //Let the user choose email app and open in there
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        //If no email apps installed
                        Toast.makeText(student_support_page.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

    }
}
