package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewQuizActivity extends AppCompatActivity {

    private List<StateCapitalPOJO> list = new ArrayList<>();
    private StateCapitalsData stateCapitalsData = null;
    private QuizResultPOJO recentQuizResult = new QuizResultPOJO();

    /**
     * overriding callback to initialize value declared and
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        stateCapitalsData = new StateCapitalsData(getApplicationContext());
        stateCapitalsData.open();
        List<StateCapitalPOJO> capitalList = new ArrayList<>();
        capitalList = stateCapitalsData.createQuiz();
        list.addAll(capitalList);
        ResultManager.setList(list);
        ResultManager.setPosition(-1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        Long result = 0L;
        QuizResultPOJO quizResult = new QuizResultPOJO(result, currentDateAndTime);
        new SaveQuizWriter().execute(quizResult);

        ViewPager2 pager = findViewById(R.id.viewpager);
        NewQuizesPageAdapter avpAdapter = new NewQuizesPageAdapter(getSupportFragmentManager(), getLifecycle(), list);
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(avpAdapter);
    }

    /**
     * Inner class to create a new quiz
     */
    public class SaveQuizWriter extends AsyncTask<QuizResultPOJO, QuizResultPOJO> {
        @Override
        protected QuizResultPOJO doInBackground(QuizResultPOJO... arguments) {
            stateCapitalsData.storeQuizRecord(arguments[0]);
            return arguments[0];
        }

        @Override
        protected void onPostExecute(QuizResultPOJO quizResultPOJO) {
            Toast.makeText(getApplicationContext(), "Quiz record created for " + quizResultPOJO.getId(),
                    Toast.LENGTH_SHORT).show();
            recentQuizResult = quizResultPOJO;
            ResultManager.setQuizId(recentQuizResult.getId());
            ResultManager.setDate(recentQuizResult.getDate());
            Log.d("Quiz record print", String.valueOf(recentQuizResult));
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("new Quiz onPause", "Called onPause" + ResultManager.getPosition());

    }

    @Override
    public void onStop(){
        super.onStop();
//        Log.d("new Quiz onStop", "Called onStop" + ResultManager.getPosition());
    }

    /**
     * overriding callback to delete incomplete quiz from DB
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("## new Quiz onDestroy ##", "Called onDestroy" + ResultManager.getPosition());

        if (ResultManager.getPosition() < 6) {
            stateCapitalsData = new StateCapitalsData(getApplicationContext());
            stateCapitalsData.open();
            new DeleteIncompleteQuiz().execute();
        }
    }

    /**
     * Inner class to Delete quiz record
     */
    private class DeleteIncompleteQuiz extends AsyncTask<Void, Void>{

        @Override
        protected Void doInBackground(Void... arguments) {
            stateCapitalsData.deleteIncompleteQuizRecord(ResultManager.getQuizId());
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

}
