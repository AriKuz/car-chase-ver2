package com.example.carchase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameStartActivity extends AppCompatActivity {

    private TextView scoreTable;
    //Buttons
    private Button easyButton;
    private Button hardButton;
    private Button quitButton;

    int lastScore;
    int best1,best2,best3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        SharedPreferences preferences = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        lastScore = preferences.getInt("HIGH_SCORE", 0);
        best1 = preferences.getInt("best1", 0);
        best2 = preferences.getInt("best2", 0);
        best3 = preferences.getInt("best3", 0);

        scoreTable = findViewById(R.id.table);
   //    easyButton = findViewById(R.id.easyButton);
        hardButton = findViewById(R.id.hardButton);
        quitButton = findViewById(R.id.quitButton);


        // Handle score table
        if(lastScore > best3 && lastScore < best2){
            best3 = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.apply();
        }

        if(lastScore > best2 && lastScore < best1){
            int temp = best2;
            best2 = lastScore;
            best3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.putInt("best2", best2);
            editor.apply();
        }

        if(lastScore > best1){
            int temp = best1;
            best1 = lastScore;
            best2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best2", best2);
            editor.putInt("best1", best1);
            editor.apply();
        }
        // display scores
        scoreTable.setText("HIGH SCORES : " + "\n" +
                "Last Score:  " + lastScore + "\n" +
                "1st Place  " + best1 + "\n" +
                "2nd Place  " + best2 + "\n" +
                "3rd Place  " + best3);



        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        final Intent intent2 = new Intent(getApplicationContext(), HardModeActivity.class);
//        easyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(intent);
//            }
//        });


        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        });

    }
}
