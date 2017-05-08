package com.example.android.campusapp;

/**
 * Created by elsabergman on 2017-04-28.
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
            urlConnection.setDoOutput(true); // is output buffer writer
                 /*---set headers and method---*/
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                /*---Json data---*/
            writer.write(JsonDATA);
            writer.flush();
            writer.close();
                /*--get Response code from Database, either 200 Ok or 400 Error --*/
            int code = urlConnection.getResponseCode(); /*response code, either 200 OK or 401 */
                /*--if Error code, send this info to Login Class, which won't not grant access to log in--*/
            if ((Character.toLowerCase(String.valueOf(code).charAt(0)) != '2')) {
                Login.runOnUiThread(new Runnable() { //To make compatible with AsyncTask
                    public void run() {
                        Login.LoginAccessGranted("error", "no token given", "no group given");
                    }
                });
            }
                /*--Input Stream, response from Database ---*/
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
            String my_token = JSON_response.getString("token");
            String my_group = JSON_response.getString("groups");

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
