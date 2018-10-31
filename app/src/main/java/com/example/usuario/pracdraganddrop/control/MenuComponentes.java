package com.example.usuario.pracdraganddrop.control;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;

import java.util.ArrayList;

public class MenuComponentes {

    private LinearLayout componentesLayout;
    private Context context;

    private boolean comDI, comDO;

    public MenuComponentes(Context context, LinearLayout compLayout){
        this.componentesLayout=compLayout;
        this.context=context;

    }
    public View.OnTouchListener touch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:

                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};


                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    view.startDrag(null,shadowBuilder,getNuevoComponente(view),0);

                    cerrarMenus();
                    return true;


            }
            return false;
        }
    };

    public Object getNuevoComponente(View view){
        if (view instanceof EntradaDigitalView){
            EntradaDigitalView entradaView=new EntradaDigitalView(context);

            entradaView.setImageResource(R.drawable.icon_sensor);
            //entradaView.setImageDrawable(((EntradaView)view).getDrawable());
            return entradaView;

        }else if(view instanceof SalidaDigitalView){
            SalidaDigitalView salidaView=new SalidaDigitalView(context);
            salidaView.setImageResource(R.drawable.icon_salida);
            //salidaView.setImageDrawable(((SalidaView)view).getDrawable());
            return salidaView;

        }
        return null;
    }
    public void entradasArraste(View.OnTouchListener touch){
        EntradaView entradaView=componentesLayout.findViewById(R.id.entrada_digital);
        SalidaView salidaView=componentesLayout.findViewById(R.id.salida_digital);

        if (!comDI){
            entradaView.setOnTouchListener(null);
            entradaView.setColorFilter(Color.GRAY);
        }else{
            entradaView.setOnTouchListener(touch);
            entradaView.clearColorFilter();
        }
        if (!comDO){
            salidaView.setOnTouchListener(null);
            salidaView.setColorFilter(Color.GRAY);
        }else{
            salidaView.setOnTouchListener(touch);
            salidaView.clearColorFilter();
        }
    }

    public void cerrarMenus(){
        Animation animation;
        if (componentesLayout.getVisibility()==View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(context, R.anim.desaparecer_left);
            animation.setFillAfter(true);
            componentesLayout.startAnimation(animation);
            componentesLayout.setVisibility(View.INVISIBLE);
            entradasArraste(null);
        }




    }
    public void abrirMenu(ArrayList<View> componentes){
        componentesDisponibles(componentes);

        if (componentesLayout.getVisibility()==View.INVISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.aparecer_left);
            animation.setFillAfter(true);
            componentesLayout.startAnimation(animation);
            componentesLayout.setVisibility(View.VISIBLE);
            componentesLayout.setEnabled(true);
            entradasArraste(touch);
        }


    }


    public void componentesDisponibles(ArrayList<View> componentes){
        int cantidadDI=0, cantidadDO=0;
        comDI=true;
        comDO=true;

        for (int i=0;i<componentes.size();i++){
            if (componentes.get(i) instanceof EntradaDigitalView){
                cantidadDI++;
            }else if (componentes.get(i) instanceof  SalidaDigitalView){
                cantidadDO++;
            }
        }
        if (cantidadDI==context.getResources().getStringArray(R.array.digital_input).length){
            comDI=false;
        }
        if (cantidadDO==context.getResources().getStringArray(R.array.digital_output).length){
            comDO=false;

        }
    }



}
