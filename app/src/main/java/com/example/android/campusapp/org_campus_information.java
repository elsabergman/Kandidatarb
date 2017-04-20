package com.example.android.campusapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.FOCUS_DOWN;
import static com.example.android.campusapp.R.id.campusesSpinner;
import static com.example.android.campusapp.R.id.parent;
import static com.example.android.campusapp.R.id.spinner1;

/**
 * Created by argr0731 on 2017-04-10.
 */

public class org_campus_information extends SlidingMenuActivity{
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;

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


    // Get the application context
    mContext = getApplicationContext();

    // Get the activity
    mActivity = org_campus_information.this;

    // Get the widgets reference from XML layout
    mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
    mButton = (Button) findViewById(R.id.btn);

    // Set a click listener for the text view
        mButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Initialize a new instance of LayoutInflater service
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.popup_file,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
            // Initialize a new instance of popup window
            mPopupWindow = new PopupWindow(
                    customView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // Set an elevation value for popup window
            // Call requires API level 21
            if(Build.VERSION.SDK_INT>=21){
                mPopupWindow.setElevation(5.0f);
            }

            // Get a reference for the custom view close button
            ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

            // Set a click listener for the popup window close button
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    mPopupWindow.dismiss();
                }
            });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
            // Finally, show the popup window at the center location of root relative layout
            mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
        }
    });

}

    }










