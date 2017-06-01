package com.example.android.campusapp;

/**
 * Name: DatabaseManager.java
 * Author: Elsa Bergman
 * Parts of this code is written with the help of code found in the link below. Unfortunately the author's name remains unknown.
 * http://www.codexpedia.com/android/asynctask-and-httpurlconnection-sample-in-android/
 *
 * This class handles the connection to the database. A token, the Json data that is to be sent to the database, the url which is used
 * to set up the connection and the type of request the user wishes to make are passed as parameters. If no json data is to be sent
 * (i.e the user wants to make a get or delete request), a string saying "no Json data" will be passed since the same amount of
 * parameters have to be used at all times.
 *
 * The DatabaseManager class sets up a HTTP connection and sends data on an output stream. This class can also receive
 * data on an input stream. If the database sends back a response code that is not 2XX (marking that everything worked the way it should),
 * the return value will be "false". This value is passed back to the user to let them know that their request was not executed in the
 * intended way. Otherwise, the return value will be "true" or the actual json data that was requested, depending on what type of request
 * was being made.
 *
 */

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;


import static android.content.ContentValues.TAG;


/**
 * Created by elsabergman on 2017-04-11.
 */


class DatabaseManager extends AsyncTask<Object, Object, String> {

    /* ----instance variables ---- */

    static JSONObject JSON_token_key;
    String status;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String JsonResponse = null;
    private Callback.FragmentCallback mFragmentCallback; //our Fragment which connects to Callback
    String response;
    // private Callback.FragmentCallbackGET mFragmentCallbackGET;

    /*--- constructor ------*/
    public DatabaseManager(Callback.FragmentCallback fragmentCallback) {
        this.mFragmentCallback = fragmentCallback;

    }

    /*This method runs in the background and is called in Callback by databasemanager.execute */
    protected String doInBackground(Object... params) {
        String JsonResponse = null;
        String JsonDATA = null;

        /*The URL that we connect to*/
        String urlen = (String) params[0];

        /*the token connected to the user */
        String token = (String) params[1];

        /*type of request, POST or GET */
        String type = (String) params[2];

       /*JSON data from class that calls DatabaseManager*/
        JsonDATA = (String) params[3];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        /*try connect to the URL*/
        try {
            URL url = new URL(urlen);
            urlConnection = (HttpURLConnection) url.openConnection();

            /*Set headers needed to set up output stream*/
            urlConnection.setRequestMethod(type);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            /*From classes that do not have a token, a token = 0 will be passed to the database */
            if (token != "0") {
                urlConnection.setRequestProperty("Authorization", "JWT " + token);

            }

            if(type == "POST" || type == "PATCH" || type == "PUT") {

                urlConnection.setDoOutput(true);
            /* Write message on stream */
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.flush();
                writer.close();

            }

            int code = urlConnection.getResponseCode(); //Response code from database telling front end if connection could be established




            /*Input stream. The message that is returned from the database is being sent on this stream*/
            InputStream inputStream = null;

            try {
                inputStream = urlConnection.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }


            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            /*read the content of the input stream from the database*/
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;

            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();//response data

            Log.i(TAG, JsonResponse);
              /* If Response code is not a 2XX, we want to return false + error message */
            if ((Character.toLowerCase(String.valueOf(code).charAt(0)) == '2')) {
                Log.v(TAG, "OK");
            } else {

                status = "false";
                return status + " " + JsonResponse;
            }

            if (type == "POST" || type == "PATCH" || type == "PUT") {

                status = "true"; //the request was successful
                return status; //return message saying request was successful to Callback
            }
            if(type == "GET") {
                response = JsonResponse;
                return response;
            }


        } catch (IOException e) {
            e.printStackTrace();
        /*close connection */
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            /*close stream */
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    /*call on TaskDone in Callback when doInBackground is finished */
    @Override
    protected void onPostExecute(String result) {

        mFragmentCallback.onTaskDone(result);


    }


}

