package com.example.android.campusapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by angelicaelvin on 2017-05-16.
 */

public class forgot_password extends AppCompatActivity {

    String url = "212.25.151.161";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        final Button sendEmail_button = (Button) findViewById(R.id.sendEmail);

        sendEmail_button.setOnClickListener(new View.OnClickListener() {


            /*---- Function when saved password button is clicked ----*/
            public void onClick(View v) {

                final EditText email= (EditText) findViewById(R.id.input_email);
                String email_string = email.getText().toString(); //saves email input from user to be able to send a new password i back-end

                JSONObject post_dict = new JSONObject(); //creates Json object


                try {
                    post_dict.put("email", email_string);
                    if (post_dict.length() > 0) {


                        Callback myCallback = new Callback();

                        try {


                            String status = (myCallback.execution_Post("http://"+url+":8000/reset-pwd/", "0","PATCH",post_dict.toString()));

                            if (status == "true") {
                                Toast.makeText(forgot_password.this, "Email sent successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(forgot_password.this, login.class);
                                startActivity(intent);
                            }if(status == "false"){
                                Toast.makeText(forgot_password.this, "email could not be sent", Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }
}
