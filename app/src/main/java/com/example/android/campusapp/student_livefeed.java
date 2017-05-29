package com.example.android.campusapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.io.Serializable;
import java.util.ArrayList;

import java.util.concurrent.ExecutionException;




/**
 * Created by Anna on 2017-04-24.
 */

public class student_livefeed extends student_SlidingMenuActivity implements Serializable  {

    private Context mContext;
    private Activity mActivity;

    private LinearLayout mLinearLayout;
    private FloatingActionButton fab;

    private PopupWindow mPopupWindow;
    String token;
    String status;
    private ArrayList<String> listContent;
    private ArrayList<String> listID;
    private ArrayList<String> listLocation;
    private ArrayList<String> listCount;
    private ArrayList<ArrayList> listComments_message;
    private ArrayList<String> listComments;
    private ArrayList<String> listCommentCount;


    private ArrayList<String> neutralArray;
    private ArrayList<String> greenArray;
    private ArrayList<String> redArray;
    private String before_voting;
    private String after_voting;
     ImageButton arrow_up;
     ImageButton arrow_down;
    ImageButton comment;
    String comment_content;


    String url = "212.25.147.115";








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

        //Hämtar alla inlägg från databasen och lägger de i list
        Callback myCallback = new Callback();

        try {
            status = (myCallback.execution_Get("http://" + url +":8000/messages/", token, "GET", "No JsonData"));
            System.out.println(status);
            if (status == "false") {
                Toast.makeText(student_livefeed.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
            } else {

                JSONArray feedArray = new JSONArray(status);


                listID = new ArrayList<>();



                listContent = new ArrayList<>();
                listLocation = new ArrayList<>();
                listCount = new ArrayList<>();
                listComments_message = new ArrayList<>();
                listCommentCount = new ArrayList<>();





                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject json_data = feedArray.getJSONObject(i);
                    String content = json_data.getString("content");
                    listContent.add(content);

                    String ID = json_data.getString("id");
                    listID.add(ID);

                    String location = json_data.getString("location");
                    listLocation.add(location);

                    String count = json_data.getString("votes");
                    listCount.add(count);



                    JSONArray values = json_data.getJSONArray("comments");

                    listComments = new ArrayList<>();

                    for(int j = 0; j<values.length();j++){
                        JSONObject comment = values.getJSONObject(j);

                        comment_content = comment.getString("content");

                        listComments.add(comment_content);
                    }

                    listComments_message.add(listComments);


                    listCommentCount.add(String.valueOf(listComments.size()));

                }



                ArrayList voteArray = new ArrayList<>();


        /* Här skapas rutorna i feeden */
                for (int i = listContent.size()-1; i >= 0; i--) {

                    RelativeLayout feed = new RelativeLayout(this);
                    final TextView descriptionArea = new TextView(this);
                    TextView locationArea = new TextView(this);
                    final TextView countArea = new TextView(this);
                    TextView commentArea = new TextView(this);
                    TextView commentCountArea = new TextView(this);

                    descriptionArea.setText(listContent.get(i));
                    descriptionArea.setTextSize(21);
                    descriptionArea.setTextColor(Color.rgb(0, 0, 0));
                    descriptionArea.setHeight(200);
                    descriptionArea.setWidth(500);

                    locationArea.setText(listLocation.get(i));
                    locationArea.setTextSize(15);
                    locationArea.setHeight(200);
                    locationArea.setWidth(200);
                    locationArea.setTextColor(Color.rgb(0, 0, 0));

                    countArea.setText(listCount.get(i));
                    countArea.setTextSize(20);
                    countArea.setHeight(200);
                    countArea.setWidth(200);
                    countArea.setTextColor(Color.rgb(0, 0, 0));

                    commentCountArea.setText(listCommentCount.get(i));
                    commentCountArea.setTextSize(20);
                    commentCountArea.setHeight(200);
                    commentCountArea.setWidth(200);
                    commentCountArea.setTextColor(Color.rgb(0, 0, 0));


                    feed.addView(descriptionArea);
                    feed.addView(locationArea);
                    feed.addView(commentCountArea);


                    feed.setBackgroundResource(R.color.white);
                    feed.setAlpha((float) 0.45);


                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, 300);
                    lp.setMargins(0, 0, 0, 15);

            /* Create up and down arrows and counter */

                    ImageView pin = new ImageView(this);
                    comment = new ImageButton(this);
                    arrow_up = new ImageButton(this);
                    arrow_down = new ImageButton(this);


                    arrow_up.setId(i+1);
                    arrow_down.setId(i+1);

                    final int Id_arrowUp = arrow_up.getId();
                    final int Id_arrowDown = arrow_down.getId();


                    voteArray.add(Id_arrowUp);
                    voteArray.add(Id_arrowDown);


                    pin.setImageResource(R.drawable.pin_live_feed);
                    comment.setBackgroundResource(R.drawable.comments_live_feed);

                    arrow_up.setBackgroundResource(R.drawable.arrow_up);
                    arrow_down.setBackgroundResource(R.drawable.arrow_down);

                    ArrayList<ImageButton> buttons = new ArrayList<>();


                    buttons.add(arrow_up);
                    buttons.add(arrow_down);


                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp2.setMargins(0, 20, 150, 0);

                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp3.setMargins(0, 0, 150, 20);

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

                    RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp7.setMargins(20, 20, 0, 0);

                    RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(70, 70);
                    lp8.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp8.setMargins(300,0,0,0);

                    RelativeLayout.LayoutParams lp9 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp9.setMargins(320, 250, 0, 0);

                    RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp10.setMargins(380, 240, 0, 0);



                    arrow_up.setLayoutParams(lp2);
                    arrow_down.setLayoutParams(lp3);
                    countArea.setLayoutParams(lp4);
                    pin.setLayoutParams(lp5);
                    locationArea.setLayoutParams(lp6);
                    descriptionArea.setLayoutParams(lp7);
                    comment.setLayoutParams(lp8);
                    commentArea.setLayoutParams(lp9);
                    commentCountArea.setLayoutParams(lp10);


                    feed.addView(arrow_up);
                    feed.addView(arrow_down);
                    feed.addView(comment);
                    feed.addView(pin);
                    feed.addView(countArea);


                    ll.addView(feed, lp);


                    /*Click on comment and get to other page*/

                    final int u = i;

                    comment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            //Intent intent = new Intent(student_livefeed.this, student_comments.class);
                            //startActivity(intent);


                            Intent intent = new Intent(student_livefeed.this, student_comments.class);
                            intent.putExtra("content", listContent.get(u));
                            intent.putExtra("id", listID.get(u));

                            ArrayList <Object> object = listComments_message.get(u);
                            Bundle args = new Bundle();
                            args.putSerializable("comments", (Serializable)object);
                            intent.putExtra("comments", args);







                            startActivity(intent);






                        }
                        });

                    /*Upvote-listener*/


                    if(i+1 == Id_arrowUp) {
                        arrow_up.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                System.out.println(status);



                                JSONObject post_dict = new JSONObject();


                                try {
                                    post_dict.put("message_id", String.valueOf(Id_arrowUp));
                                    post_dict.put("upvote", "True");
                                    post_dict.put("downvote", "False");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Callback myCallback = new Callback();


                                try {
                                    String status = (myCallback.execution_Post("http://" + url +":8000/vote/", token, "POST", post_dict.toString()));
                                } catch (Exception e) {

                                    System.out.println("Could not post feed");
                                }

                                try {
                                    status = (myCallback.execution_Get("http://" + url +":8000/messages/?id=" + String.valueOf(Id_arrowUp), token, "GET", "No JsonData"));
                                    if (status == "false") {
                                        Toast.makeText(student_livefeed.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
                                    } else {
                                        JSONArray feedArrayCount = new JSONArray(status);

                                        listCount = new ArrayList<>();

                                         JSONObject json_data_vote = feedArrayCount.getJSONObject(0);
                                         String count = json_data_vote.getString("votes");



                                         listCount.add(count);
                                         countArea.setText(count);

                                         String upvote = json_data_vote.getJSONObject("my_vote").getString("upvote");
                                         String downvote = json_data_vote.getJSONObject("my_vote").getString("downvote");
                                        System.out.println(upvote+ "    " + downvote);

                                       if (upvote == "true" && downvote == "false"){
                                           System.out.println("hooho");
                                           view.setBackgroundResource(R.drawable.arrow_up_green);

                                        }
                                        else if(downvote == "true" && upvote =="true"){
                                           arrow_down.setImageResource(R.drawable.arrow_down_red);

                                       }
                                       else{
                                            view.setBackgroundResource(R.drawable.arrow_up);

                                        }

                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });


                    }




                    if(i+1 == Id_arrowDown) {
                        arrow_down.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                JSONObject post_dict = new JSONObject();

                                try {
                                    post_dict.put("message_id", String.valueOf(Id_arrowDown));
                                    post_dict.put("upvote", "False");
                                    post_dict.put("downvote", "True");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Callback myCallback = new Callback();

                                try {
                                    String status = (myCallback.execution_Post("http://" + url +":8000/vote/", token, "POST", post_dict.toString()));
                                } catch (Exception e) {

                                    System.out.println("Could not post feed");
                                }

                                try {
                                    status = (myCallback.execution_Get("http://" + url +":8000/messages/?id=" + String.valueOf(Id_arrowDown), token, "GET", "No JsonData"));
                                    if (status == "false") {
                                        Toast.makeText(student_livefeed.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
                                    } else {

                                        JSONArray feedArrayCount = new JSONArray(status);

                                        listCount = new ArrayList<>();

                                        JSONObject json_data_vote = feedArrayCount.getJSONObject(0);
                                        String count = json_data_vote.getString("votes");
                                        System.out.println(count);
                                        listCount.add(count);
                                        countArea.setText(count);

                                        System.out.println(status);

                                        String upvote = json_data_vote.getJSONObject("my_vote").getString("upvote");
                                        String downvote = json_data_vote.getJSONObject("my_vote").getString("downvote");


                                            if (upvote == "true") {
                                                arrow_up.setImageResource(R.drawable.arrow_up_green);

                                            } else if (downvote == "true") {
                                                view.setBackgroundResource(R.drawable.arrow_down_red);
                                                arrow_up.setImageResource(R.drawable.arrow_up);


                                            } else {
                                                view.setBackgroundResource(R.drawable.arrow_down);

                                            }


                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });


                    }

                }

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

                      Button add_post = (Button) customView.findViewById(R.id.comment_added);
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
                                    String status = (myCallback.execution_Post("http://" + url +":8000/messages/", token, "POST", post_dict.toString()));
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