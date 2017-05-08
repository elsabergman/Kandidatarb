package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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


        /* Här skapas rutorna i feeden */
        for (int i = 1; i < 10; i++) {

            RelativeLayout feed = new RelativeLayout(this);
            
            feed.setBackgroundResource(R.color.white);
            feed.setAlpha((float) 0.3);


            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 300);
            lp.setMargins(0, 0, 0, 10);

            /* Create up and down arrows and counter */
            ImageButton arrow_up =  new ImageButton(this);
            ImageButton arrow_down = new ImageButton(this);
            TextView count = new TextView(this);
            count.setText("0");
            count.setTextColor(getResources().getColor(R.color.black));
            count.setTextSize(20);



            arrow_up.setImageResource(R.drawable.arrow_up);
            arrow_down.setImageResource(R.drawable.arrow_down);

            ArrayList <ImageButton> buttons = new ArrayList<>();


            buttons.add(arrow_up);
            buttons.add(arrow_down);



            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp2.setMargins(0,0,100,0);

            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
           lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp3.setMargins(0,0,100,0);

            RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp4.addRule(RelativeLayout.END_OF, buttons.indexOf(arrow_up));
            lp4.setMargins(0,120,145,0);



            arrow_up.setLayoutParams(lp2);
            arrow_down.setLayoutParams(lp3);
            count.setLayoutParams(lp4);

            feed.addView(arrow_up);
            feed.addView(arrow_down);
            feed.addView(count);


            ll.addView(feed, lp);

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
                            mPopupWindow.setContentView(inflater.inflate(R.layout.popup_file_livefeed, null, false));

                            JSONObject post_dict = new JSONObject();

                            try {
                                post_dict.put("content", feed_post.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (post_dict.length() > 0) {

                                Callback myCallback = new Callback();

                                try {
                                    String status = (myCallback.execution_Post("http://212.25.150.89:8000/messages/", token, "POST", post_dict.toString()));
                                    System.out.println(status);
                                } catch (Exception e) {

                                    System.out.println("Could not post feed");
                                }


                                mPopupWindow.dismiss();
                                back_dim_layout.setVisibility(View.GONE);
                            }
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