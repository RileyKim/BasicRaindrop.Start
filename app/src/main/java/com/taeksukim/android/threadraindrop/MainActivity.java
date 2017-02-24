package com.taeksukim.android.threadraindrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FrameLayout layout;
    Button btnStart, btnPause, btnStop;
    Stage stage;

    int deviceWidth, deviceHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics matrix = getResources().getDisplayMetrics();
        deviceWidth = matrix.widthPixels;
        deviceHeight = matrix.heightPixels;

        layout = (FrameLayout) findViewById(R.id.layout);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnStop = (Button) findViewById(R.id.btnStop);


        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        stage = new Stage(this);
        layout.addView(stage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart :

                MakeRain rain = new MakeRain();
                rain.start();


                break;

            case R.id.btnPause :


                break;

            case R.id.btnStop :

                break;
        }
    }

    class MakeRain extends Thread{

        boolean flag = true;

        @Override
        public void run() {
            super.run();
            while (flag){
                Raindrop raindrop = new Raindrop();
                stage.addRaindrop(raindrop);
                raindrop.start();
                try{
                    Thread.sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class Raindrop extends Thread{
        int x,y;
        int radius;
        int speed;
        int direction;

        boolean stopflag = true;
        boolean pausefalg = false;

        @Override
        public void run() {
            super.run();
            while(stopflag && y < deviceHeight + radius){
                if(!pausefalg) {
                    y = y + speed;
                    stage.postInvalidate();
                    try {
                        Thread.sleep(50);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public Raindrop(){
            Random random = new Random();

            x = random.nextInt(deviceWidth);
            y = random.nextInt(deviceHeight);

            radius = random.nextInt(30);
            speed = random.nextInt(10);
        }

    }

    class Stage extends View{


        Paint rainColor;
        List<Raindrop> raindrops;

        public Stage(Context context){
            super(context);
            raindrops = new CopyOnWriteArrayList<>();
            rainColor = new Paint();
            rainColor.setColor(Color.BLUE);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int i = 0; i<raindrops.size(); i++) {
                Raindrop raindrop = raindrops.get(i);
                canvas.drawCircle(raindrop.x, raindrop.y, raindrop.radius, rainColor);
            }
        }
        public void addRaindrop(Raindrop raindrop){
            raindrops.add(raindrop);
        }

        public void removeRaindrop(Raindrop raindrop){
            raindrops.remove(raindrop);
        }
    }
}
