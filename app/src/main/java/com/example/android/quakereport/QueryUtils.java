package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl){

        Log.v("loaderManagerInitLoader", "fetchEarthquakeData");

        // create URL  object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant field from the JSON response and create a list of {@link Earthquake}s
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

    /**
     *
     * @param stringUrl
     * @return new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     *  Make an HTTP request to the given URL and return a String ad the response
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest (URL url) throws IOException {
        String jsonResponse = "";

        // if the URL is null, the return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.connect();

            // If the request eas successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Error response code: " , String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return  jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractFeatureFromJson(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Crate a JSONObject from the jsonResponse string
            JSONObject root = new JSONObject(jsonResponse);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features
            JSONArray features = root.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < features.length(); i++) {

                // Get a single earthquake at position i within the list of earthquake
                JSONObject featureItem = features.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake
                JSONObject properties = featureItem.getJSONObject("properties");

                // Extract the value for the key called "mag"
                Double magnitude = properties.getDouble("mag");

                // Extract the value for the key called "place"
                String location = properties.getString("place");

                // Extract the value for the key called "time"
                Long timeMilliSecond = properties.getLong("time");

                // Extract the value for the key called "url"
                String url = properties.getString("url");

                // create a new {@link Earthquake} abject with the magnitude, location, time
                // and url from the JSON response and add to the list of earthquake
                earthquakes.add(new Earthquake(magnitude, location, timeMilliSecond, url));


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}