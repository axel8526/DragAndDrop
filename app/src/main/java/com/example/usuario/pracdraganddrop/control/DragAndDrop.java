package com.example.usuario.pracdraganddrop.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;

import java.util.ArrayList;

public class DragAndDrop  {

    public static final int ESTADO_NORMAL=0;
    public static final int ESPERA_ENTRADA=1;
    public static final int ESPERA_SALIDA=2;

    private Context context;
    private RelativeLayout layoutDragAndDrop;
    private ComponenteEstado componenteEstado;

    private ConfiComponente confiComponente;



    public DragAndDrop(Context context, RelativeLayout layoutDragAndDrop){
        this.context=context;
        this.layoutDragAndDrop=layoutDragAndDrop;
        activarReceptor();
        componenteEstado=new ComponenteEstado();
        componenteEstado.setEstadoDragAndDrop(ESTADO_NORMAL);

    }
    private void activarReceptor(){
        layoutDragAndDrop.setOnDragListener(startDrag);
    }

    private View.OnDragListener startDrag= new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            int action = dragEvent.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;


                case DragEvent.ACTION_DRAG_ENTERED:

                    view.getBackground().setColorFilter(context.getResources().getColor(R.color.color_filter_dad), PorterDuff.Mode.MULTIPLY);
                    view.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    view.getBackground().clearColorFilter();

                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:
                    view.invalidate();

                    View v = (View) dragEvent.getLocalState();

                    RelativeLayout container = (RelativeLayout) view;//se castea un linear layout de layout receptor
                    container.addView(v);//añadimos la vista al contenedor que casteamos
                    v.setVisibility(View.VISIBLE);// Visibility to VISIBLE
                    v.setX(dragEvent.getX());
                    v.setY(dragEvent.getY());


                    MoverVista moverVista = new MoverVista(v, v.getX(), v.getY());
                    moverVista.comprobarPosicion(context);

                    confiComponente = new ConfiComponente(v, context);
                    confiComponente.abrirDialogReferencia();


                    clickEnLaVista(v);
                    iniciarMovimientoVista(v);



                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    view.getBackground().clearColorFilter();
                    view.invalidate();

                    return true;
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                    break;

            }
            return false;

        }
    };

    public View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            PointF starPt;
            PointF downPt = new PointF();
            int modificarX = 100;
            int modificarY = 100;

            int ae = motionEvent.getAction();


            switch (ae) {
                case MotionEvent.ACTION_DOWN:
                    downPt.y = motionEvent.getY();
                    downPt.x = motionEvent.getX();


                    break;
                case MotionEvent.ACTION_MOVE:

                    starPt = new PointF(view.getX(), view.getY());
                    PointF move = new PointF(motionEvent.getX() - downPt.x, motionEvent.getY() - downPt.y);

                    float moveX = (starPt.x + move.x) - modificarX;
                    float moveY = (starPt.y + move.y) - modificarY;

                    MoverVista moverVista = new MoverVista(view, moveX, moveY);
                    moverVista.verificarMovimiento();


                    break;
                case MotionEvent.ACTION_UP:
                    MoverVista moverVista1 = new MoverVista(view, view.getX(), view.getY());
                    moverVista1.comprobarPosicion(context);
                    view.setOnTouchListener(null);

                    break;

                default:
                    break;
            }
            return true;
        }
    };

    public void iniciarMovimientoVista(View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setOnTouchListener(touch);
                return true;
            }
        });
    }

    public void clickEnLaVista(View view){

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confiComponente.opcionRealizar(view, componenteEstado);

            }
        });
    }


    @TargetApi(23)
    public void estadoDragAndDrop(ComponenteEstado estado){
        componenteEstado=estado;

        for (int i = 0; i < layoutDragAndDrop.getChildCount(); i++) {
            View v=layoutDragAndDrop.getChildAt(i);

            if(componenteEstado.getEstadoDragAndDrop()==ESPERA_ENTRADA){

                if (v instanceof EntradaView){
                    //if (v.getTag().toString().equalsIgnoreCase(MainActivity.IMAGE_ENTRADA)){
                    EntradaView entradaView=(EntradaView)v;

                    entradaView.setColorFilter(context.getResources().getColor(R.color.fondo_imagen_omitir));


                }

            }else if (componenteEstado.getEstadoDragAndDrop()==ESPERA_SALIDA){
                if (v instanceof SalidaView){

                    SalidaView salidaView=(SalidaView) v;
                    salidaView.setColorFilter(context.getResources().getColor(R.color.fondo_imagen_omitir));
                }
            }else{
                AppCompatImageView imageView=(AppCompatImageView)layoutDragAndDrop.getChildAt(i);
                imageView.clearColorFilter();
            }
        }

    }

    public ArrayList<View> getComponentes(){
        ArrayList<View> com=new ArrayList<>();

        for (int i=0; i<layoutDragAndDrop.getChildCount();i++){
            com.add(layoutDragAndDrop.getChildAt(i));

        }
        return com;
    }

    public ArrayList<SalidaDigitalView> getSalidasDigitales(){
        ArrayList<SalidaDigitalView> salidas=new ArrayList<>();

        for (int i=0; i<layoutDragAndDrop.getChildCount();i++){
            if (layoutDragAndDrop.getChildAt(i) instanceof SalidaDigitalView)
                salidas.add((SalidaDigitalView)layoutDragAndDrop.getChildAt(i));


        }

        return salidas;
    }
    public ArrayList<EntradaDigitalView> getEntradasDigitales(){
        ArrayList<EntradaDigitalView> entradas=new ArrayList<>();

        for (int i=0; i<layoutDragAndDrop.getChildCount();i++){
            if (layoutDragAndDrop.getChildAt(i) instanceof EntradaDigitalView)
                entradas.add((EntradaDigitalView) layoutDragAndDrop.getChildAt(i));


        }

        return entradas;
    }

}
