package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.password;
import static android.R.attr.phoneNumber;

import static com.example.android.campusapp.R.id.supportSpinner;

/**
 * Created by argr0731 on 2017-04-10.
 */

public class org_support_page extends SlidingMenuActivity{
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_support_page, null);

        drawer.addView(contentView, 0);

        //This line prevents keyboard to pop up in screens with edittexts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Button button = (Button) findViewById(R.id.send_support_button);

        /*Input fields for creating a user*/

        final EditText organizationName_Edit = (EditText) findViewById(R.id.input_add_organization);
        final EditText emailAddress_Edit = (EditText) findViewById(R.id.input_email_address);
        final EditText phoneNumber_Edit = (EditText) findViewById(R.id.input_phone_number);
        final EditText messageText_Edit = (EditText) findViewById(R.id.support_text_field);


        final Spinner spinner = (Spinner) findViewById(supportSpinner);

        final TextView notCompleteFilledForm = (TextView) findViewById(R.id.inputSupportWrong);

        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                String organizationname = organizationName_Edit.getText().toString();
                String emailaddress = emailAddress_Edit.getText().toString();
                String phonenumber = phoneNumber_Edit.getText().toString();
                String messagetext = messageText_Edit.getText().toString();
                String spinnertext = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                String adminemail = "arvid.grans.0731@student.uu.se";





                if ((organizationname.equals("")) || (emailaddress.equals("")) || (phonenumber.equals("")) || (messagetext.equals(""))) {

                    notCompleteFilledForm.setText("Fill in all fields");

                } else {

                    Toast.makeText(org_support_page.this, "Please choose your preferred email client", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(org_support_page.this, org_support_page.class);
                    startActivity(intent);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{adminemail});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Support Errand Regarding: "+spinnertext+" from "+organizationname);
                    i.putExtra(Intent.EXTRA_TEXT   , messagetext+"\n \n From: "+organizationname+" \nContact details,"+" email: "+emailaddress+", phone: "+phonenumber);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(org_support_page.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });

    }
}

