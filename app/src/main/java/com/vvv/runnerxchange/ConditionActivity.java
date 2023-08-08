package com.vvv.runnerxchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ConditionActivity extends AppCompatActivity {

    private EditText weightEditText;
    private EditText heightEditText;
    private TextView resultTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_condition);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        AppCompatButton calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);
        ImageView history_button = findViewById(R.id.history_button);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        history_button.setOnClickListener(v -> {
            @SuppressLint("IntentWithNullActionLaunch") Intent intent = new Intent(ConditionActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        calculateButton.setOnClickListener(v -> calculateBMI());
    }

    @Override
    protected void onResume() {
        super.onResume();

        float bmiResult = sharedPreferences.getFloat("bmiResult", 0.0f);
        displayBMIResult(bmiResult);
    }

    private void calculateBMI() {
        String weightStr = weightEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr) / 100.0;

            double bmi = weight / (height * height);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("bmiResult", (float) bmi);
            editor.apply();

            displayBMIResult(bmi);
        }
    }

    private void displayBMIResult(double bmi) {
        String category;
        if (bmi < 18.5) {
            category = "Underweight";
        } else if (bmi < 24.9) {
            category = "Normal Weight";
        } else if (bmi < 29.9) {
            category = "Overweight";
        } else {
            category = "Obese";
        }

        @SuppressLint("DefaultLocale") String result = String.format("BMI: %.2f\nCategory: %s", bmi, category);
        resultTextView.setText(result);
    }
}
