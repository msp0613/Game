package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.test1.game.GameView;

public class MainActivity extends AppCompatActivity {
    private Button restartBtn;
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView=findViewById(R.id.gameView);
        restartBtn=findViewById(R.id.restatrBtn);
        restartBtn.setOnClickListener(v -> restartGame());
        findViewById(R.id.leftBtn).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gameView.leftPressed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                gameView.leftPressed(false);
            }
            return true;
        });
        findViewById(R.id.rightBtn).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              gameView.rightPressed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               gameView.rightPressed(false);
            }
            return true;
        });
        gameView.endGame.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean endGame) {
                if (endGame) {
                    restartBtn.setVisibility(View.VISIBLE);
                }
                else {
                    restartBtn.setVisibility(View.GONE);
                }
            }
        });


    }

    private void restartGame() {
        gameView.restart();
    }
}