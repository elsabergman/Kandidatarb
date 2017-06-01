package com.example.android.campusapp;

/** Name: GetTokenLogin.java
 * Author: Elsa Bergman
 *Connects to: login.java
 *
 *The GetTokenLogin class sets up a HTTP connection and sends data on an output stream. This class can also receive
 * data on an input stream. If the database sends back a response code that is not 2XX (marking that everything worked the way it should),
 * the return value will be "error". This value is passed back to the user to let them know that their request was not executed in the
 * intended way. Otherwise, the return value will be "access granted", stating that the login request was successful. If the request is
 * successful, the user will also recieve a token and fetch their group (student or organization) and send this information back to
 * login.java where login access is granted.
 */

        import android.os.AsyncTask;
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
class GetTokenLogin extends AsyncTask<String, String, String> {
    private static login Login;

    public GetTokenLogin(login Login) {
        this.Login = Login;
    }

    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String JsonDATA = (String) params[0];
        String urlen = (String) params[1];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        /*--- try connecting to URL ---*/
        try {
            URL url = new URL(urlen);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            /*---set headers and method---*/
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));

            /*---Json data that the user wants to send to the database---*/
            writer.write(JsonDATA);
            writer.flush();
            writer.close();

            /*--get Response code from Database, either 2XX Ok or some type of Error --*/
            int code = urlConnection.getResponseCode();
                /*--if Error code, send this info to Login Class, which won't not grant access to log in--*/
            if ((Character.toLowerCase(String.valueOf(code).charAt(0)) != '2')) {
                Login.runOnUiThread(new Runnable() { //To make compatible with AsyncTask
                    public void run() {
                        /* lets login.java know that the user could not fetch a token and a group and will not be logged in */
                        Login.LoginAccessGranted("error", "no token given", "no group given");

                    }
                });
            }
            /*--Input Stream, response from Database---*/
            InputStream inputStream  = null;
            try {
                inputStream = urlConnection.getInputStream();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
            JsonResponse = buffer.toString(); //Json Response from Database, actually String format
            Log.i(TAG,JsonResponse); //Log response data

            /*make JsonResponse an actual Json string, as of now it only looks like a Json string
                but it actually is a regular String*/
            JSONObject JSON_response = new JSONObject(JsonResponse);
            String my_token = JSON_response.getString("token"); // the token that the user will need to access pages in the system
            String my_group = JSON_response.getString("groups"); //student or organization, depending on what type of user this person is.

            Login.LoginAccessGranted("access granted", my_token, my_group); //Send ok to Login Class, which will grant login access
            return JsonResponse;

             /*--catch errors --*/
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        finally {
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
