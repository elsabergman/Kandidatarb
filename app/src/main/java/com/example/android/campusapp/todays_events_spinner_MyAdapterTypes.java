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


        //TEST

        //holder.mCheckBox.setChecked(listState.get(position).isSelected());

        convertView.setOnClickListener(new View.OnClickListener()
                                       {
                                           public void onClick(View v)
                                           {

                                               System.out.println("KKKKKKKKK");
                                               ViewHolder temp = (ViewHolder) v.getTag();
                                               temp.mCheckBox.setChecked(!temp.mCheckBox.isChecked());

                                               int len = items_checkedTypes.size();
                                               for (int i = 0; i < len; i++)
                                               {
                                                   System.out.println("HHHHHHHHHHHH");
                                                   if (i == position)
                                                   {
                                                       (listState.get(position)).setSelected(!(listState.get(position)).isSelected());
                                                       //Log.i("TAG", "On Click Selected : " + (listState.get(position)).getTitle() + " : " + (listState.get(position)).isSelected());
                                                       System.out.println("EEEEEEEEEEEEEEEEEEEEEE");
                                                       break;
                                                   }
                                               }
                                           }
                                       });

        //END TEST



        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if (items_checkedTypes.contains(listState.get(position).getTitle())) {
            System.out.println("title checked "+listState.get(position).getTitle());
            System.out.println("title checked status "+listState.get(position).isSelected());

            //holder.mCheckBox.setChecked(listState.get(position).isSelected());
        }


        /*if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        }*/

        //if in list it is checked
       /* if (items_checkedTypes.contains(listState.get(position).getTitle())) {
            //holder.mCheckBox.setVisibility(View.INVISIBLE);
            //boolean isChecked = true;
            //todays_events_spinner_StateVOTypes.isSelected();
            //holder.mCheckBox.setChecked(false);
            System.out.println("Putting holder.mCheckBox.setChecked(false);");
            //holder.mCheckBox.setVisibility(View.INVISIBLE);
            //holder.mCheckBox.setChecked(true);
            //items_checkedTypes.get(position).setSelected(true);

            //Gör en lösning där vi går igenom hela items_checkedTypes och kollar om det finns i listState.get(position) och om det gör det så setSelected(true) på den liststetegrejen. Arvid 22/5
*/
      /*      for(int i = 0; i < listState.size(); i++){

                String testitemliststate = listState.get(i).getTitle();

               // items_checkedTypes.get(i);

                if (items_checkedTypes.contains(testitemliststate)/*   equals(items_checkedTypes.get(i).toString())*/ {



                   /* listState.get(i).setSelected(true);
                    System.out.println("liststate setselected(true);");

                }

                else{


                }*/

            }

           /* if (listState.get(position).getTitle().equals(items_checkedTypes.toString())) {

                listState.get(position).setSelected(true);

            }*/

        /*}*/



 /*       else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
*/





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

                    //listState.get(position).setSelected(true);

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
                        sendTodaysEvents.sendInfoToDatabaseType(items_checkedTypes);
                        System.out.println("Sent to sendinfotodatabasetype "+items_checkedTypes);
                    }

                } else {
                    System.out.println("VI ÄR INTE CHECKED?");
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
