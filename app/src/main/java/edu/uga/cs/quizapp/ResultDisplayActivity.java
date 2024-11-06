package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity class to display quiz result.
 */
public class ResultDisplayActivity extends AppCompatActivity {

    TextView textView;
    Button newQuiz;
    Button backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_dispaly);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textView = findViewById(R.id.textView3);
        newQuiz = findViewById(R.id.newQuiz);
        backToHome = findViewById(R.id.backToHome);

        //Get the intent
        Intent intent = getIntent();
        if (intent != null) {
            //Get the final result value
            Long value = intent.getLongExtra("final_result",0);
            Log.d("Result in result activity", String.valueOf(value));
            //set the final result
            textView.setText("Score: "+value);
        }

        //Anonymous class to set the button functionality for a new quiz
        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the new activity(NewQuizActivity) when the button clicked.
                Intent intent = new Intent(getApplicationContext(),NewQuizActivity.class);
                startActivity(intent);
            }
        });

        //Anonymous class to set the button functionality for a Back to home button
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the new activity(MainActivity) when the button clicked.
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}