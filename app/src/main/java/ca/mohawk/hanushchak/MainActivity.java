// I, Maksym Hanushchak, 000776919 certify that this material is my original work.
// No other person's work has been used without due acknowledgement.
// https://youtu.be/CU6__jV6UX8
package ca.mohawk.hanushchak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    final static String tag = "@@ MainActivity @@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(tag,"onCreate()");

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDate = findViewById(R.id.editTextDate);

        editTextTitle.setText("");
        editTextDate.setText("");
    }

    /**
     * Validates the search request in the EditText fields
     * Displays errors if invalid
     * Creates an intent to and starts searchResults activity if valid
     * @param view current view
     */
    public void onClickSearch(View view) {
        Log.d(tag,"onClickSearch()");

        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDate = findViewById(R.id.editTextDate);

        String title = editTextTitle.getText().toString();
        String year = editTextDate.getText().toString();

        Log.d(tag,"onClickSearch() title: " + title);
        Log.d(tag,"onClickSearch() year: " + year);

        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("This field is required!");
        }
        else {
            if(!TextUtils.isEmpty(year) && year.length() != 4){
                editTextDate.setError("Please enter 4 digits or leave empty");
            } else {
                Intent switchToSearchResultsActivity = new Intent(MainActivity.this, SearchResultsActivity.class);
                switchToSearchResultsActivity.putExtra("title", title);
                switchToSearchResultsActivity.putExtra("year", year);
                startActivity(switchToSearchResultsActivity);
            }
        }
    }

    /**
     * Handles the click on the My Favorites button
     * Starts FavoritesActivity
     * @param view current view
     */
    public void onClickMyFavorites(View view) {
        Log.d(tag,"onClickMyFavorites()");

        Intent switchToFavoritesActivity = new Intent(MainActivity.this, FavoritesActivity.class);
        startActivity(switchToFavoritesActivity);
    }
}