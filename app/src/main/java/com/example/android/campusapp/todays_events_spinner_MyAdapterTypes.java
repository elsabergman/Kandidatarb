package com.example.android.campusapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by argr0731 on 2017-04-13. This Adapter handles the spinner with checkboxes in todays_events. Code from Ironman post on stackexchange Jul 14 2016: http://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox
 */

public class todays_events_spinner_MyAdapterTypes extends ArrayAdapter<todays_events_spinner_StateVOTypes> {
    private Context mContext;
    private ArrayList<todays_events_spinner_StateVOTypes> listState;
    private todays_events_spinner_MyAdapterTypes todayseventsspinnerMyAdapterTypes;
    private boolean isFromView = false;

    private todays_events sendTodaysEvents;


    //public static String[] checkedCampuses;
    final ArrayList<String> items_checkedTypes= new ArrayList<String>();
    //private todays_events.view items_checkedCampuses; //our Fragment which connects to Callback

    public todays_events_spinner_MyAdapterTypes(Context context, int resource, List<todays_events_spinner_StateVOTypes> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<todays_events_spinner_StateVOTypes>) objects;
        this.todayseventsspinnerMyAdapterTypes = this;

        this.sendTodaysEvents = (todays_events) context;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;


        if (items_checkedTypes.contains(listState.get(position).getTitle())) {
            holder.mCheckBox.setChecked(listState.get(position).isSelected());
        }



        /*if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);


        }*/
        //if in list it is checked
       /* if (items_checkedTypes.contains(listState.get(position).getTitle())) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
            isChecked = true;
        }*/


        else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }





        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {





            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();


                if (isChecked == true) {

                    //Here we see the checked boxes and their name, put it in to a array and send it to todays_events

                    System.out.println("Denna position har namn "+listState.get(position).getTitle());
                    System.out.println("VI ÄR CHECKED?");
                    if (items_checkedTypes.contains(listState.get(position).getTitle())) {
                        System.out.println(isChecked);
                        System.out.println("Denna position har namn "+listState.get(position).getTitle()+ "och borde inte läggas in i listan");
                        System.out.println("listan ser nu ut såhär: "+items_checkedTypes);
                    }

                    else{
                        //items_checkedTypes is sent to todays_events
                        items_checkedTypes.add(listState.get(position).getTitle());
                      //  todays_events sendTodaysEvents = new todays_events();
                        sendTodaysEvents.sendInfoToDatabaseType(items_checkedTypes);
                        System.out.println("Sent to sendinfotodatabasetype "+items_checkedTypes);
                    }


                }


                if (isChecked == false) {
                    System.out.println("VI ÄR INTE CHECKED?");
                    items_checkedTypes.remove(listState.get(position).getTitle());
                    //todays_events sendTodaysEvents = new todays_events();
                    sendTodaysEvents.sendInfoToDatabaseType(items_checkedTypes);

                }




            }
        });
        return convertView;
    }


    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
