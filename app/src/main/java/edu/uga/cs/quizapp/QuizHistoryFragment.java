package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle/display quiz history
 */
public class QuizHistoryFragment extends Fragment {

    private StateCapitalsData stateCapitalsData = null;
    private static final String TAG = "QuizHistoryFragment";

    private List<StateCapitalPOJO> stateCapitalList;
    private List<QuizResultPOJO> quizResults;

    private RecyclerView recyclerView;

    private ViewQuizHistory quizHistory;

    public QuizHistoryFragment() {
        // Required empty public constructor
    }

    public static QuizHistoryFragment newInstance() {
        QuizHistoryFragment fragment = new QuizHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the search menu population.
        // When the parameter of this method is true, Android will call onCreateOptionsMenu on
        // this fragment, when the options menu is being built for the hosting activity.
        setHasOptionsMenu(true);
    }

    /**
     * overriding onCreateView and inflating with quiz_history.xml
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.quiz_history, container, false);
    }

    /**
     * overriding onViewCreated to load/initialize objects.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        quizResults = new ArrayList<QuizResultPOJO>();
        stateCapitalsData = new StateCapitalsData(getActivity());
        stateCapitalsData.open();
        new StateCapitalReader().execute();
    }

    /**
     * Inner class to retrieve all quizes
     */
    private class StateCapitalReader extends AsyncTask<Void, List<QuizResultPOJO>> {

        @Override
        protected List<QuizResultPOJO> doInBackground(Void... arguments) {
            List<QuizResultPOJO> quizResults = stateCapitalsData.retrieveAllQuizzes();
            Log.d( TAG, "StateCapitalReader: State capitals retrieved: " + quizResults.size() );
            return quizResults;
        }

        @Override
        protected void onPostExecute(List<QuizResultPOJO> quizResultPOJOs) {
            quizResults.addAll(quizResultPOJOs);
            Log.d( TAG, "StateCapitalReader: stateCapitals.size(): " + quizResults.size() );
            quizHistory = new ViewQuizHistory(getActivity(), quizResultPOJOs);
            recyclerView.setAdapter(quizHistory);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (stateCapitalsData != null && !stateCapitalsData.isDBOpen()) {
            stateCapitalsData.open();
            Log.d( TAG, "QuizHistoryFragment.onResume(): opening DB" );
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (stateCapitalsData != null) {
            stateCapitalsData.close();
            Log.d( TAG, "QuizHistoryFragment.onPause(): closing DB" );
        }
    }

}
