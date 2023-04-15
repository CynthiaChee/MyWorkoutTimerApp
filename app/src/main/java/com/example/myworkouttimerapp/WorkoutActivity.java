package com.example.myworkouttimerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class WorkoutActivity extends AppCompatActivity {

    //Initializing variables
    int setsTotal, workoutTotal, restTotal, setsRemain;
    CountDownTimer myTimer;
    Boolean currentlyWorking = false, workingOut = false, isPaused = false;
    TextView activityText, mySetsRemaining, myActivityRemaining, myTitle;
    TextView totalSetNumber, totalWorkoutSecs, totalRestSecs;
    ProgressBar myProgress;
    Button myButton;


    // Set some variable from the intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //obtain data from previous activity
        Intent myIntent = getIntent();
        setsTotal = myIntent.getIntExtra("sets", 0);
        setsRemain = setsTotal;
        workoutTotal = myIntent.getIntExtra("workout", 0);
        restTotal = myIntent.getIntExtra("rest", 0);

        myTitle = findViewById(R.id.mainTitle);
        String titleString = getIntent().getStringExtra("name") + "'s Workout Session";
        myTitle.setText(titleString);

    }

    // set up and display correct values in the view
    @Override
    protected void onStart() {
        super.onStart();

        //find view by ID for total values and setting the text
        totalSetNumber = findViewById(R.id.totalSetNumber);
        totalWorkoutSecs = findViewById(R.id.totalWorkoutSecs);
        totalRestSecs = findViewById(R.id.totalRestSecs);

        totalSetNumber.setText(String.valueOf(setsTotal));
        totalWorkoutSecs.setText(String.valueOf(workoutTotal));
        totalRestSecs.setText(String.valueOf(restTotal));

        activityText = findViewById(R.id.myActivity);

        //find view by ID for countdown values and setting the text
        mySetsRemaining = findViewById(R.id.setsCountdown);
        myActivityRemaining = findViewById(R.id.activityCountdown);

        mySetsRemaining.setText(String.valueOf(setsTotal));
        myActivityRemaining.setText(String.valueOf(workoutTotal));

        //Other variables
        myProgress = findViewById(R.id.progressBar);
        myButton = findViewById(R.id.beginButton);

    }

    //When begin button is clicked
    public void beginWorkoutClicked(View view) {

        //starts workout if not currently working out
        //begin button changes to pause button
        if (currentlyWorking == false) {
            workingOut = true;
            countdownActivity(view);
            myButton.setText("PAUSE");
            myButton.setBackgroundColor(Color.parseColor("#FF8C00"));
            currentlyWorking = true;
        }

        //begin button changes to pause or resume button during workout
        else {
            if (isPaused == false) {
                isPaused = true;
                myTimer.cancel();
                myButton.setText("RESUME");
                myButton.setBackgroundColor(Color.parseColor("#009F07"));
            }
            //when resume button pressed
            else{
                isPaused = false;
                myButton.setText("PAUSE");
                myButton.setBackgroundColor(Color.parseColor("#FF8C00"));

                //temp variable to hold total time to reset after each countdown
                int tempTotalTime = workoutTotal;
                workoutTotal = Integer.parseInt((myActivityRemaining.getText().toString()));
                countdownActivity(view);
                workoutTotal = tempTotalTime;
            }
        }
    }

    //if Go Back button clicked
    public void goBackClicked(View view){
        myTimer.cancel();
        Intent goBackIntent = new Intent(this, MainActivity.class);
        startActivity(goBackIntent);
    }

    //counting down the timer
    public void countdownActivity(View view){
        //at the end of the workout and all sets
        if(setsRemain <= 0){
            currentlyWorking = false;
            setsRemain = setsTotal;
            myButton.setText("BEGIN");
            mySetsRemaining.setText(String.valueOf(setsRemain));
            return;
        }
        //while working out, change text and button displays
        if(workingOut) {
            activityText.setText("WORKOUT");
            activityText.setTextColor(Color.parseColor("#EA0000"));
            myActivityRemaining.setTextColor(Color.parseColor("#EA0000"));
            myActivityTimer(workoutTotal, myActivityRemaining, myProgress, view);
        }
        //while resting, change text and button displays
        else {
            activityText.setText("REST");
            activityText.setTextColor(Color.parseColor("#0048FF"));
            myActivityRemaining.setTextColor(Color.parseColor("#0048FF"));
            myActivityTimer(restTotal, myActivityRemaining, myProgress, view);
        }

    }

    //counting down and updating the progress bar
   public void myActivityTimer(int tick, TextView updateText, ProgressBar updateProgress, View view){
        CountDownTimer workoutTimer = new CountDownTimer((tick * 1000L), 1) {
            @Override
            public void onTick(long millisecs) {
                myTimer = this;
                long secs = (millisecs / 1000);
                NumberFormat form = new DecimalFormat();
                updateText.setText((form.format(secs)));
                int myProgress = (int) (((workoutTotal * 1000L) - millisecs) / ((workoutTotal * 1000L) / 100));
                updateProgress.setProgress(myProgress);
            }

            @Override
            public void onFinish() {
                //if working out
                if (workingOut){
                    workingOut = false;
                    myActivityRemaining.setText(String.valueOf(workoutTotal));
                    countdownActivity(view);
                }
                //if resting
                else{
                    workingOut = true;
                    setsRemain--;
                    mySetsRemaining.setText(String.valueOf(setsRemain));
                    updateText.setText(String.valueOf(restTotal));
                    countdownActivity(view);
                }
            }
        }.start();
    }
}
