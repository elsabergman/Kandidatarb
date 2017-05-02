package com.example.android.campusapp;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.campusapp.R.id.textView;


/**
 * Created by Anna on 2017-04-24.
 */

public class student_livefeed extends SlidingMenuActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_livefeed);

        int viewId = getResources().getIdentifier("feed", "id", getPackageName())+0;

        List <View> feedlist = new ArrayList<>();
        for (int i = 1; i < 6; i++) {


            feedlist.add((View)findViewById(viewId));

            // feedlist.set(i, (View) findViewById(viewId));

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, viewId-1);
            viewId = viewId + 1;

        }


    }

}






