package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.Constants.DESCRIPTION;
import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;
import static com.example.android.campusapp.Constants.URL;


/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends student_SlidingMenuActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> total_list;

    MaterialBetterSpinner materialBetterSpinner ;

    String[] SPINNER_DATA = { "Type of Event:", "Promoting event", "Lunch event", "Evening event", "Case event", "Campus:", "Ångström", "Engelska Parken"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.todays_events, null);
        drawer.addView(contentView, 0);

        /*-----------remember token--------------------*/
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        System.out.println(token);

        /*----------------------------------------------*/

             /*---Fonts for our Logo---*/
        TextView header = (TextView) findViewById(R.id.todays_events);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        header.setTypeface(custom_font);
        /*--------------------------*/






        Callback myCallback = new Callback();

        try { String status = (myCallback.execution_Get("http://130.243.201.128:8000/events/", token, "GET", "No JsonData"));


            if (status == "false"){
                Toast.makeText(todays_events.this, "could not fetch events", Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray myEventsArray = new JSONArray(status);


                ListView listView = (ListView) findViewById(R.id.todays_events_list);

                /*list = the list that will store all hashMaps
                hashMap = stores all information about a specific event
                total_list = the list that will be displayed
                 */

                /* --- create hash map that all Json objects are inserted to --- */
                list=new ArrayList<HashMap<String,String>>();
                total_list=new ArrayList<HashMap<String,String>>();
                ListViewAdapter adapter;

                /*create as many hash maps as needed */
                for(int i = 0; i < myEventsArray.length(); i++) {
                    list.add(new HashMap<String, String>());
                }

                for (int i = 0; i < myEventsArray.length(); i++) {
                    JSONObject json_data = myEventsArray.getJSONObject(i);
                    String date = json_data.getString("date");
                    String name = json_data.getString("name_event");
                    String start_time = json_data.getString("start_time");
                    String end_time = json_data.getString("stop_time");
                    String owner = json_data.getString("owner");
                    String description = json_data.getString("description");
                    String url = json_data.getString("external_url");
                    //    String id =json_data.getString("id");
                    list.get(i).put(FIRST_COLUMN, date);
                    list.get(i).put(SECOND_COLUMN,start_time + "- " +end_time );

                    list.get(i).put(THIRD_COLUMN,name);
                  //  list.get(i).put(FOURTH_COLUMN, name );
                    list.get(i).put(DESCRIPTION, description);
                    total_list.add(list.get(i));
                    if ( url != null) {

                        list.get(i).put(URL, url);
                    }

                    else {

                        list.get(i).put(URL, " ");
                    }

                    Log.d(name, "name");
                    Log.d(date, "date");
                    Log.d(start_time,"start");
                    Log.d(end_time, "end");
                    Log.d(description,"description");
                    // Log.d(id, "id");


                }

                adapter=new ListViewAdapter(this, list, listView);
                listView.setAdapter(adapter);






            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(todays_events.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

       // materialBetterSpinner.setAdapter(adapter1);

        ArrayList<todays_events_spinner_StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < SPINNER_DATA.length; i++) {
            todays_events_spinner_StateVO todayseventsspinnerStateVO = new todays_events_spinner_StateVO();
            todayseventsspinnerStateVO.setTitle(SPINNER_DATA[i]);
            todayseventsspinnerStateVO.setSelected(false);
            listVOs.add(todayseventsspinnerStateVO);
        }
        todays_events_spinner_MyAdapter todayseventsspinnerMyAdapter = new todays_events_spinner_MyAdapter(todays_events.this, 0,
                listVOs);
        materialBetterSpinner.setAdapter(todayseventsspinnerMyAdapter); }




        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Här inne är vad som sker när en grej i listan väljs
            Toast toast = Toast.makeText(todays_events.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
            toast.show();
            /**Denna toast visar i en liten ruta vilken man valt*/
        }

        /**   This part controls the spinner with checkboxes for choices,Code from Ironman post on stackexchange Jul 14 2016: http://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox    */
     /*   final String[] select_qualification = {
                "Choose to filter by...", "Promoting event", "Lunch event", "Evening event", "Case event",
                "Other"};
        Spinner spinner = (Spinner) findViewById(R.id.filterSpinner);

        ArrayList<todays_events_spinner_StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            todays_events_spinner_StateVO todayseventsspinnerStateVO = new todays_events_spinner_StateVO();
            todayseventsspinnerStateVO.setTitle(select_qualification[i]);
            todayseventsspinnerStateVO.setSelected(false);
            listVOs.add(todayseventsspinnerStateVO);
        }
        todays_events_spinner_MyAdapter todayseventsspinnerMyAdapter = new todays_events_spinner_MyAdapter(todays_events.this, 0,
                listVOs);
        spinner.setAdapter(todayseventsspinnerMyAdapter);


    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Här inne är vad som sker när en grej i listan väljs
        Toast toast = Toast.makeText(todays_events.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
        toast.show();    /**Denna toast visar i en liten ruta vilken man valt*/
  //  }






}