package com.example.android.campusapp;

/**
 * Created by elsabergman on 2017-05-11.
 */

           import android.app.Activity;
           import android.graphics.drawable.Drawable;
           import android.media.Image;
           import android.support.v4.content.res.ResourcesCompat;
           import android.view.LayoutInflater;
           import android.view.View;
           import android.view.ViewGroup;
           import android.widget.AdapterView;
           import android.widget.BaseAdapter;
           import android.widget.ImageButton;
           import android.widget.ImageView;
           import android.widget.ListView;
           import android.widget.TextView;
           import android.widget.ToggleButton;

           import org.w3c.dom.Text;

           import java.util.ArrayList;
           import java.util.HashMap;
           import static com.example.android.campusapp.Constants.FIRST_COLUMN;
           import static com.example.android.campusapp.Constants.FOURTH_COLUMN;
           import static com.example.android.campusapp.Constants.HEART;
           import static com.example.android.campusapp.Constants.SECOND_COLUMN;
        import static com.example.android.campusapp.Constants.THIRD_COLUMN;

public class student_ListViewAdapter extends BaseAdapter{


        public ArrayList<HashMap<String, String>> list;
        public ArrayList<HashMap<String, Integer>> imageList;
        Activity activity;
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        ImageView heart;
        TextView txtDescription;
        ListView listView;
        boolean isVisible;
        ImageView txtFourth;
        public student_ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list, ListView listView, ArrayList<HashMap<String, Integer>> imageList){
            super();
            this.activity=activity;
            this.list=list;
            this.listView = listView;
            this.imageList = imageList;
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
                txtFirst=(TextView) convertView.findViewById(R.id.dateEvent);
                txtSecond=(TextView) convertView.findViewById(R.id.nameEvent);
                txtThird=(TextView) convertView.findViewById(R.id.Time);
                txtFourth = (ImageView) convertView.findViewById(R.id.owner);
                heart=(ImageView) convertView.findViewById(R.id.fav_image);
                txtDescription = (TextView) convertView.findViewById((R.id.description));
                // listView = (ListView) convertView.findViewById(R.id.your_event_list);
            }

            final HashMap<String, String> map=list.get(position);
            final HashMap<String, Integer> imagemap = imageList.get(position);
            txtFirst.setText(map.get(FIRST_COLUMN));
            txtSecond.setText(map.get(SECOND_COLUMN));
            txtThird.setText(map.get(THIRD_COLUMN));
         //  txtFourth.setImageResource(map.get(FOURTH_COLUMN));
            heart.setImageResource(imagemap.get(HEART));
            heart.setImageResource(R.drawable.favorite_toggle);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                    String myDescription = item.get("Description");
                    txtDescription = (TextView) view.findViewById((R.id.description));
                    txtDescription.setText(myDescription);
                    if ( txtDescription.getVisibility() == View.VISIBLE)
                    {
                        txtDescription.setVisibility(View.GONE);
                        txtDescription.invalidate();
                    }
                    else
                    {
                        txtDescription.setVisibility(View.VISIBLE);
                        txtDescription.invalidate();
                    }
                }
            });
            return convertView;
        }
    }

