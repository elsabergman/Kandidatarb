package com.example.android.campusapp;

import android.support.v4.app.Fragment;

import java.util.concurrent.ExecutionException;

/**
 * Name: Callback.java
 * Author: Elsa Bergman
 * Some of this code is written with help from Simon Zettervall and davidcondrey for code they have written on Stack Overflow
 * on March 17th 2013 and October 9th 2014. See link below.
 * https://stackoverflow.com/questions/15271271/android-callback-asynctask-to-fragmentnot-activity
 *
 * This file handles the connection to DatabaseManager, a class that in turn connects to the database
 * by sending and retrieving information. Callback has two methods, one that is called when the user wants to make
 * a post or patch request to the database and one that is called when the user wants to make a get or delete request.
 * Callback also extends Fragment which takes care of the return message that is delivered from the database via DatabaseManager.
 *
 *
 *
 * Created by elsabergman on 2017-04-27.
 */

/*This class handles the connection to the DatabaseManager class, which creates a HttpURLConnection. This class extends
fragments in order to enable a callback from the DatabaseManager class */

public class Callback extends Fragment {

    String status;
    String response;
    /* method called from the class wishing to connect to the database. */
    public String  execution_Post( String url, String token, String type, String JSON) throws ExecutionException, InterruptedException {

        /*create new databaseManager object, in order to enable a connection */
        DatabaseManager databaseManager = new DatabaseManager(new FragmentCallback() {
            /*method to be executed when databaseManager is finished */
            @Override
            public void onTaskDone(String result) {
                if (result == "true") {

                    status = "true";

                } else {
                    status = "false";

                }

            }
        });
        /*call databaseManager class */
        databaseManager.execute(url, token, type, JSON).get();
        return databaseManager.status; //return true or false depending on if request was successful or not
    }

    public String execution_Get(String url, String token, String type, String Json) throws ExecutionException, InterruptedException {
        /*create new databaseManager object, in order to enable a connection */
        DatabaseManager databaseManager = new DatabaseManager(new FragmentCallback() {
            /*method to be executed when databaseManager is finished */
            @Override
            public void onTaskDone(String result) {

                response = result;

            }
        });
        /*call databaseManager class */
        databaseManager.execute(url, token, type, Json).get();
        return databaseManager.response; //return true or false depending on if request was successful or not
    }


    /*Fragment interface which calls onTaskDone when databaseManager is finished.
This interface is called from DatabaseManager */
    public  interface FragmentCallback {
        public void onTaskDone(String result);
    }


}