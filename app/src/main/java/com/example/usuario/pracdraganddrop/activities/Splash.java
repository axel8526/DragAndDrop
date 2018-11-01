package com.example.usuario.pracdraganddrop.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.usuario.pracdraganddrop.R;

public class Splash extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable animationDrawable;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView= findViewById(R.id.image_animation);

        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(getApplicationContext(), Configuracion.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
