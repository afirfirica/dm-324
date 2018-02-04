package xyz.pinaki.androidcamera.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        getSupportActionBar().hide();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000); // 3000ms is the time to sleep
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{

                    Intent intent = new Intent(Splash1.this,Splash2.class);
                    startActivity(intent);

                }
            }
        };
        timer.start();
    }
}
