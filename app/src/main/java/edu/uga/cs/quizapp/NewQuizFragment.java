package edu.uga.cs.quizapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewQuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewQuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private int mParam2;
    private Map<String, String> keyValueMapList = new HashMap<>();

    TextView question1;
    TextView score;
    TextView questionsLeft;
    Integer checkedId1;
    Integer checkedId2;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton trueButton;
    RadioButton falseButton;
    Long result = 0L;
    int finalResult;
    int quizId=0;

    private QuizResultPOJO quizResult = new QuizResultPOJO();
    private StateCapitalsData stateCapitalsData = null;
    private QuizResultPOJO updatedRecord;

    public NewQuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewQuizFragment.
     */


    public static NewQuizFragment newInstance(String list, int position, List<StateCapitalPOJO> stateCapitalPOJOS) {
        NewQuizFragment fragment = new NewQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, list);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            keyValueMapList = convertStringToMap(getArguments().getString(ARG_PARAM1));
            mParam2 = getArguments().getInt(ARG_PARAM2);
            ResultManager.setPosition(mParam2);
        }
    }

    /**
     * overriding onCreateView and inflating with fragment_new_quiz.xml
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_quiz, container, false);
    }

    /**
     * overriding onViewCreated and designing/updating quiz questions, score and answered questions.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        score = view.findViewById(R.id.score);
        questionsLeft = view.findViewById(R.id.questionsAnswered);

        question1 = view.findViewById(R.id.textView);
        radioGroup = view.findViewById(R.id.radioGroup1);
        radioButton1 = view.findViewById(R.id.radioButton);
        radioButton2 = view.findViewById(R.id.radioButton2);
        radioButton3 = view.findViewById(R.id.radioButton3);

        TextView question2 = view.findViewById(R.id.textView2);
        RadioGroup radioGroup2 = view.findViewById(R.id.radioGroup2);
        trueButton = view.findViewById(R.id.radioButton4);
        falseButton = view.findViewById(R.id.radioButton5);

        if(mParam2 < 6){
            String option1 = keyValueMapList.get("capitalCity");
            String option2 = keyValueMapList.get("secondCity");
            String option3 = keyValueMapList.get("thirdCity");
            List<String> optionsList = new ArrayList<>();
            optionsList.add(option1);
            optionsList.add(option2);
            optionsList.add(option3);
            Collections.shuffle(optionsList);

            question1.setText(getQuestions());
            radioButton1.setText(optionsList.get(0));
            radioButton2.setText(optionsList.get(1));
            radioButton3.setText(optionsList.get(2));

            question2.setText(getSecondQuestion());

            //int count = 0;
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                checkedId1 = checkedId;
            });
            radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
                checkedId2 = checkedId;

            });
        }else {
            question1.setText(getQuestions());
            radioButton1.setText("");
            radioButton2.setText("");
            radioButton3.setText("");
            question2.setText("");
            trueButton.setText("");
            falseButton.setText("");
        }
    }

    /**
     * overriding onResume and updating score and answered questions.
     */
    @Override
    public void onResume(){
        super.onResume();
        Long sc = 0L;
        if (ResultManager.getCumulativeResult() >= 0){
            sc = ResultManager.getCumulativeResult();
        }
        score.setText("Score: " + sc);
        questionsLeft.setText("Answered : " + (mParam2)+ "/6");

    }

    /**
     * overriding onPause and calling resultCalculator() to calculate result
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d("onpause","onpause "+mParam2);
        if(mParam2 < 6){
            resultCalculator();
        }
        if (mParam2 == 5){
            Intent intent = new Intent(getContext(), ResultDisplayActivity.class);
            intent.putExtra("final_result", ResultManager.getCumulativeResult());
            //DBCAll to insert the final result in db. Based on quizId.
            quizId = ResultManager.getQuizId();
            Log.d("Quiz Id from Resukt Manager", String.valueOf(quizId));
            quizResult.setId(quizId);
            quizResult.setResult(ResultManager.getCumulativeResult());
            quizResult.setDate(ResultManager.getDate());
            stateCapitalsData = new StateCapitalsData(getActivity());
            stateCapitalsData.open();
            Log.d("Quiz record to update", String.valueOf(quizResult));
            new QuizResultUpdater().execute(quizResult);
            Log.d("Final Result after position 5", "Final Result"+String.valueOf(ResultManager.getCumulativeResult()));
            ResultManager.resetCumulativeResult();
            startActivity(intent);
            new QuizQuestionUpdater().execute(ResultManager.getList());
        }
    }

    /**
     * Designing Questions from DB record
     * @param stringList
     * @return
     */
    private Map convertStringToMap(String stringList) {
        String[] keyValuePairs = stringList.split(",\\s+");
        Map<String, String> keyValueMap = new HashMap<>();

        // Parse and populate the Map
        for (String pair : keyValuePairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim().replace("{", "");
                String value = parts[1].trim().replaceAll("'", "").replace("}", "");
                keyValueMap.put(key, value);
            }
        }
        return keyValueMap;
    }

    private String getQuestions() {

        String question = "What is the capital city of the state " + keyValueMapList.get("state");
        return question;
    }

    private String getSecondQuestion() {
        String secondQuestion = "Is the capital city the biggest city of the state " + keyValueMapList.get("state");
        return secondQuestion;
    }

    /**
     * Calculating result
     */
    private void resultCalculator(){
        result = 0L;
        Long capitalResult = 0L;
        //try {
        if(checkedId1 != null && checkedId2 != null){
            Log.d("test","test log");
            if (checkedId1 == radioButton1.getId()) {
                if (radioButton1.getText() == keyValueMapList.get("capitalCity")) {
                    capitalResult += 1;
                }
                // Option 1 selected
            } else if (checkedId1 == R.id.radioButton2) {
                if (radioButton2.getText() == keyValueMapList.get("capitalCity")) {
                    capitalResult += 1;
                }
                // Option 2 selected
            } else if (checkedId1 == R.id.radioButton3) {
                if (radioButton3.getText() == keyValueMapList.get("capitalCity")) {
                    capitalResult += 1;
                }
            }
            result = result + capitalResult;

            Long sizeResult = 0L;
            if (checkedId2 == trueButton.getId()) {
                if (keyValueMapList.get("sizeRank").equals("1")) {
                    sizeResult += 1;
                }
            } else if (checkedId2 == falseButton.getId()) {
                if (!(keyValueMapList.get("sizeRank").equals("1"))) {
                    sizeResult += 1;
                }
            }
            result = result + sizeResult;

        }
        // } catch (Exception e) {
        //   NewQuizFragment.newInstance(getArguments().getString(ARG_PARAM1), mParam2, quizId);
        // }
        finalResult+=result;
        ResultManager.addToCumulativeResult((Long) result);
        Log.d("Final Result", "Final Result"+String.valueOf(ResultManager.getCumulativeResult()));
    }

    /**
     * Inner class to update Quiz Record using AsyncTask
     */
    private class QuizResultUpdater extends AsyncTask<QuizResultPOJO, QuizResultPOJO>{

        @Override
        protected QuizResultPOJO doInBackground(QuizResultPOJO... arguments) {
            arguments[0] = quizResult;
            Log.d("In doInBackground", String.valueOf(arguments[0]));
            int updatedRecordId = stateCapitalsData.updateQuizResult(quizId, arguments[0]);
            return null;
        }

        @Override
        protected void onPostExecute(QuizResultPOJO quizResultPOJO) {
        }
    }

    private class QuizQuestionUpdater extends AsyncTask<List<StateCapitalPOJO>, Void>{

        @Override
        protected Void doInBackground(List<StateCapitalPOJO>... arguments) {
            List<StateCapitalPOJO> list = ResultManager.getList();
            int id = stateCapitalsData.insertQuizQuestions(ResultManager.getQuizId(), list);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("new Quiz Fragment onDestroy", "Called onDestroy" + mParam2);
    }

}