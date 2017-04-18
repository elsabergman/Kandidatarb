package com.example.android.campusapp;

import android.os.AsyncTask;
import android.util.Log;


<<<<<<< HEAD
=======
import org.json.JSONException;
>>>>>>> origin/master
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;


import static android.content.ContentValues.TAG;


/**
 * Created by elsabergman on 2017-04-11.
 */

class SendToDatabase extends AsyncTask<String, String, String> {


    @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = (String) params[0];
        String url1 = (String) params[1];
        System.out.println(url1);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writer
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                System.out.println("här1");
                writer.write(JsonDATA);
                System.out.println("jsondata" + JsonDATA);

// json data

                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
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
<<<<<<< HEAD

=======
                System.out.println(buffer+ "buffer");
>>>>>>> origin/master
//response data
                Log.i(TAG,JsonResponse);

                //send to post execute
                System.out.println(inputLine + "inputLine");

                JSONObject JSON_token_key = new JSONObject(JsonResponse);
                String my_token = JSON_token_key.getString("token"); /* spara token för att gå vidare till andra sidor*/


                return JsonResponse;


<<<<<<< HEAD
=======



>>>>>>> origin/master
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();



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
