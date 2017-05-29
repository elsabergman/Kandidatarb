package com.example.android.campusapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.button;
import static android.R.attr.data;
import static com.example.android.campusapp.R.layout.spinner_item;

/**
 * Created by argr0731 on 2017-04-13. This Adapter handles the spinner with checkboxes in todays_events. This is linked to todays_events_spinner_StateVOTypes.java which collects the calls from this class.
 *
 * Basic code from Ironman post on stackexchange Jul 14 2016: http://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox
 */

public class todays_events_spinner_MyAdapterTypes extends ArrayAdapter<todays_events_spinner_StateVOTypes> {
    private Context mContext;
    private ArrayList<todays_events_spinner_StateVOTypes> listState;
    private todays_events_spinner_MyAdapterTypes todayseventsspinnerMyAdapterTypes;
    private boolean isFromView = false;
    private todays_events sendTodaysEvents;
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
            convertView = layoutInflator.inflate(spinner_item, null);
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

        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


       /*     @Override
            public void performClick(CompoundButton buttonView, boolean isChecked) {


            }
            private boolean mOpenInitiated = false;
            @Override
            public boolean performClick() {
                // register that the Spinner was opened so we have a status
                // indicator for when the container holding this Spinner may lose focus
                mOpenInitiated = true;
                if (mListener != null) {
                    mListener.onSpinnerOpened(this);
                }
                return super.performClick();
            }*/


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();


                if (isChecked == true) {

                    //Here we see the checked boxes and their name, put it in to a array and send it to todays_events to update sorting of list

                    if (items_checkedTypes.contains(listState.get(position).getTitle())) {
                    //Do nothing
                    }

                    else{
                        //items_checkedTypes is sent to todays_events
                        items_checkedTypes.add(listState.get(position).getTitle());
                        sendTodaysEvents.sendInfoToDatabaseType(items_checkedTypes);
                    }

                } else {
                    items_checkedTypes.remove(listState.get(position).getTitle());
                    sendTodaysEvents.sendInfoToDatabaseType(items_checkedTypes);

                    //listState.get(position).setSelected(false);
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
