/**
 * Created by Jasmine L on 1/3/18.
 */

package com.jasminelawrence.uncovered;

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
 * Helper methods related to requesting and receiving Article data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s
        List<Article> Articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Article}s
        return Articles;
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

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */

    private static List<Article> extractFeatureFromJson(String ArticlesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(ArticlesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Articles to
        List<Article> articleList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(ArticlesJSON);

            // Extract the JSONArray associated with the key called "reponse",
            // which represents a list of items (or Articles).
            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray articlesArray = response.getJSONArray("results");

            // For each Article in the ArticleArray, create an {@link Article} object
            for (int i = 0; i < articlesArray.length(); i++) {

               // Get a single Article at position i within the list of Articles
                JSONObject currentArticle = articlesArray.getJSONObject(i);

                // For a given Article, extract the JSONObject associated with the
                // key called "results", which represents a list of all properties
                // for that Article.

                // Extract the value for the key called "place"
                String title = currentArticle.getString("webTitle");

                // Extract the value for the key called "place"
                String section = currentArticle.getString("sectionName");
                //TODO:format date

                // Extract the value for the key called "place"
                String published = currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "place"
                String url = currentArticle.getString("webUrl");


                // Create a new {@link Article} object with the title, author, date published,
                // and url from the JSON response.
                Article article = new Article( title, section,published,url);

                //TODO: replace with correct field for author. This is not always available


                // Extract the value for the key called "place"
                JSONArray tags = currentArticle.getJSONArray("tags");

                // Extract the value for the key called "place"

                if(tags != null && tags.length() > 0 ){

                    // Extract the value for the key called "place"
                    String author =tags.getJSONObject(0).getString("webTitle");

                    // Create a new {@link Article} object with the title, author, date published,
                    // and url from the JSON response.
                    article = new Article( title, section,author,published,url);

                }

                // Add the new {@link Article} to the list of Articles.
                articleList.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Article JSON results", e);
        }

        // Return the list of Articles
        return articleList;
    }




}