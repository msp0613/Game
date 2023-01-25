package com.example.test1.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameView extends SurfaceView {

    private GameLoopThread gameLoopThread;

    private final List<Object2d> items = new ArrayList<>();
    private Object2d player;

    public final MutableLiveData<Integer> points = new MutableLiveData<>(0);
    public final MutableLiveData<Boolean> endGame = new MutableLiveData<>(false);

    private final Random random = new Random();

    private int speedX = 6;
    private int speedY = 6;
    private int spawnTime = 1000; //co jaki czas maja sie generowac rzeczy z gory
    private int level = 0;

    private long lastFrameTime = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gameLoopThread = new GameLoopThread(this);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }


    private void createPlayer() {
        int maxY = getHeight();
        int maxX = getWidth();

        int playerRadius = 36;
        int playerX = maxX / 2;
        int playerY = maxY - playerRadius;
        int playerSpeedX = 6;
        player = new Object2d(playerX, playerY, Color.MAGENTA, playerSpeedX, 0, playerRadius, maxX, maxY);
    }

    private void createItem() {
        int maxY = getHeight();
        int maxX = getWidth();
        int radius = 36;
        int x = random.nextInt(getWidth() - radius);
        int y = -radius;
        Object2d object2d = new Object2d(x, y, Color.RED, 0, speedY, radius, maxX, maxY);
        object2d.setGoBottom(true);
        items.add(object2d);
    }

    public GameView(Context context) {
        super(context);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        updateGameLogic();
        drawGame(canvas);
    }

    private void drawGame(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        items.forEach(object2d -> {
            object2d.draw(canvas);
        });
        if (player != null) {
            player.draw(canvas);
        }
    }

    private void updateGameLogic() {
        if (endGame.getValue()) {
            return;
        }
        updateLevel();
        updateItems();
        updatePlayer();
        updatePoints();
    }

    private void updatePlayer() {
        if (player == null) {
            createPlayer();
        }
        player.setSpeedX(speedX);
        player.update();
        for (Object2d object2d : items) {
            if (player != null && object2d.collide(player)) {
                endGame.postValue(true);
            }
        }
    }

    private void updatePoints() {
        items.removeIf(object2d -> {
            boolean result = object2d.shouldBeBeRemoved();
            if (result) {
                points.postValue(points.getValue() + 1);
            }
            return result;
        });
    }

    private void updateItems() {
        if (lastFrameTime == 0) {
            lastFrameTime = System.currentTimeMillis();
            createItem();
        }
        long deltaTime = System.currentTimeMillis() - lastFrameTime;
        if (deltaTime >= spawnTime) {
            lastFrameTime = System.currentTimeMillis();
            createItem();
        }
        for (Object2d object2d : items) {
            object2d.setSpeedY(speedY);
            object2d.update();
        }
    }

    private void updateLevel() {
        if (points.getValue() >= 10 && level == 0) {
            speedY = 8;
            speedX = 6;
            spawnTime = 800;
            level = 1;
        }
        if (points.getValue() >= 20 && level == 1) {
            speedY = 8;
            speedX = 6;
            spawnTime = 800;
            level = 2;
        }

        if (points.getValue() >= 20 && level == 2) {
            speedY = 10;
            speedX = 6;
            spawnTime = 600;
            level = 3;
        }

        if (points.getValue() >= 30 && level == 3) {
            speedY = 12;
            speedX = 6;
            spawnTime = 500;
            level = 4;
        }

        if (points.getValue() >= 40 && level == 4) {
            speedY = 13;
            speedX = 8;
            spawnTime = 450;
            level = 5;
        }

        if (points.getValue() >= 50 && level == 5) {
            speedY = 14;
            speedX = 8;
            spawnTime = 400;
            level = 6;
        }
        if (points.getValue() >= 60 && level == 6) {
            speedY = 15;
            speedX = 8;
            spawnTime = 400;
            level = 7;
        }
        if (points.getValue() >= 70 && level == 7) {
            speedY = 16;
            speedX = 12;
            spawnTime = 400;
            level = 8;
        }

        if (points.getValue() >= 80 && level == 8) {
            speedY = 16;
            speedX = 16;
            spawnTime = 400;
            level = 9;
        }
        if (points.getValue() >= 90 && level == 9) {
            speedY = 17;
            speedX = 17;
            spawnTime = 500;
            level = 10;
        }
        if (points.getValue() >= 100 && level == 10) {
            speedY = 20;
            speedX = 20;
            spawnTime = 300;
            level = 11;
        }
    }

    public void leftPressed(boolean goLeft) {
        player.setGoLeft(goLeft);
    }

    public void rightPressed(boolean goRight) {
        player.setGoRight(goRight);
    }

    public void restart() {
        speedX = 6;
        speedY = 6;
        spawnTime = 1000;
        level = 0;
        items.clear();
        player = null;
        points.postValue(0);
        endGame.postValue(false);
    }
}
