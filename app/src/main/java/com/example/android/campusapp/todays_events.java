package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by elsabergman on 2017-03-31.
 */
public class todays_events extends student_SlidingMenuActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;


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
    }






}