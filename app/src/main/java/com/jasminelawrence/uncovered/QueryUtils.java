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

    private static final String testJSON = "{\n" +
            "  \"response\": {\n" +
            "    \"status\": \"ok\",\n" +
            "    \"userTier\": \"developer\",\n" +
            "    \"total\": 24,\n" +
            "    \"startIndex\": 1,\n" +
            "    \"pageSize\": 20,\n" +
            "    \"currentPage\": 1,\n" +
            "    \"pages\": 2,\n" +
            "    \"orderBy\": \"relevance\",\n" +
            "    \"results\": [\n" +
            "      {\n" +
            "        \"id\": \"us-news\\/2017\\/apr\\/24\\/building-affordable-housing-is-all-but-impossible-in-californias-bay-area\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"us-news\",\n" +
            "        \"sectionName\": \"US news\",\n" +
            "        \"webPublicationDate\": \"2017-04-24T15:18:10Z\",\n" +
            "        \"webTitle\": \"Building affordable housing is all but impossible in California\\u2019s Bay Area | Letter\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/us-news\\/2017\\/apr\\/24\\/building-affordable-housing-is-all-but-impossible-in-californias-bay-area\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/us-news\\/2017\\/apr\\/24\\/building-affordable-housing-is-all-but-impossible-in-californias-bay-area\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"society\\/2018\\/jan\\/03\\/windsor-council-calls-removal-homeless-people-before-royal-wedding\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"society\",\n" +
            "        \"sectionName\": \"Society\",\n" +
            "        \"webPublicationDate\": \"2018-01-03T18:12:48Z\",\n" +
            "        \"webTitle\": \"Windsor council leader calls for removal of homeless before royal wedding\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/society\\/2018\\/jan\\/03\\/windsor-council-calls-removal-homeless-people-before-royal-wedding\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/society\\/2018\\/jan\\/03\\/windsor-council-calls-removal-homeless-people-before-royal-wedding\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"society\\/2017\\/dec\\/19\\/churches-offering-shelter-from-the-storm\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"society\",\n" +
            "        \"sectionName\": \"Society\",\n" +
            "        \"webPublicationDate\": \"2017-12-19T18:23:04Z\",\n" +
            "        \"webTitle\": \"Churches offering shelter from the storm | Brief letters\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/society\\/2017\\/dec\\/19\\/churches-offering-shelter-from-the-storm\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/society\\/2017\\/dec\\/19\\/churches-offering-shelter-from-the-storm\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"housing-network\\/2017\\/aug\\/17\\/waitrose-blameless-evictions-section-21-housing-homelessness\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"housing-network\",\n" +
            "        \"sectionName\": \"Housing Network\",\n" +
            "        \"webPublicationDate\": \"2017-08-17T06:32:01Z\",\n" +
            "        \"webTitle\": \"A new Waitrose means more evictions, what should be done? | Dan Wilson Craw\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/housing-network\\/2017\\/aug\\/17\\/waitrose-blameless-evictions-section-21-housing-homelessness\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/housing-network\\/2017\\/aug\\/17\\/waitrose-blameless-evictions-section-21-housing-homelessness\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"us-news\\/2017\\/apr\\/15\\/eugene-oregon-dog-ban-homeless\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"us-news\",\n" +
            "        \"sectionName\": \"US news\",\n" +
            "        \"webPublicationDate\": \"2017-04-15T10:00:22Z\",\n" +
            "        \"webTitle\": \"Oregon city's dog ban condemned as crackdown on homeless people\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/us-news\\/2017\\/apr\\/15\\/eugene-oregon-dog-ban-homeless\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/us-news\\/2017\\/apr\\/15\\/eugene-oregon-dog-ban-homeless\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"world\\/2017\\/may\\/27\\/canada-homelessness-housing-data-too-big-small-homes\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"world\",\n" +
            "        \"sectionName\": \"World news\",\n" +
            "        \"webPublicationDate\": \"2017-05-27T10:30:25Z\",\n" +
            "        \"webTitle\": \"Canada's 'us and them cities': data shows most homes are too small \\u2013 or too big\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/world\\/2017\\/may\\/27\\/canada-homelessness-housing-data-too-big-small-homes\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/world\\/2017\\/may\\/27\\/canada-homelessness-housing-data-too-big-small-homes\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"us-news\\/2016\\/jul\\/11\\/san-diego-homeless-killing-spree-suspect-released\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"us-news\",\n" +
            "        \"sectionName\": \"US news\",\n" +
            "        \"webPublicationDate\": \"2016-07-11T23:39:06Z\",\n" +
            "        \"webTitle\": \"San Diego police still searching for suspected serial killer of homeless\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/us-news\\/2016\\/jul\\/11\\/san-diego-homeless-killing-spree-suspect-released\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/us-news\\/2016\\/jul\\/11\\/san-diego-homeless-killing-spree-suspect-released\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"politics\\/2017\\/may\\/31\\/cambridge-the-housing-crisis-is-at-breaking-point\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"politics\",\n" +
            "        \"sectionName\": \"Politics\",\n" +
            "        \"webPublicationDate\": \"2017-05-31T11:46:20Z\",\n" +
            "        \"webTitle\": \"Cambridge: 'The housing crisis is at breaking point'\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/politics\\/2017\\/may\\/31\\/cambridge-the-housing-crisis-is-at-breaking-point\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/politics\\/2017\\/may\\/31\\/cambridge-the-housing-crisis-is-at-breaking-point\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"society\\/2017\\/mar\\/06\\/housing-benefit-cuts-young-people-homelessness-landlords\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"society\",\n" +
            "        \"sectionName\": \"Society\",\n" +
            "        \"webPublicationDate\": \"2017-03-06T15:07:16Z\",\n" +
            "        \"webTitle\": \"Housing benefit cuts 'put young people at risk of homelessness'\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/society\\/2017\\/mar\\/06\\/housing-benefit-cuts-young-people-homelessness-landlords\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/society\\/2017\\/mar\\/06\\/housing-benefit-cuts-young-people-homelessness-landlords\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pillarName\": \"News\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"commentisfree\\/2016\\/mar\\/07\\/homelessness-is-a-national-emergency-the-crisis-demands-federal-leadership\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"commentisfree\",\n" +
            "        \"sectionName\": \"Opinion\",\n" +
            "        \"webPublicationDate\": \"2016-03-07T16:26:57Z\",\n" +
            "        \"webTitle\": \"Homelessness is a national emergency \\u2013 the crisis demands federal leadership | Edward B Murray\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/commentisfree\\/2016\\/mar\\/07\\/homelessness-is-a-national-emergency-the-crisis-demands-federal-leadership\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/commentisfree\\/2016\\/mar\\/07\\/homelessness-is-a-national-emergency-the-crisis-demands-federal-leadership\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/opinion\",\n" +
            "        \"pillarName\": \"Opinion\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"us-news\\/2016\\/jan\\/31\\/sleeping-rough-in-seattle-homeless-crisis-exposes-dark-side-of-affluent-microsoft-city\",\n" +
            "        \"type\": \"article\",\n" +
            "        \"sectionId\": \"us-news\",\n" +
            "        \"sectionName\": \"US news\",\n" +
            "        \"webPublicationDate\": \"2016-01-31T00:02:12Z\",\n" +
            "        \"webTitle\": \"Sleeping rough in Seattle: homeless crisis exposes dark side of hi-tech city\",\n" +
            "        \"webUrl\": \"https:\\/\\/www.theguardian.com\\/us-news\\/2016\\/jan\\/31\\/sleeping-rough-in-seattle-homeless-crisis-exposes-dark-side-of-affluent-microsoft-city\",\n" +
            "        \"apiUrl\": \"https:\\/\\/content.guardianapis.com\\/us-news\\/2016\\/jan\\/31\\/sleeping-rough-in-seattle-homeless-crisis-exposes-dark-side-of-affluent-microsoft-city\",\n" +
            "        \"isHosted\": false,\n" +
            "        \"pillarId\": \"pillar\\/news\",\n" +
            "        \"pill";


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
        //TODO: test partsing by changing this to testJSON string from "jsonResponse"
        List<Article> Articles = extractFeatureFromJson(testJSON);

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

    //TODO: Update JSON parsing for the guardian website!
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

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of items (or Articles).
            JSONArray articlesArray = baseJsonResponse.getJSONArray("results");

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

                // Extract the value for the key called "place"
                String published = currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "place"
                String url = currentArticle.getString("webUrl");

                //TODO: replace with correct field for author. This is not always available
                // Extract the value for the key called "place"
                String author = currentArticle.getString("type");


                // Create a new {@link Article} object with the title, author, date published,
                // and url from the JSON response.
                Article article = new Article( title, section, author, published,url);


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