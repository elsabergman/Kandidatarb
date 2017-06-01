package com.example.android.campusapp;
/**
 * Name: student_comments.java
 * Author: Anna Eriksson
 * This page is where the commenting happens. Firstly it gets the the message content from the live feed so it
 * shows the the right information when clicking on the comment symbol att the message in the live feed. Then there is
 * a comments area below which shows all the comments which have been commented on the post. In the bottom om the page,
 * there is a text field where the user can add his or her own comment. After clicking post, the comment is added in the
 * comment area.
 *
 * The class has two methods: displayingComments() and addComments()
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class student_comments extends Activity {
    ArrayList<Object> comments;
    private ArrayList<String> listComments;
    String content;
    String id;
    private ArrayList <ArrayList> listComments_message;
    private Activity mActivity =  student_comments.this;
    String url = "212.25.151.161";
    String token;
    int message_id;
    ScrollView scroll;

    protected void onCreate(Bundle savedInstanceState) {
        /*-----------remember token--------------------*/
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
        /*----------------------------------------------*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_comments);

        /* gets the information from student_livefeed */

        Bundle bundle = getIntent().getExtras();
        content = bundle.getString("content");
        id = bundle.getString("id");
        message_id = Integer.parseInt(id);

        scroll = (ScrollView) findViewById(R.id.scroll);
        scroll.fullScroll(ScrollView.FOCUS_DOWN);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("comments");

        /* creates a list for the comments, which has a certain length*/

        comments = (ArrayList<Object>) args.getSerializable("comments");

        displayComments();
        addComments();
    }






    void displayComments() {
        /* connects the headers to the xml-files id */
        TextView post_header = (TextView) findViewById(R.id.post);
        TextView comment_header = (TextView) findViewById(R.id.comment_header);
        /*sets font style*/
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        post_header.setTypeface(custom_font);
        comment_header.setTypeface(custom_font);

        /* connects the linear layouts to the xml-files id */

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        LinearLayout commentsArea_out = (LinearLayout) findViewById(R.id.comments);

        /* creates message area and sets style */
        final RelativeLayout message = new RelativeLayout(this);
        message.setBackgroundResource(R.color.white);
        message.setAlpha((float) 0.7);

        /*creates description area and sets style and adds it to the message*/
        final TextView descriptionArea = new TextView(this);

        descriptionArea.setText(content);
        descriptionArea.setTextSize(21);
        descriptionArea.setTextColor(Color.rgb(0, 0, 0));
        descriptionArea.setHeight(200);
        descriptionArea.setWidth(500);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 0, 0);

        descriptionArea.setLayoutParams(lp);

        message.addView(descriptionArea);

        /* goes through all comments*/

        for (int i = 0 ; i <= comments.size()-1; i++) {
            /* creates comment area and sets style and it to the commentsArea_out*/
            RelativeLayout comment = new RelativeLayout(this);

            final TextView commentArea = new TextView(this);

            comment.setBackgroundResource(R.color.white);
            comment.setAlpha((float) 0.5);

            commentArea.setText((String) comments.get(i));
            commentArea.setTextSize(15);
            commentArea.setHeight(200);
            commentArea.setWidth(200);
            commentArea.setTextColor(Color.rgb(0, 0, 0));

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(20, 20, 0, 0);

            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(0, 20, 0, 0);

            commentArea.setLayoutParams(lp1);
            comment.addView(commentArea);

            commentsArea_out.addView(comment, lp2);


        }

        //add comment to the linear layout
        ll.addView(message);





    }

    void addComments(){
    /* connect the button to the XMLfile's button id and add a click listener*/
        Button add_post = (Button) findViewById(R.id.comment_added);
        add_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //connect the text field to til XMLfile's text field id
                final EditText comment_post = (EditText) findViewById(R.id.comment_field);


                JSONObject post_dict = new JSONObject();

                try {
                    post_dict.put("content", comment_post.getText().toString());
                    post_dict.put("message", message_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (post_dict.length() > 0) {

                    Callback myCallback = new Callback();

                    try {
                        /* post content and the message_id to the database*/
                        String status = (myCallback.execution_Post("http://" + url +":8000/messages/comments/", token, "POST", post_dict.toString()));

                    } catch (Exception e) {

                        System.out.println("Could not message feed");
                    }

                }
                /* clears the list "comments" */
                comments.clear();

                Callback myCallback = new Callback();

                try {
                    /* gets the new comments from the database */
                    String status = (myCallback.execution_Get("http://" + url + ":8000/messages/", token, "GET", "No JsonData"));
                    if (status == "false") {
                        Toast.makeText(student_comments.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray commentArray = new JSONArray(status);

                        listComments_message = new ArrayList<>();

                    //adds all comments to a listComments for each message and adds these lists to listComments_message
                       for (int i = 0; i < commentArray.length(); i++) {
                            JSONObject json_data = commentArray.getJSONObject(i);

                            JSONArray values = json_data.getJSONArray("comments");

                            listComments = new ArrayList<>();

                           //adds all comments to a listComments for each message
                            for(int j = 0; j<values.length();j++){
                                JSONObject comment = values.getJSONObject(j);

                                String comment_content = comment.getString("content");

                                listComments.add(comment_content);
                            }
                            //adds lists to the listComments_message
                            listComments_message.add(listComments);
                        }

                        // adds all comments for the specific message
                        for(int i = 0; i < listComments_message.get(message_id-1).size(); i++ ){
                            comments.add(listComments_message.get(message_id-1).get(i));
                        }
                        //confirmation
                        Toast.makeText(student_comments.this, "Scroll down to see your message", Toast.LENGTH_LONG).show();
                    }

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mActivity.recreate();
                // sets the text filed to nothing after posting a comments
                comment_post.setText("");


            }


        });
    }
}
