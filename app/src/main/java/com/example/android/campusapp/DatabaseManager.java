package com.example.android.campusapp;

/**
 * Created by elsabergman on 2017-04-18.
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

        /*JSON data from class that calls DatabaseManager*/
       

        /*The URL that we connect to*/
        String urlen = (String) params[0];

        /*the token connected to the user */
        String token = (String) params[1];

        /*type of request, POST or GET */
        String type = (String) params[2];

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
            System.out.println(token);
            System.out.println(type);
            if(type == "POST" || type == "PATCH" || type == "PUT") {
                urlConnection.setDoOutput(true);
            /* Write message on stream */
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.flush();
                writer.close();

            }

            int code = urlConnection.getResponseCode(); //Response code from database telling front end if connection could be established
            System.out.println(code);

            /* If Response code is not a 2XX, we want to stop running the code here */
            if ((Character.toLowerCase(String.valueOf(code).charAt(0)) == '2')) {
                Log.v(TAG, "OK");
            } else {

                status = "false";
                return status;
            }

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
            /*read the content of the input stream */
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

