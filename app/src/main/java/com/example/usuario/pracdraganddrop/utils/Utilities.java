package com.example.usuario.pracdraganddrop.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.usuario.pracdraganddrop.R;

public class Utilities {

    private Context context;
    public Utilities(Context context) {
        this.context=context;
    }

    public void iniciarAnimacion(ViewGroup viewGroup, int action, final Dialog dialog){
        Animation animation;
        if (action==0){
            animation= AnimationUtils.loadAnimation(context, R.anim.aparecer_centro);
            viewGroup.startAnimation(animation);

        }else{
            animation=AnimationUtils.loadAnimation(context,R.anim.desaparecer_right);
            viewGroup.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
