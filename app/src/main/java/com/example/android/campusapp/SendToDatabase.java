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

class SendToDatabase extends AsyncTask<String, String, String> {
    static JSONObject JSON_token_key;
    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        /*JSON data from class that calls SendToDatabase*/
        String JsonDATA = (String) params[0];
        /*The URL that we connect to*/
        String urlen = (String) params[1];
        /*the token connected to the user */
        String token = (String) params[2];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        System.out.println(token);

        /*try connect to the URL*/
        try {
            URL url = new URL(urlen);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writer
            /*Set headers needed to set up output stream*/
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "JWT " + token);


//set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));

            writer.write(JsonDATA);
            writer.flush();
            int code = urlConnection.getResponseCode();
            writer.close();

            /*Input stream*/
            InputStream inputStream = null;

            try {
                 inputStream = urlConnection.getInputStream();
            }
            catch (Exception e){
                e.printStackTrace();
            }
//input stream

            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;

            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();

//response data
            Log.i(TAG,JsonResponse);

            //send to post execute

            /*make JsonResponse an actual Json string, as of now it only looks like a Json string
             but it actually is a regular String*/

            System.out.println(JsonResponse);
            /*call to method in the class that needs to know that everything went smoothly!*/
            return JsonResponse; //this is the response from the Database!



        } catch (IOException e) {
            e.printStackTrace();
           /* } catch (JSONException e) {
                e.printStackTrace();

*/

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
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



}


