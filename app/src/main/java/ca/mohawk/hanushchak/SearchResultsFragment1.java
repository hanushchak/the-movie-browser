package ca.mohawk.hanushchak;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SearchResultsFragment1 extends Fragment {

    public static final String TAG = "@@ ResultsFragment1 @@";

    public SearchResultsFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View v = inflater.inflate(R.layout.fragment_search_results, container, false);

        assert getArguments() != null;
        String title = getArguments().getString("title");
        String year = getArguments().getString("year");

        String searchTitle = "Searching for <b>" + title + "</b>";

        if(year != null && !year.equals("")) {
            searchTitle += " in <b>" + year + "</b>";
        }

        TextView searchTitleTextView = v.findViewById(R.id.searchResultsTitle);
        searchTitleTextView.setText(Html.fromHtml(searchTitle));

        Button newSearchButton = v.findViewById(R.id.newSearchButton);
        newSearchButton.setOnClickListener(this::onNewSearchClick);
        return v;
    }


    public void onNewSearchClick(View view) {
        Log.d(TAG, "onNewSearchClick");
        NewSearchDialogFragment newSearchDialog = new NewSearchDialogFragment();
        assert getFragmentManager() != null;
        newSearchDialog.show(getFragmentManager(), null);
    }
}