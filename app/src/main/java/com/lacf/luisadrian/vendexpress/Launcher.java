package com.lacf.luisadrian.vendexpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Launcher extends Activity {

    private SharedPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        data = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectActivity();
            }
        }, 600);
    }

    private void selectActivity() {
        if (data.contains("id") && data.contains("email") && data.contains("firstName") && data.contains("lastName") && data.contains("phone")) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        finish();
    }
}
