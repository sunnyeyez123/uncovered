package com.jasminelawrence.uncovered;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * Constant value for the Article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int Article_LOADER_ID = 1;

    ProgressBar loadingIndicator;
    private ArticleAdapter mAdapter;

    TextView results;

    String query = "https://content.guardianapis.com/search?q=homelessANDsan%20francisco&show-fields=headline,short-url&order-by=relevance&format=json&api-key=bd330cde-7007-47b8-9a51-7a4ad02d1583&page-size=5&show-references=author&tag=society/homelessness&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);

        results = (TextView) findViewById(R.id.empty_view);

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        articleListView.setEmptyView(results);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentarticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentarticle.getUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });




// Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(Article_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            results.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ArticleLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> ArticleList) {
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);


        // Clear the adapter of previous Article data
        mAdapter.clear();

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (ArticleList != null && !ArticleList.isEmpty()) {
            mAdapter.addAll(ArticleList);

        } else {
            // Set empty state text to display "No Articles found."
            results.setText(getString(R.string.no_articles));

        }


    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Log.i("ArticleListActivity", "Searching again");
        startActivity(intent);

    }
}
