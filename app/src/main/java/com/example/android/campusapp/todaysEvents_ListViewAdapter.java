package com.example.android.campusapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.android.campusapp.Constants.FIRST_COLUMN;
import static com.example.android.campusapp.Constants.SECOND_COLUMN;
import static com.example.android.campusapp.Constants.THIRD_COLUMN;
import static com.example.android.campusapp.Constants.ID;
/**
 * Created by elsabergman on 2017-05-16.
 */

public class todaysEvents_ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    public ArrayList<HashMap<String, Integer>> imageList;
    Activity activity;
    TextView txtFirst, txtSecond, txtThird,txtDescription, txtFavorites, txtURL;
    ImageView heart;
    ListView listView;
    boolean isVisible;
    String token;
    String serverURL = "130.243.182.165";
    public todaysEvents_ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list, ListView listView, String token){
        super();
        this.activity=activity;
        this.list=list;
        this.listView = listView;
        this.token = token;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();

    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final LayoutInflater inflater=activity.getLayoutInflater();
        if(convertView == null){
            convertView=inflater.inflate(R.layout.student_column_rows, null);


            txtFavorites = (TextView) convertView.findViewById((R.id.fav));
        }
       else {
            convertView = convertView;
        }
        txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
        txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
        txtThird=(TextView) convertView.findViewById(R.id.Time);
        txtDescription = (TextView) convertView.findViewById((R.id.description));
        txtURL = (TextView) convertView.findViewById((R.id.url));

        final HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                String myDescription = item.get("Description");
                String myUrl = item.get("Url");
               final String id_event = item.get("id");

                System.out.println("MITT ID " + id_event);
                txtDescription = (TextView) view.findViewById((R.id.description));
                txtURL = (TextView) view.findViewById((R.id.url));
                txtFavorites = (TextView) view.findViewById(R.id.fav);
                txtDescription.setTextColor(Color.DKGRAY);
                txtDescription.setText("Description: " + myDescription);
                txtURL.setText(myUrl);
                SpannableString content = new SpannableString(item.get("favorites"));
                content.setSpan(new UnderlineSpan(), 0, item.get("favorites").length(), 0);
                txtFavorites.setText(content);
                //txtURL.setLinkTextColor(Color.DKGRAY);

               /* if (myUrl!= ""){

                    txtURL.setText(myUrl);
                }

                else {
                    txtURL.setText(" ");
                }*/

                if ( (txtDescription.getVisibility() == View.VISIBLE)  )
                {

                    txtDescription.setVisibility(View.GONE);
                    txtURL.setVisibility(View.GONE);
                    txtFavorites.setVisibility(View.GONE);
                    txtDescription.invalidate();
                    // txtURL.invalidate();

                    view.setBackgroundColor(Color.WHITE);

                }
                else
                {
                    txtDescription.setVisibility(View.VISIBLE);
                    txtURL.setVisibility(View.VISIBLE);
                    txtFavorites.setVisibility(View.VISIBLE);
                    txtDescription.invalidate();
                    /*add to favorites */
                    txtFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject post_dict = new JSONObject(); //creates Json object

                            Callback myCallback = new Callback();

                            try {
                                post_dict.put("event_id", id_event);
                                System.out.println(post_dict + "JSON FILE");

                                String status = (myCallback.execution_Post("http://"+serverURL+":8000/events/my-favourites/add/", token,"POST",post_dict.toString()));

                                if (status == "true") {
                                    Toast.makeText(v.getContext(), "Event was added to your favorites", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(v.getContext(), "Event could not be added to favorites", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    // txtURL.invalidate();

                    view.setBackgroundResource(R.color.very_light_grey);




                }

            }
        });
        return convertView;
    }
}

