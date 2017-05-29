package com.example.android.campusapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import static com.example.android.campusapp.R.id.custom;
import static com.example.android.campusapp.R.id.spinner1;

/**
 * Created by argr0731 on 2017-04-10.
 */

public class org_campus_information extends SlidingMenuActivity {
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;
    String universityJson = "Change University?";
    String campusJson;
    private PopupWindow mPopupWindow;
    String universityID;
    JSONArray myCampusArray;
    ArrayList<String> nameCampusList;
    ArrayList<String> idCampusList;
    TextView txtaddress;
    TextView txtopening, txtemail,txtphone, textUser;
    String image;
    ImageView iv ;
    String serverUrl = "130.243.182.165";

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


         /*-----------remember token--------------------*/
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/

        /*CALL TO GET PREFERED UNIVERSITY AND CAMPUS */
        Callback myCallback = new Callback();

        try {


            String status = (myCallback.execution_Get("http://"+serverUrl+":8000/profile/", token, "GET", "No JsonData"));


            JSONObject myInfoObject = new JSONObject(status);
            universityJson = myInfoObject.getJSONObject("campus").getString("university_name");
            System.out.println(universityJson + " universityJson");
            first_name = myInfoObject.getString("first_name");
            campusJson = myInfoObject.getJSONObject("campus").getString("campus_name");
            textUser = (TextView) findViewById(R.id.welcome);
            textUser.setText("Hello " + first_name + "!");
            String universities = (myCallback.execution_Get("http://"+serverUrl+":8000/university/", token, "GET", "No JsonData"));
            JSONArray myuniversities = new JSONArray(universities);

            first_name = myInfoObject.getString("first_name");
            textUser = (TextView) findViewById(R.id.welcome);
            textUser.setText("Hello " + first_name + "!");


            for (int i = 0; i < myuniversities.length(); i++) {
                JSONObject json_data = myuniversities.getJSONObject(i);
                String name = json_data.getString("name");
                String id = json_data.getString("id");
                if (universityJson.equals(name)) {
                    universityID = id;
                }

            }
            String all_campuses = (myCallback.execution_Get("http://"+serverUrl+":8000/campus/?university=" + universityID, token, "GET", "No JsonData"));
            myCampusArray = new JSONArray(all_campuses);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        nameCampusList = new ArrayList<String>();
        idCampusList = new ArrayList<String>();


        for (int i = 0; i < myCampusArray.length(); i++) {
            JSONObject json_data = null;
            try {
                json_data = myCampusArray.getJSONObject(i);
                String nameCampus = json_data.getString("name");
                String idCampus = json_data.getString("id");
                nameCampusList.add(i, nameCampus);
                idCampusList.add(i, idCampus);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
             /*add campuses to spinner list, with default campus as the first element */
             boolean resultOfComparison;
            final ArrayList<String> items_campus = new ArrayList<String>();
            items_campus.add(campusJson.toString());
        System.out.println(items_campus);
            for (int k=0; k<nameCampusList.size(); k++) {
                resultOfComparison=nameCampusList.get(k).equals(items_campus.get(0));
                System.out.println(resultOfComparison);
                if(resultOfComparison == false) {
                    items_campus.add(nameCampusList.get(k));
                }

            }
        /*--spinner implementation--*/

            final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
            ArrayAdapter<String> campusadapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, items_campus);
            campusadapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner.setAdapter(campusadapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Här inne är vad som sker när en grej i listan väljs

            /*Toast to show what campus is selected */
                    Toast toast = Toast.makeText(org_campus_information.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    String CAMPUSTEXT = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                    for (int i=0; i<myCampusArray.length(); i++) {
                        if ((CAMPUSTEXT.equals(nameCampusList.get(i)))) {
                            try {
                                JSONObject json_data = myCampusArray.getJSONObject(i);

                                txtaddress=(TextView) findViewById(R.id.the_address);
                                txtopening =(TextView) findViewById(R.id.opening);
                                txtphone =(TextView) findViewById(R.id.phone_number);
                                txtemail =(TextView) findViewById(R.id.email_address);


                                String street_address = json_data.getString("street_address");
                                String postal_code = json_data.getString("postal_code");
                                String city = json_data.getString("city");
                                String phone_number = json_data.getString("phone_number");
                                String email = json_data.getString("email");
                                String opening_hours = json_data.getString("opening_hours");
                                image = json_data.getString("image");


                                txtaddress.setText(street_address + ", " + postal_code + ", " + city);
                                txtopening.setText(opening_hours);
                                txtphone.setText(phone_number);
                                txtemail.setText(email);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
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
                public void onNothingSelected(AdapterView<?> parent) {
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
                    View customView = inflater.inflate(R.layout.popup_file, null);

                    iv = (ImageView) customView.findViewById(R.id.tv);

                    new DownloadImageTask(iv).execute(image); //calls class DownloadImageTask


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
                    if (Build.VERSION.SDK_INT >= 21) {
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
                    mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
                }
            });

        }

    }


    /*CLASS TO DOWNLOAD IMAGE */
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        Bitmap resized = null;
        try {

            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            resized = Bitmap.createScaledBitmap(mIcon11, 470, 900, true);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return resized;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        bmImage.setImageBitmap(result);
    }
}


