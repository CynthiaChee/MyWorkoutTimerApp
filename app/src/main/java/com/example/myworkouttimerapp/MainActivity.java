package com.example.myworkouttimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //initializing variables
    EditText setsText, workoutText, restText, nameText;
    String nameInput="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view by ID
        setsText = findViewById(R.id.setsEditText);
        workoutText = findViewById(R.id.workoutEditText);
        restText = findViewById(R.id.restEditText);
        nameText = findViewById(R.id.nameEditText);
    }

    //When the start button is clicked
    public void OnStartClicked(View view) {

        //variables to hold user inputs
        int setsInput = getIntValue(setsText);
        int workoutInput = getIntValue(workoutText);
        int restInput = getIntValue(restText);
        nameInput = nameText.getText().toString();
        nameText.setText(nameInput);

        //if inputs are valid
        if(setsInput > 0 && workoutInput > 0 && restInput > 0){

            //create new activity and send data to workout activity
            Intent workoutIntent = new Intent(this, WorkoutActivity.class);
            workoutIntent.putExtra("sets", setsInput);
            workoutIntent.putExtra("workout", workoutInput);
            workoutIntent.putExtra("rest", restInput);
            workoutIntent.putExtra("name", nameInput);
            startActivity(workoutIntent);
        }
    }

    //function to convert user input to int and check if input is valid
    private int getIntValue(EditText userInput){
        try {
            return Integer.valueOf(userInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
}
