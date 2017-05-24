package com.example.android.campusapp;

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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anna on 2017-05-18.
 */

public class student_comments extends Activity {
    ArrayList<Object> comments;
    private ArrayList<String> listComments;
    String content;
    String id;
    private ArrayList <ArrayList> listComments_message;
    private Activity mActivity =  student_comments.this;
    String url = "130.243.182.165";
    String token;
    int message_id;
    ScrollView scroll;






    protected void onCreate(Bundle savedInstanceState) {

          /*-----------remember token--------------------*/
        token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", null);
    /*----------------------------------------------*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_comments);

        Bundle bundle = getIntent().getExtras();
        content = bundle.getString("content");
        id = bundle.getString("id");
        message_id = Integer.parseInt(id);

         scroll = (ScrollView) findViewById(R.id.scroll);

        scroll.fullScroll(ScrollView.FOCUS_DOWN);


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("comments");

        comments = (ArrayList<Object>) args.getSerializable("comments");

        System.out.println("i andra klassen " + content);
        System.out.println("i andra klassen " + comments);



        createComments();
    }






    void createComments() {

        TextView post_header = (TextView) findViewById(R.id.post);
        TextView comment_header = (TextView) findViewById(R.id.comment_header);
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/Shrikhand-Regular.ttf");
        post_header.setTypeface(custom_font);
        comment_header.setTypeface(custom_font);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        LinearLayout commentsArea_out = (LinearLayout) findViewById(R.id.comments);

        final TextView descriptionArea = new TextView(this);

        final RelativeLayout post = new RelativeLayout(this);
        post.setBackgroundResource(R.color.white);
        post.setAlpha((float) 0.7);


        descriptionArea.setText(content);
        descriptionArea.setTextSize(21);
        descriptionArea.setTextColor(Color.rgb(0, 0, 0));
        descriptionArea.setHeight(200);
        descriptionArea.setWidth(500);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 0, 0);

        descriptionArea.setLayoutParams(lp);

        post.addView(descriptionArea);



        for (int i = 0 ; i <= comments.size()-1; i++) {
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


        ll.addView(post);


        /*Add comment*/

        Button add_post = (Button) findViewById(R.id.comment_added);
        add_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
                        String status = (myCallback.execution_Post("http://" + url +":8000/messages/comments/", token, "POST", post_dict.toString()));
                        System.out.println(status);
                    } catch (Exception e) {

                        System.out.println("Could not post feed");
                    }

                }
                comments.clear();
                System.out.println("gamla " + comments);

                Callback myCallback = new Callback();

                try {
                    String status = (myCallback.execution_Get("http://" + url + ":8000/messages/", token, "GET", "No JsonData"));
                    System.out.println(status);
                    if (status == "false") {
                        Toast.makeText(student_comments.this, "could not fetch feeds", Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray commentArray = new JSONArray(status);

                        listComments_message = new ArrayList<>();


                        for (int i = 0; i < commentArray.length(); i++) {
                            JSONObject json_data = commentArray.getJSONObject(i);

                            JSONArray values = json_data.getJSONArray("comments");

                            listComments = new ArrayList<>();

                            for(int j = 0; j<values.length();j++){
                                JSONObject comment = values.getJSONObject(j);

                                String comment_content = comment.getString("content");

                                listComments.add(comment_content);
                            }
                            listComments_message.add(listComments);
                        }



                        for(int i = 0; i < listComments_message.get(message_id-2).size(); i++ ){
                            comments.add(listComments_message.get(message_id-2).get(i));
                        }
                        System.out.println("nya" +comments);
                        Toast.makeText(student_comments.this, "Scroll down to see your post", Toast.LENGTH_LONG).show();


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
                comment_post.setText("");


            }


        });


    }
}
