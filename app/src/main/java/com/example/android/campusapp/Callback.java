package com.example.android.campusapp;

import android.support.v4.app.Fragment;

import java.util.concurrent.ExecutionException;

/**
 * Created by elsabergman on 2017-04-27.
 */

/*This class handles the connection to the DatabaseManager class, which creates a HttpURLConnection. This class extends
fragments in order to enable a callback from the DatabaseManager class */

public class Callback extends Fragment {

    boolean status;
    /* method called from the class wishing to connect to the database. */
    public boolean  execution(String JSON, String url, String token, String type) throws ExecutionException, InterruptedException {
        /*create new databaseManager object, in order to enable a connection */
        DatabaseManager databaseManager = new DatabaseManager(new FragmentCallback() {
            /*method to be executed when databaseManager is finished */
            @Override
            public void onTaskDone(boolean result) {
                if (result) {

                    status = true;

                } else {
                    status = false;

                }

            }
        });
        /*call databaseManager class */
        databaseManager.execute(JSON, url, token, type).get();
        return databaseManager.status; //return true or false depending on if request was successful or not
    }

    /*Fragment interface which calls onTaskDone when databaseManager is finished.
    This interface is called from DatabaseManager */
    public  interface FragmentCallback {
        public void onTaskDone(boolean result);
    }


}