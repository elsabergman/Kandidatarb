package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Anna on 2017-04-24.
 */

public class student_livefeed extends student_SlidingMenuActivity {

    private Context mContext;
    private Activity mActivity;

    private LinearLayout mLinearLayout;
    private FloatingActionButton fab;

    private PopupWindow mPopupWindow;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*--navigation drawer menu --*/
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_livefeed, null);
        drawer.addView(contentView, 0);


        createFeed();
        createPost();

    }


    void createFeed() {


        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        /* Här skapas rutorna i feeden */
        for (int i = 1; i < 6; i++) {

            TextView feed = new TextView(this);
            feed.setText("Feed " + i);
            feed.setTextColor(getResources().getColor(R.color.black));
            feed.setBackgroundResource(R.color.white);
            feed.setAlpha((float) 0.3);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
            lp.setMargins(0, 0, 0, 10);
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
                        // Call requires API level 21
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }




                      Button add_post = (Button) customView.findViewById(R.id.post_added);
                        add_post.setOnClickListener(new View.OnClickListener(){
                        @Override
                         public void onClick(View view) {
                            final EditText feed_post = (EditText) customView.findViewById(R.id.feed_text);

                            mPopupWindow.setContentView(inflater.inflate(R.layout.popup_file_livefeed, null, false));
                           // String feed_post_data = feed_post.getText().toString();
                            //System.out.println(feed_post_data);

                            JSONObject post_dict = new JSONObject();

                            try {
                                post_dict.put("content", feed_post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (post_dict.length() > 0) {

                                Callback myCallback = new Callback();

                                try {
                                    String status = (myCallback.execution_Post("http://130.243.134.165:8000/messages/", "0", "POST", post_dict.toString()));
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