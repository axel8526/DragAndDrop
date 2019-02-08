package com.example.usuario.pracdraganddrop.control;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;

import java.util.ArrayList;

public class MenuComponentes {

    public static final String DO="DigitalOutput";
    public static final String DI="DigitaInput";
    public static final String AIV="AnalogInput V";
    public static final String AOV="AnalogOutput V";
    public static final String AII="AnalogInput I";
    public static final String AOI="AnalogOutput I";
    public static final String LED="Led";
    public static final String BUTTON="Button";

    public static final String R_DO="DO";
    public static final String R_DI="DI";
    public static final String R_AIV="AIV";
    public static final String R_AOV="AOV";
    public static final String R_AII="AII";
    public static final String R_AOI="AOI";
    public static final String R_LED="LED";
    public static final String R_BUTTON="BUTTON";


    private LinearLayout componentesLayout;
    private Context context;

    private boolean comDI, comDO;

    private ArrayList<View> inputsOuputs;
    private boolean menuC;

    public MenuComponentes(Context context, LinearLayout compLayout){
        this.componentesLayout=compLayout;
        this.context=context;
        inputsOuputs=new ArrayList<>();

        InputCView con=new InputCView(context,DI,R.drawable.icon_connect);
        OutputCView out=new OutputCView(context,DO,R.drawable.icon_connect);
        inputsOuputs.add(con);
        inputsOuputs.add(out);

        componentesLayout.addView(con);
        componentesLayout.addView(out);

    }

    public void addOutput(OutputCView output){
        inputsOuputs.add(output);
    }
    public void addInputs(InputCView input){
        inputsOuputs.add(input);
    }

    public View.OnTouchListener touch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:

                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};


                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);


                    //getNuevoComponente es para crear uno nuevo, ya que si solo se envia la vista
                    //no se va a poder agregar al otro contedor ya que esta puesto en uno
                    view.startDrag(null,shadowBuilder,getNuevoComponente(view),0);

                    cerrarMenus();
                    return true;


            }
            return false;
        }
    };

    public Object getNuevoComponente(View view){
         if(view instanceof InputCView){

            InputCView inputCView0=(InputCView)view;
            InputCView inputCView=new InputCView(context,inputCView0.getReference(),inputCView0.getImageResouce());
            return inputCView;

        }else if(view instanceof OutputCView){
            OutputCView outputCView2=(OutputCView)view;
            OutputCView outputCView=new OutputCView(context,
                    outputCView2.getReference(),outputCView2.getImageResouce());
            return outputCView;
        }
        return null;
    }


    public void entradasArraste(View.OnTouchListener touch){

        for(View view: inputsOuputs){
            if(view instanceof InputCView){
                InputCView input=(InputCView)view;

                switch (input.getReference()){
                    case DI:
                        setTouch(input,touch,comDI);
                        break;
                    case AIV:
                        break;
                    case AII:
                        break;
                    case BUTTON:
                        break;
                }

            }else if(view instanceof OutputCView){
                OutputCView output=(OutputCView)view;
                switch (output.getReference()){
                    case DO:
                        setTouch(output,touch,comDO);
                        break;
                    case AOV:
                        break;
                    case AOI:
                        break;
                    case LED:
                        break;
                }
            }
        }


    }
    private void setTouch(View view, View.OnTouchListener t,boolean disponible){
        if(!disponible){
            view.setOnTouchListener(null);
        }else{
            view.setOnTouchListener(t);
        }
        if(view instanceof InputCView){
            if(!disponible)((InputCView)view).setColorFilterImage(R.color.fondo_imagen_omitir);
            else ((InputCView)view).clearColorFilter();
        }else{
            if(!disponible)((OutputCView)view).setColorFilterImage(R.color.fondo_imagen_omitir);
            else ((OutputCView)view).clearColorFilter();
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
        int cantidadDI=0,
                cantidadDO=0,
                cantLed=0,
                cantButton=0,
                cantAIv=0,
                cantAIi=0,
                cantAOv=0,
                cantAOi=0;
        comDI=true;
        comDO=true;

        for (int i=0;i<componentes.size();i++){
            if (componentes.get(i) instanceof InputCView){
                InputCView input=(InputCView)componentes.get(i);
                String ref=input.getReference().split("_")[0];

                switch (ref){
                    case R_DI:
                        cantidadDI++;
                        break;
                    case R_BUTTON:
                        break;
                    case R_AII:
                        break;
                    case R_AIV:
                        break;
                }



            }else if (componentes.get(i) instanceof  OutputCView){

                OutputCView output=(OutputCView) componentes.get(i);
                String ref=output.getReference().split("_")[0];

                switch (ref){
                    case R_DO:
                        cantidadDO++;
                        break;
                    case R_LED:
                        break;
                    case R_AOI:
                        break;
                    case R_AOV:
                        break;
                }
            }
        }


        if (cantidadDI>=context.getResources().getStringArray(R.array.digital_input).length){
            comDI=false;
        }
        if (cantidadDO>=context.getResources().getStringArray(R.array.digital_output).length){
            comDO=false;

        }
    }



}
