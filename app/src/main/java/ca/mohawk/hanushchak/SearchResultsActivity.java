package ca.mohawk.hanushchak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SearchResultsActivity extends AppCompatActivity {

    public static final String TAG = "@@ SearchResActivity @@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Log.d(TAG, "onCreate");

        Intent intent = getIntent();

        Bundle bundle = new Bundle();

        if (intent.getStringExtra("title") != null) {
            bundle.putString("title", intent.getStringExtra("title"));
        }

        if (intent.getStringExtra("year") != null) {
            bundle.putString("year", intent.getStringExtra("year"));
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        SearchResultsFragment1 fragment1 = new SearchResultsFragment1();
        fragment1.setArguments(bundle);

        SearchResultsFragment2 fragment2 = new SearchResultsFragment2();
        fragment2.setArguments(bundle);

        fragmentTransaction.replace(R.id.searchResultsFrame1, fragment1);
        fragmentTransaction.replace(R.id.searchResultsFrame2, fragment2);

        fragmentTransaction.commit();



    }
}