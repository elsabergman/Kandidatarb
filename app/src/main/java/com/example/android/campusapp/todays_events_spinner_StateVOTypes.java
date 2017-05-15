package com.example.android.campusapp;

/**
 * Created by argr0731 on 2017-04-13. This file handles the spinner with checkboxes in todays events. Code from Ironman post on stackexchange Jul 14 2016: http://stackoverflow.com/questions/38417984/android-spinner-dropdown-checkbox
 */

public class todays_events_spinner_StateVOTypes {
    private String title;
    private boolean selected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

