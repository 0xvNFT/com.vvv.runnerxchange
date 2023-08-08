package com.vvv.runnerxchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final double WEIGHT_KG = 60.0;
    private final double runningSpeedKmPerHour = 1.0;
    private final double lastTotalDistance = 0.0;
    private final List<HistoryItem> historyItems = new ArrayList<>();
    public SharedPreferences.Editor editor;
    public CountDownTimer countDownTimer;
    private String selectedTargetValue = "NOT SET";
    private AlertDialog alertDialog;
    private TextView durationTextView;
    private ImageView start_button;
    private boolean timerRunning = false;
    private long startTime = 0;
    private double distance = 0.0;
    private double calories = 0.0;
    private TextView distanceTextView;
    private TextView caloriesTextView;
    private double totalDistance = 0.0;
    private long totalTimeInMillis = 0;
    private double lastElapsedTimeInSeconds = 0.0;
    private HistoryAdapter historyAdapter;


    @SuppressLint("SetTextI18n")
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
        setContentView(R.layout.activity_main);

        historyAdapter = new HistoryAdapter(historyItems);

        RelativeLayout target_relative = findViewById(R.id.target_relative);
        ImageView history_button = findViewById(R.id.history_button);
        ImageView food_button = findViewById(R.id.food_button);
        ImageView condition_button = findViewById(R.id.condition_button);
        ImageView target_button = findViewById(R.id.target_button);

        durationTextView = findViewById(R.id.duration_time);
        durationTextView.setText("0.00");
        distanceTextView = findViewById(R.id.distance);
        caloriesTextView = findViewById(R.id.calories);
        start_button = findViewById(R.id.start_button);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        selectedTargetValue = sharedPreferences.getString("selectedTargetValue", "NOT SET");
        TextView target_title = findViewById(R.id.target_title);
        target_title.setText(String.format("GOAL IS: %s", selectedTargetValue));

        target_button.setOnClickListener(v -> {
            openAlertDialog();
        });
        start_button.setOnClickListener(v -> {
            if (timerRunning) {
                stopTimer();
                target_relative.setVisibility(View.VISIBLE);
                food_button.setVisibility(View.VISIBLE);
                condition_button.setVisibility(View.VISIBLE);
                history_button.setVisibility(View.VISIBLE);
            } else {
                startTimer();
                target_relative.setVisibility(View.GONE);
                food_button.setVisibility(View.GONE);
                condition_button.setVisibility(View.GONE);
                history_button.setVisibility(View.GONE);
            }
        });
        history_button.setOnClickListener(v -> {
            @SuppressLint("IntentWithNullActionLaunch") Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("historyItems", (Serializable) historyItems);
            startActivity(intent);
        });
        food_button.setOnClickListener(v -> {
            @SuppressLint("IntentWithNullActionLaunch") Intent intent = new Intent(MainActivity.this, FoodActivity.class);
            startActivity(intent);
        });
        condition_button.setOnClickListener(v -> {
            @SuppressLint("IntentWithNullActionLaunch") Intent intent = new Intent(MainActivity.this, ConditionActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.custom_target_dialog, null);
        builder.setView(customView);
        builder.setOnDismissListener(dialog -> {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        });
        alertDialog = builder.show();

        Spinner targetSpinner = customView.findViewById(R.id.target_spinner);
        String[] targetOptionsArray = getResources().getStringArray(R.array.target_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_layout,
                R.id.spinner_item_text,
                targetOptionsArray
        );
        targetSpinner.setAdapter(adapter);
        int defaultIndex = -1;
        for (int i = 0; i < targetOptionsArray.length; i++) {
            if (targetOptionsArray[i].equals("6500")) {
                defaultIndex = i;
                break;
            }
        }
        if (defaultIndex != -1) {
            targetSpinner.setSelection(defaultIndex);
        }

        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTargetValue = parent.getItemAtPosition(position).toString();
                editor.putString("selectedTargetValue", selectedTargetValue);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ImageView start_target_button = customView.findViewById(R.id.start_target_button);
        start_target_button.setOnClickListener(v -> {
            TextView target_title = findViewById(R.id.target_title);
            target_title.setText(String.format("GOAL IS: %s", selectedTargetValue));
            TextView target_text = findViewById(R.id.target_text);
            target_text.setText("EDIT TARGET");

            editor.putString("selectedTargetValue", selectedTargetValue);
            editor.apply();

            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        });
        builder.setPositiveButton("Start", (dialog, which) -> {
        });
    }

    @SuppressLint("DefaultLocale")
    private void startTimer() {
        timerRunning = true;
        startTime = System.currentTimeMillis();
        totalDistance = lastTotalDistance;

        new Thread(() -> {
            while (timerRunning) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                totalTimeInMillis = elapsedTime;
                final double[] seconds = {elapsedTime / 1000.0};

                runOnUiThread(() -> {
                    String timeText;
                    if (seconds[0] >= 60) {
                        long minutes = (long) (seconds[0] / 60);
                        seconds[0] = seconds[0] % 60;
                        timeText = String.format("%d:%05.2f", minutes, seconds[0]);
                    } else {
                        timeText = String.format("%.2f", seconds[0]);
                    }
                    durationTextView.setText(timeText);

                    double elapsedTimeInSeconds = elapsedTime / 1000.0;
                    double distanceIncrement = runningSpeedKmPerHour * (elapsedTimeInSeconds / 3600.0);
                    distance = lastTotalDistance + distanceIncrement;

                    totalDistance += distance;
                    String totalDistanceText = String.format("%.2f", totalDistance);
                    distanceTextView.setText(totalDistanceText);

                    calories = calculateCalories(totalTimeInMillis);
                    updateCaloriesTextView();
                    if (timerRunning) {
                        distance = (runningSpeedKmPerHour * (seconds[0] / 3600.0)) + lastTotalDistance;
                        lastElapsedTimeInSeconds = seconds[0];
                    } else {
                        distance = (runningSpeedKmPerHour * (lastElapsedTimeInSeconds / 3600.0)) + lastTotalDistance;
                    }
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        start_button.setImageResource(R.drawable.stop_button);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void stopTimer() {
        timerRunning = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        start_button.setImageResource(R.drawable.start_button);
        String durationText = durationTextView.getText().toString();
        String distanceText = distanceTextView.getText().toString();
        String caloriesText = caloriesTextView.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        HistoryItem historyItem = new HistoryItem(currentDateTime, durationText, distanceText, caloriesText);
        historyItems.add(historyItem);

        historyAdapter.notifyDataSetChanged();

    }

    private void updateCaloriesTextView() {
        calories = calculateCalories(totalTimeInMillis);

        @SuppressLint("DefaultLocale") String caloriesText = String.format("%.2f", calories);
        caloriesTextView.setText(caloriesText);
    }

    private double calculateCalories(long timeInMillis) {
        double timeInHours = timeInMillis / (1000.0 * 60.0 * 60.0);
        double MET = getMETForRunningSpeed();
        return MET * MainActivity.WEIGHT_KG * timeInHours;
    }

    private double getMETForRunningSpeed() {
        return 1.0;
    }
}