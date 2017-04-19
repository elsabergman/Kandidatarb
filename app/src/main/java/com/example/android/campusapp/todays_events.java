package com.example.android.campusapp;

import android.app.Activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static android.R.attr.button;


/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends SlidingMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.todays_events, null);
        drawer.addView(contentView, 0);

 /**   This part controls the spinner with checkboxes for choices,Code from Ironman post on stackexchange Jul 14 2016: http://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox    */
        final String[] select_qualification = {
                "Choose filtering options", "Promoting event", "Lunch event", "Evening event", "Case event",
                "Other"};
        Spinner spinner = (Spinner) findViewById(R.id.filterSpinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(todays_events.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);
    }

    public void onItemSelected (AdapterView< ? > parent, View view, int position, long id) {
        //Här inne är vad som sker när en grej i listan väljs
        Toast toast = Toast.makeText(todays_events.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
        toast.show();    /**Denna toast visar i en liten ruta vilken man valt*/
    }


}
