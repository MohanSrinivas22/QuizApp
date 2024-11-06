package edu.uga.cs.quizapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreen extends Fragment {

    private Button newQuiz;
    private Button quizHistory;

    private TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainScreen() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainScreen newInstance() {
        MainScreen fragment = new MainScreen();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * overriding onCreateView and inflating with fragment_main_screen.xml
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    /**
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        newQuiz = view.findViewById(R.id.newQuiz);
        quizHistory = view.findViewById(R.id.quizHistory);
        textView = view.findViewById(R.id.appInstructions);
        String text = getResources().getString(R.string.splash_screen_text);
        textView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));

        /**
         * setOnClickListener to redirect to new Quiz upon newQuiz Button click
         */
        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewQuizActivity.class);
                startActivity(intent);
            }
        });

        /**
         * setOnClickListener to redirect to quiz history upon quizHistory Button click
         */
        quizHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new QuizHistoryFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack("Splash Screen").commit();
            }
        });
    }
}