package com.example.android.campusapp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by argr0731 on 2017-04-10.
 */

public class org_campus_information extends org_SlidingMenuActivity {
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
        /*--navigation drawer menu --*/
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.org_campus_information, null);
        drawer.addView(contentView, 0);

        /*--spinner implementation--*/

        final Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        String[] items_uni = new String[]{"Ångströmslaboratioriet", "Info.teknologiskt centrum"};
        ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_uni);
        campusadapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(campusadapter);
        /*final Spinner spinner = (Spinner) findViewById(spinner1);

        Spinner campuses = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> campusadapter = ArrayAdapter.createFromResource(
                this, R.array.campuses_array, R.layout.spinner_layout);


        campuses.setAdapter(campusadapter);*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

    {
        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        //Här inne är vad som sker när en grej i listan väljs

            /*Toast to show what campus is selected */
            Toast toast = Toast.makeText(org_campus_information.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
            toast.show();
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

     /*--------Start of Popup-design-------- */
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

            final RelativeLayout back_dim_layout = (RelativeLayout) findViewById(R.id.background_popup);
            back_dim_layout.setVisibility(View.VISIBLE); /*set faded background */

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
                    back_dim_layout.setVisibility(View.GONE); /*remove faded background*/
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










