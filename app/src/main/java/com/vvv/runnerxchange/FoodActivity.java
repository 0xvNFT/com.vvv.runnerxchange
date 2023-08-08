package com.vvv.runnerxchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    private final List<HistoryItem> historyItems = new ArrayList<>();
    public HistoryAdapter historyAdapter;

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
        setContentView(R.layout.activity_food);

        historyAdapter = new HistoryAdapter(historyItems);

        CardView cardOne = findViewById(R.id.one);
        CardView cardTwo = findViewById(R.id.two);
        CardView cardThree = findViewById(R.id.three);
        CardView cardFour = findViewById(R.id.four);
        ImageView history_button = findViewById(R.id.history_button);


        history_button.setOnClickListener(v -> {
            @SuppressLint("IntentWithNullActionLaunch") Intent intent = new Intent(FoodActivity.this, HistoryActivity.class);
            intent.putExtra("historyItems", (Serializable) historyItems);
            startActivity(intent);
        });

        cardOne.setOnClickListener(this::openWebsite);
        cardTwo.setOnClickListener(this::openWebsite);
        cardThree.setOnClickListener(this::openWebsite);
        cardFour.setOnClickListener(this::openWebsite);
    }

    public void openWebsite(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        startActivity(intent);
    }
}
