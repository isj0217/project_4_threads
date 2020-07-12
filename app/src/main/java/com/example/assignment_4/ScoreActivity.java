package com.example.assignment_4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView tv_finalScore;
    int finalScore;

    Button btn_goToMain;
    Button btn_tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score);

        btn_goToMain = findViewById(R.id.btn_goToMain);
        btn_goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_tryAgain = findViewById(R.id.btn_tryAgain);
        btn_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });





        finalScore = 0;

        tv_finalScore = findViewById(R.id.tv_finalScore);


        finalScore = getIntent().getIntExtra("finalScore", 0);

        tv_finalScore.setText("최종 판매 금액: " + finalScore + " 원");

    }

}