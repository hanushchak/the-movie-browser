package ca.mohawk.hanushchak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "@@ DetailsActivity @@";

    public ImageView posterImageView;
    public Button favoritesButton;
    public TextView titleTextView;
    public TextView yearTextView;
    public TextView genreTextView;
    public TextView runtimeTextView;
    public TextView directorTextView;
    public TextView plotTextView;

    SQLHelper dbHelper = new SQLHelper(this);

    public static String imdbId;
    public static String title;
    public static String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posterImageView = findViewById(R.id.moviePosterImageView);
        favoritesButton = findViewById(R.id.addToFavoritesButton);
        titleTextView = findViewById(R.id.movieTitleTextView);
        yearTextView = findViewById(R.id.yearTextView);
        genreTextView = findViewById(R.id.genreTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        directorTextView = findViewById(R.id.actorsTextView);
        plotTextView = findViewById(R.id.plotTextTextView);

        posterImageView.setImageBitmap(null);
        titleTextView.setText("");
        yearTextView.setText("");
        genreTextView.setText("");
        runtimeTextView.setText("");
        directorTextView.setText("");
        plotTextView.setText("");

        Intent intent = getIntent();
        imdbId = intent.getStringExtra("imdbid");
        title = intent.getStringExtra("title");
        year = intent.getStringExtra("year");

        if (imdbId != null) {
            startDownload(intent.getStringExtra("imdbid"));
            queryFavorites(imdbId);
            favoritesButton.setOnClickListener(this::onFavoritesClick);
        }
    }

    /**
     * Handles clicks on My Favorites button
     * If the movie is not added to favorites, adds it
     * If the current movie is already added to Favorites, removes it
     * @param view current view
     */
    public void onFavoritesClick(View view) {
        boolean isInFavorites = queryFavorites(imdbId);
        if(isInFavorites) {
            removeFromFavorites(imdbId);
        }else{
            addToFavorites(imdbId);
        }
    }

    /**
     * Queries the DB for the current movie. If it's in the db, returns true, else false
     * Changes the text of the button depending on whether the movie is in DB
     * @param imdbId IMDB ID for the movie
     * @return true if in DB, false otherwise
     */
    public Boolean queryFavorites(String imdbId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] tableColumns = new String[] { "imdb_id" };
        String whereClause = "imdb_id = ?";
        String[] whereArgs = new String[] { imdbId };

        Cursor c = db.query("imdb_wishlist", tableColumns,
                whereClause, whereArgs, null, null, null);

        if (c.moveToFirst()) {
            Log.d(TAG, "idx " + c.getString(0));
            favoritesButton.setText(R.string.remove_from_favorites);
            favoritesButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
            c.close();
            return true;
        }
        favoritesButton.setText(R.string.add_to_favorites);
        favoritesButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
        c.close();
        return false;
    }

    /**
     * Adds the movie to the DB (favorites table)
     * @param imdbId IMDB ID for the movie
     */
    public void addToFavorites(String imdbId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(SQLHelper.IMDBID, imdbId);
        contentValue.put(SQLHelper.TITLE, title);
        contentValue.put(SQLHelper.YEAR, year);

        long i = db.insert(SQLHelper.TABLENAME, null, contentValue);

        queryFavorites(imdbId);
        Log.d(TAG, "insert result " + i);
    }

    /**
     * Removes the movies from the DB (favorites table)
     * @param imdbId  IMDB ID for the movie
     */
    public void removeFromFavorites(String imdbId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        long i = db.delete(SQLHelper.TABLENAME, SQLHelper.IMDBID + "='" + imdbId + "'", null);
        queryFavorites(imdbId);
        Log.d(TAG, "remove result " + i);
    }

    /**
     * This method will start async download of the movie details
     * @param imdbId IMDB ID for the movie
     */
    public void startDownload(String imdbId) {
        GetMovieDetailsAsyncTask dl = new GetMovieDetailsAsyncTask();

        String uri = "http://www.omdbapi.com/";
        String api_key = "acccaf1f";

        uri += "?apikey=" + api_key + "&i=" + imdbId;

        Log.d(TAG, "startDownload: " + uri);

        dl.execute(uri);
    }

    public class GetMovieDetailsAsyncTask extends AsyncTask<String, Void, String> {
        public static final String TAG = "@@ GetMovieAsync @@";

        /**
         * Downloads data in the background
         * @param params api url at index 0
         * @return download results
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
         * When the data is downloaded, handles the results
         * @param result result of async download
         */
        protected void onPostExecute(String result) {
            Movie movie = null;
            if (result == null) {
                Log.d(TAG, "No results :(");
            } else {
                Log.d(TAG, result);
                Gson gson = new Gson();
                movie = gson.fromJson(result, Movie.class);
            }
            if (movie != null) {
                String genre = "<b>Genre: </b>" + movie.Genre;
                String runtime = "<b>Runtime: </b>" + movie.Runtime;
                String actors = "<b>Actors: </b>" + movie.Actors;

                posterImageView.setImageBitmap(getBitmapFromURL(movie.Poster));
                titleTextView.setText(movie.Title);
                yearTextView.setText(String.format("(%s)", movie.Year));

                genreTextView.setText(Html.fromHtml(genre));
                runtimeTextView.setText(Html.fromHtml(runtime));
                directorTextView.setText(Html.fromHtml(actors));
                plotTextView.setText(movie.Plot);

            }
        }

    }

    /**
     * Downloads bitmap image from url
     * @param src download source (url)
     * @return bitmap image if successful, null if unsuccessful
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}