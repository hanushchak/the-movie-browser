package ca.mohawk.hanushchak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.mohawk.hanushchak.model.Movie;

public class FavoritesActivity extends AppCompatActivity {

    public static final String TAG = "@@ FavoritesActivity @@";

    SQLHelper dbHelper = new SQLHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Log.d(TAG, "onCreate()");
        // Queries the DB to populate Favorites list
        queryDB();
    }

    /**
     * Re-query and re-populates the list on resume
     * (to refresh the list when the user removes the movie and hits back button)
     */
    @Override
    public void onResume(){
        super.onResume();

        Log.d(TAG, "onResume()");
        // Queries the DB to populate Favorites list
        queryDB();
    }

    /**
     * Queries the DB and populates the Favorites list
     */
    public void queryDB() {
        Log.d(TAG, "queryDB()");
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { SQLHelper.ID, SQLHelper.IMDBID, SQLHelper.TITLE, SQLHelper.YEAR };

        Cursor c = db.query(SQLHelper.TABLENAME, projection, null,
                null, null, null, null);


        if (c != null) {
            // Loop through our returned results from the start
            while (c.moveToNext()) {

                String title = c.getString(
                        c.getColumnIndex(SQLHelper.TITLE));
                String year = c.getString(
                        c.getColumnIndex(SQLHelper.YEAR));
                String imdbId = c.getString(
                        c.getColumnIndex(SQLHelper.IMDBID));

                Movie movieToAdd = new Movie();
                movieToAdd.imdbID = imdbId;
                movieToAdd.Title = title;
                movieToAdd.Year = year;

                movies.add(movieToAdd);
            }
            c.close();
        }

        // Show results
        ArrayAdapter<Movie> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies);
        ListView wishlistListView = findViewById(R.id.wishlistListView);
        wishlistListView.setAdapter(adapter);

        wishlistListView.setOnItemClickListener(this::onItemClick);

        TextView noResults = findViewById(R.id.textViewNoFavorites);
        if(!movies.isEmpty()) {
            noResults.setText("");
        }
        else{
            noResults.setText(R.string.no_favorites);
        }

    }

    /**
     * Handles clicks on movies in the list
     * Creates intent and opens Details activity for the selected movie
     * @param adapterView current list's adapter
     * @param view current view
     * @param i index of the array
     * @param l not used
     */
    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d(TAG, "onItemClick()");
        String imdbID = ((Movie)adapterView.getItemAtPosition(i)).imdbID;
        String title = ((Movie)adapterView.getItemAtPosition(i)).Title;
        String year = ((Movie)adapterView.getItemAtPosition(i)).Year;

        Intent switchToDetailsActivity = new Intent(this, DetailsActivity.class);
        switchToDetailsActivity.putExtra("imdbid", imdbID);
        switchToDetailsActivity.putExtra("title", title);
        switchToDetailsActivity.putExtra("year", year);
        startActivity(switchToDetailsActivity);

    }
}