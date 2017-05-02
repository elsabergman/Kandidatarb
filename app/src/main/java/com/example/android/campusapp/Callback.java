package com.example.android.campusapp;

import android.support.v4.app.Fragment;

import java.util.concurrent.ExecutionException;

/**
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
                System.out.println(response);

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