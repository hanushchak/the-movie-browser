package ca.mohawk.hanushchak;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class NewSearchDialogFragment extends DialogFragment implements View.OnClickListener {

    final static String tag = "@@ NewSearchDialog @@";

    public NewSearchDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag,"onCreateView()");

        View v = inflater.inflate(R.layout.fragment_new_search_dialog, container, false);

        Button buttonCancel = v.findViewById(R.id.buttonCancel);
        Button buttonOk = v.findViewById(R.id.buttonOk);

        buttonCancel.setOnClickListener(this);
        buttonOk.setOnClickListener(this);

        return v;
    }

    /**
     * Handles clicks on Search and Cancel buttons
     * Validates the search request, displays errors if invalid
     * Otherwise starts SearchResults activity
     * @param view current view
     */
    @Override
    public void onClick(View view) {

        Log.d(tag,"onClick()");

        if(view.getId() == R.id.buttonCancel) {
            dismiss();
        }
        if(view.getId() == R.id.buttonOk) {
            EditText editTextTitle = Objects.requireNonNull(getView()).findViewById(R.id.editTextDialogTitle);
            EditText editTextYear = getView().findViewById(R.id.editTextDialogYear);

            String title = editTextTitle.getText().toString();
            String year = editTextYear.getText().toString();

            if (TextUtils.isEmpty(title)) {
                editTextTitle.setError("This field is required!");
            }
            else {
                Intent switchToSearchResultsActivity = new Intent(getActivity(), SearchResultsActivity.class);
                switchToSearchResultsActivity.putExtra("title", title);
                switchToSearchResultsActivity.putExtra("year", year);
                startActivity(switchToSearchResultsActivity);
            }
        }
    }
}