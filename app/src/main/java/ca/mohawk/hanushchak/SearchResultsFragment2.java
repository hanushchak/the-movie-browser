package ca.mohawk.hanushchak;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import ca.mohawk.hanushchak.model.Movie;
import ca.mohawk.hanushchak.model.Search;

public class SearchResultsFragment2 extends Fragment {

    public static final String TAG = "@@ ResultsFragment2 @@";
    public ListView listView;
    public TextView noResultsTextView;

    public SearchResultsFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View v = inflater.inflate(R.layout.fragment_search_results2, container, false);
        assert getArguments() != null;
        String title = getArguments().getString("title");
        String year = getArguments().getString("year");

        listView = (ListView) v.findViewById(R.id.searchResultsListView);
        noResultsTextView = (TextView) v.findViewById(R.id.textViewNoResults);
        listView.setOnItemClickListener(this::onItemClick);

        startDownload(title, year); // Start async download

        return v;
    }

    /**
     * Handles clicks for individual movies in the list
     * Extracts imdbid, title and year from the adapter
     * Starts DetailsActivity
     *
     * @param adapterView adapter
     * @param view current view
     * @param i index
     * @param l not used
     */
    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String imdbID = ((Movie)adapterView.getItemAtPosition(i)).imdbID;
        String title = ((Movie)adapterView.getItemAtPosition(i)).Title;
        String year = ((Movie)adapterView.getItemAtPosition(i)).Year;

        Log.d(TAG, "Item at index: " + i);
        Log.d(TAG, "IMDB ID: " + imdbID);

        Intent switchToDetailsActivity = new Intent(getActivity(), DetailsActivity.class);
        switchToDetailsActivity.putExtra("imdbid", imdbID);
        switchToDetailsActivity.putExtra("title", title);
        switchToDetailsActivity.putExtra("year", year);
        startActivity(switchToDetailsActivity);

    }

    /**
     * Starts asyc download to populate ListView
     * @param title search request (title)
     * @param year search request (year)
     */
    public void startDownload(String title, String year) {
        GetMovieListAsyncTask dl = new GetMovieListAsyncTask();
        // Build call to Webservice
        String uri = "http://www.omdbapi.com/";
        String api_key = "acccaf1f";

        uri += "?apikey=" + api_key + "&s='" + title.replace(" ", "%20") + "'&y=" + year;

        Log.d(TAG, "startDownload: " + uri);
        dl.execute(uri);
    }

    public class GetMovieListAsyncTask extends AsyncTask<String, Void, String> {
        public static final String TAG = "@@GetMovieListAsync@@";

        /**
         * Download data in background to populate listview
         * @param params API URL at index 0
         * @return results (json as string)
         */
        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Starting Background Task");

            StringBuilder results = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                String line;

                // Open the Connection - GET is the default setRequestMethod
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Read the response
                int statusCode = conn.getResponseCode();
                if (statusCode == 200) {
                    InputStream inputStream = new BufferedInputStream(
                            conn.getInputStream());
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream,
                                    StandardCharsets.UTF_8));
                    while ((line = bufferedReader.readLine()) != null) {
                        results.append(line);
                    }
                }
                Log.d(TAG, "Data received = " + results.length());
                Log.d(TAG, "Response Code: " + statusCode);
            } catch (IOException ex) {
                Log.d(TAG, "Caught Exception: " + ex);
            }
            return results.toString();
        }

        /**
         * After async download handles the results
         * Populates listview with downloaded data
         * @param result downloaded results (json as string)
         */
        protected void onPostExecute(String result) {
            Search searchist = null;
            if (result == null) {
                Log.d(TAG, "No results");
            } else {
                Log.d(TAG, result);
                Gson gson = new Gson();
                searchist = gson.fromJson(result, Search.class);
            }
            if(getActivity() != null) {
                if (searchist != null && searchist.Search != null) {
                    noResultsTextView.setText("");
                    ArrayAdapter<Movie> adapter =
                            new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_list_item_1, searchist.Search);
                    listView.setAdapter(adapter);
                } else {
                    listView.setAdapter(null);
                    noResultsTextView.setText(getString(R.string.no_results));
                }
            }
        }
    }

}