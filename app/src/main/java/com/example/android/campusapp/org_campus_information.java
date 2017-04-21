package com.example.android.campusapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.FOCUS_DOWN;
import static com.example.android.campusapp.R.id.parent;
import static com.example.android.campusapp.R.id.spinner1;

/**
 * Created by argr0731 on 2017-04-10.
 */

public class org_campus_information extends SlidingMenuActivity{


    /**
     * Here we control the spinner located in campus_information.xml for different campuses
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_campus_information, null);

        drawer.addView(contentView, 0);

        final Spinner spinner = (Spinner) findViewById(spinner1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

    {
        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        //Här inne är vad som sker när en grej i listan väljs

            Toast toast = Toast.makeText(org_campus_information.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
            toast.show();    /**Denna toast visar i en liten ruta vilken man valt*/
            String CAMPUSTEXT = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

            if ((CAMPUSTEXT.equals("Informationsteknologiskt centrum"))) {


        }
        if ((CAMPUSTEXT.equals("Ångströmslaboratoriet"))) {

        }


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

    ArrayAdapter<CharSequence> adapter;


        /** @Override
        protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_information);


        spinner  = (Spinner) findViewById(R.id.campusesSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.camuses_array, android.r.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView<?> parent, View, int positive);
         @Override
         public void onItemSelected(AdapterView<?> parent, View, int positive)
         Toast.makeText(getBaseContext(), parent.getItemAtPositition(position)+" selected" , Toast.LENGTH_LONG).show();*/



    }


}








