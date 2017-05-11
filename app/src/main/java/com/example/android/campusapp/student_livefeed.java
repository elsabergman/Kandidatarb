package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;




/**
 * Created by Anna on 2017-04-24.
 */

public class student_livefeed extends student_SlidingMenuActivity {

    private Context mContext;
    private Activity mActivity;

    private LinearLayout mLinearLayout;
    private FloatingActionButton fab;

    private PopupWindow mPopupWindow;
    String token;
    String status;
    private ArrayList<String> listContent;
    private ArrayList<String> listLocation;
    private ArrayList<String> listCount;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*--navigation drawer menu --*/
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_livefeed, null);
        drawer.addView(contentView, 0);

    /*-----------remember token--------------------*/
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
    /*----------------------------------------------*/


        createFeed();
        createPost();

    }


    void createFeed() {


        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
          ImageButton arrow_up;
         ImageButton arrow_down;

        //Hämtar alla inlägg från databasen och lägger de i list
        Callback myCallback = new Callback();

        try {
            status = (myCallback.execution_Get("http://130.243.201.128:8000/messages/", token, "GET", "No JsonData"));
            System.out.println(status);
            if (status == "false") {
                Toast.makeText(student_livefeed.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
            } else {

                JSONArray feedArrayDescription = new JSONArray(status);
                JSONArray feedArrayLocation = new JSONArray(status);
                JSONArray feedArrayCount = new JSONArray(status);


                listContent = new ArrayList<>();
                listLocation = new ArrayList<>();
                listCount = new ArrayList<>();


                for (int i = 0; i < feedArrayDescription.length(); i++) {
                    JSONObject json_data = feedArrayDescription.getJSONObject(i);
                    String content = json_data.getString("content");
                    listContent.add(content);

                }


                for (int i = 0; i < feedArrayLocation.length(); i++) {
                    JSONObject json_data = feedArrayLocation.getJSONObject(i);
                    String location = json_data.getString("location");
                    listLocation.add(location);

                }

               for (int i = 0; i < feedArrayCount.length(); i++) {
                    JSONObject json_data = feedArrayCount.getJSONObject(i);
                    String count = json_data.getString("votes");
                    listCount.add(count);

                }

                ArrayList voteArray = new ArrayList<>();


        /* Här skapas rutorna i feeden */
                for (int i = listContent.size()-1; i >= 0; i--) {
                    System.out.println(listContent.size());

                    RelativeLayout feed = new RelativeLayout(this);
                    TextView descriptionArea = new TextView(this);
                    TextView locationArea = new TextView(this);
                    TextView countArea = new TextView(this);

                    descriptionArea.setText(listContent.get(i));
                    descriptionArea.setTextSize(24);
                    descriptionArea.setTextColor(Color.rgb(0, 0, 0));
                    descriptionArea.setHeight(200);
                    descriptionArea.setWidth(500);

                    locationArea.setText(listLocation.get(i));
                    locationArea.setTextSize(15);
                    locationArea.setHeight(200);
                    locationArea.setWidth(200);
                    locationArea.setTextColor(Color.rgb(0, 0, 0));



                    JSONObject json_data = feedArrayCount.getJSONObject(i);
                    String count = json_data.getJSONObject("votes").getString("vote_value__sum");

                    countArea.setText(count);
                    countArea.setTextSize(20);
                    countArea.setHeight(200);
                    countArea.setWidth(200);
                    countArea.setTextColor(Color.rgb(0, 0, 0));

                    feed.addView(descriptionArea);
                    feed.addView(locationArea);


                    feed.setBackgroundResource(R.color.white);
                    feed.setAlpha((float) 0.3);


                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, 300);
                    lp.setMargins(0, 0, 0, 10);

            /* Create up and down arrows and counter */

                    ImageView pin = new ImageView(this);
                    arrow_up = new ImageButton(this);
                    arrow_down = new ImageButton(this);

                    arrow_up.setId(i);
                    arrow_down.setId(-i);

                    final int Id_arrowUp = arrow_up.getId();
                    final int Id_arrowDown = arrow_down.getId();


                    voteArray.add(Id_arrowUp);
                    voteArray.add(Id_arrowDown);


                    System.out.println(voteArray);

                    pin.setImageResource(R.drawable.pin_live_feed);
                    arrow_up.setImageResource(R.drawable.arrow_up);
                    arrow_down.setImageResource(R.drawable.arrow_down);

                    ArrayList<ImageButton> buttons = new ArrayList<>();


                    buttons.add(arrow_up);
                    buttons.add(arrow_down);


                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp2.setMargins(0, 0, 130, 0);

                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp3.setMargins(0, 0, 130, 0);

                    RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp4.addRule(RelativeLayout.END_OF, buttons.indexOf(arrow_up));
                    lp4.setMargins(0, 120, 0, 0);

                    RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(70, 70);
                    lp5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                    RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp6.setMargins(80, 250, 0, 0);


                    arrow_up.setLayoutParams(lp2);
                    arrow_down.setLayoutParams(lp3);
                    countArea.setLayoutParams(lp4);
                    pin.setLayoutParams(lp5);
                    locationArea.setLayoutParams(lp6);


                    feed.addView(arrow_up);
                    feed.addView(arrow_down);
                    feed.addView(pin);
                    feed.addView(countArea);


                    ll.addView(feed, lp);

                    if(i == Id_arrowUp) {
                        arrow_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(Id_arrowUp);

                            }
                        });


                    }

                    if(-i == Id_arrowDown) {
                        arrow_down.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                System.out.println(Id_arrowDown);

                            }
                        });


                    }

                }






               /* arrow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            /*ÖKA VOTESEN*/

                 //   }
              //  });

              /*  arrow_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            /*MINSKA VOTESEN*/

                  //  }
               // });

            }
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    void createPost() {
                // Get the application context
                mContext = getApplicationContext();
                // Get the activity
                mActivity = student_livefeed.this;
                // Get the widgets reference from XML layout
                mLinearLayout = (LinearLayout) findViewById(R.id.ll);
                fab = (FloatingActionButton) findViewById(R.id.add_button);


                fab.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {

                        // Initialize a new instance of LayoutInflater service
                        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                        // Inflate the custom layout/view
                        final View customView = inflater.inflate(R.layout.popup_file_livefeed, null);
                        final RelativeLayout back_dim_layout = (RelativeLayout) findViewById(R.id.background_popup);
                        back_dim_layout.setVisibility(View.VISIBLE); /*set faded background */
                        mPopupWindow = new PopupWindow(
                                customView,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setOutsideTouchable(isRestricted());



                        // Set an elevation value for popup window
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }

                      Button add_post = (Button) customView.findViewById(R.id.post_added);
                        add_post.setOnClickListener(new View.OnClickListener(){
                        @Override
                         public void onClick(View view) {
                            final EditText feed_post = (EditText) customView.findViewById(R.id.feed_text);
                            final EditText feed_post_location = (EditText) customView.findViewById(R.id.feed_location);

                            mPopupWindow.setContentView(inflater.inflate(R.layout.popup_file_livefeed, null, false));

                            JSONObject post_dict = new JSONObject();

                            try {
                                post_dict.put("content", feed_post.getText().toString());
                                post_dict.put("location", feed_post_location.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (post_dict.length() > 0) {

                                Callback myCallback = new Callback();

                                try {
                                    String status = (myCallback.execution_Post("http://130.243.201.128:8000/messages/", token, "POST", post_dict.toString()));
                                    System.out.println(status);
                                } catch (Exception e) {

                                    System.out.println("Could not post feed");
                                }


                                mPopupWindow.dismiss();
                                back_dim_layout.setVisibility(View.GONE);
                            }
                            mActivity.recreate();

                        }

                        });






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

                        // Finally, show the popup window at the center location of root relative layout
                        mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);

                    }

                });




    }
}