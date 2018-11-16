package com.example.newsapp;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank on 11/10/18 at 7:44 PM
 **/
class NewsUtils {


    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NewsUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NewsUtils (and an object instance of QueryUtils is not needed).
     */
    private NewsUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            // build up a list of News objects with the corresponding data.
            //Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject root = new JSONObject(newsJSON);


            JSONObject responseJSONObject = root.getJSONObject("response");
            //Extract "results" JSONArray
            JSONArray resultsJSONArray = responseJSONObject.getJSONArray("results");

            // Loop through each feature in the array

            for (int i = 0; i < resultsJSONArray.length(); i++) {
                // Get news JSONObject at position i
                JSONObject currentNews = resultsJSONArray.getJSONObject(i);

                // Extract  "webTitle" for  Title
                String webTitle = currentNews.optString("webTitle");
                // Extract  "sectionName" for  section name
                String sectionName = currentNews.optString("sectionName");

                // Extract  "webPublicationDate" for  date
                String webPublicationDate = currentNews.optString("webPublicationDate");


                String[] splitDate = webPublicationDate.split("T");

                // Extract date from "webPublicationDate"
                String date = splitDate[0];

                // Extract time from "webPublicationDate"
                StringBuilder time = new StringBuilder(splitDate[1]);


                StringBuilder timeWithoutLastCharacter = time.delete(5, 9);
                String timeWithoutLastFourCharacter = new String(timeWithoutLastCharacter);


                JSONArray tags = currentNews.optJSONArray("tags");
                String authorName = "";
                if (tags != null && tags.length() > 0) {
                    JSONObject authorProfile = (JSONObject) tags.get(0);
                    authorName = authorProfile.optString("webTitle");
                }
                // Extract the value for the key called "url"
                String url = currentNews.getString("webUrl");
//              Create News java object
                News newsObj = new News(webTitle, sectionName, date, url, timeWithoutLastFourCharacter, authorName);
//              Add news to list of news
                news.add(newsObj);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("NewsUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;

    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            /*
              If the request was successful (response code 200),
              then read the input stream and parse the response.
             */

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                /*
                 Closing the input stream could throw an IOException, which is why
                 the makeHttpRequest(URL url) method signature specifies than an IOException
                 could be thrown.
                 */

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the GUARDIAN dataset and return a list of {@link News} objects.
     */

    static List<News> fetchNewsData(String requestUrl) {
        Log.i(LOG_TAG, "fetchNewsData is called");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s

        // Return the list of {@link News}s
        return extractFeatureFromJson(jsonResponse);
    }

}
