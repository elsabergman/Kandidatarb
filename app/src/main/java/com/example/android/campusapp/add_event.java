package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Anna on 2017-04-04.
 */

public class add_event extends SlidingMenuActivity{
    String chosen_campus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.add_event, null);

        drawer.addView(contentView, 0);

        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);




        Button btn = (Button) findViewById(R.id.create_event_button);
        final EditText event_name = (EditText) findViewById(R.id.input_add_event);
        final EditText company_name = (EditText) findViewById(R.id.input_company_name);
        final EditText relevant_links = (EditText) findViewById(R.id.input_links);
        final EditText description = (EditText) findViewById(R.id.eventDescription);
        final EditText date = (EditText) findViewById(R.id.date);
        final EditText starttime = (EditText) findViewById(R.id.start_time);
        final EditText stoptime = (EditText) findViewById(R.id.end_time);


         /*--spinner implementation--*/

        final Spinner spinner = (Spinner)findViewById(R.id.choose_campus);
        String[] items_uni = new String[]{"1", "Info.teknologiskt centrum"};
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
                //Här inne är vad som sker när en grej i listan väljs

                chosen_campus = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();




                /**  @Override public void onAttach(Activity context) {
                super.onAttach(context);


                }


                /**  public interface OnFragmentInteractionListener {
                // TODO: Update argument type and name
                void onFragmentInteraction(Uri uri);
                }

                 */


            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){
            }


        });


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String eventname = event_name.getText().toString(); //saves email input from user

                String companyname = company_name.getText().toString(); //saves password input from user

                String relevantlinks = relevant_links.getText().toString(); //saves password input from user

                String eventdescription = description.getText().toString(); //saves password input from user

                String dateEvent = date.getText().toString(); //saves password input from user
                String start_time = starttime.getText().toString(); //saves password input from user
                String stop_time = stoptime.getText().toString(); //saves password input from user

                JSONObject post_dict = new JSONObject(); //creates Json object


                try {
                    post_dict.put("type_event", "Lunch Lecture");
                    post_dict.put("name_event", eventname);
                    post_dict.put("description", eventdescription);
                    post_dict.put("date",dateEvent);
                    post_dict.put("start_time", start_time);
                    post_dict.put("stop_time", stop_time);
                    post_dict.put("external_url", relevantlinks);
                    post_dict.put("campus_location", chosen_campus);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (post_dict.length() > 0) {

                    /*connect to GetTokenLogin, which handles connection to Database */
                    new SendToDatabase().execute(post_dict.toString(), "http://212.25.154.105:8000/events/",token);

                }
            }
        });




    }


        }


