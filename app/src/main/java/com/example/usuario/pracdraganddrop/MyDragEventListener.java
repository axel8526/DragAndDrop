package com.example.usuario.pracdraganddrop;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;
import com.example.usuario.pracdraganddrop.control.ConfiComponente;
import com.example.usuario.pracdraganddrop.control.MoverVista;

public class MyDragEventListener implements View.OnDragListener{

    public static final String ESPERA_SALIDA = "ESPERA_S";
    public static final String ESPERA_ENTRADA = "ESPERA_E";
    private Context context;
    private String estadoComponentes = "";
    private View vistaEnEspera;
    private int modificarX = 100;
    private int modificarY = 100;

    private RelativeLayout container;

    public MyDragEventListener(Context context) {
        this.context = context;
    }

    public View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            PointF starPt = new PointF();
            PointF downPt = new PointF();
            int ae = motionEvent.getAction();


            switch (ae) {
                case MotionEvent.ACTION_DOWN:
                    downPt.y = motionEvent.getY();
                    downPt.x = motionEvent.getX();


                    break;
                case MotionEvent.ACTION_MOVE:

                    //traemos el contenedor padre de la vista, y obtenemos su tamaño
                   /* ViewGroup container=(RelativeLayout)view.getParent();
                    int maxWith=container.getWidth();
                    int maxHeigh=container.getHeight();*/


                    starPt = new PointF(view.getX(), view.getY());
                    PointF move = new PointF(motionEvent.getX() - downPt.x, motionEvent.getY() - downPt.y);

                    float moveX = (starPt.x + move.x) - modificarX;
                    float moveY = (starPt.y + move.y) - modificarY;

                    MoverVista moverVista = new MoverVista(view, moveX, moveY);
                    moverVista.verificarMovimiento();


                    /*if (container.getX()<moveX && container.getY()<moveY &&
                            moveX+view.getWidth()<maxWith && moveY+view.getHeight()<maxHeigh){

                        view.setX(moveX);
                        view.setY(moveY);

                        if (view.getTag().toString().equalsIgnoreCase(MainActivity.IMAGE_ENTRADA)){
                            EntradaView entradaView=(EntradaView)view;
                            entradaView.dibujarLineas();
                        }else{
                            SalidaView salidaView=(SalidaView)view;
                            salidaView.dibujarLinea(new PointF(view.getX(),view.getY()));
                        }

                    }*/


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


    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        final int action = dragEvent.getAction();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                //if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {


                    //view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view

                    // Invalidate the view to force a redraw in the new tint
                    //  view.invalidate();


                    return true;

                //}

                //return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);//pintar cuando se coloca encima

                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:

                return true;
            case DragEvent.ACTION_DRAG_EXITED:


                view.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                //obtener el item conenido en el dragevent
                // linea com1 ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                // Gets the text data from the item.
                //linea com1 final String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
                // Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                // Turns off any color tints
                //alex  view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                final View v = (View) dragEvent.getLocalState();
                //ViewGroup owner = (ViewGroup) v.getParent();
                //owner.removeView(v);//se crea un view conrespecto a la imagen que se mueve, para quitarlo del layout y añadirlo al siguiente
                container = (RelativeLayout) view;//se castea un linear layout de layout receptor
                container.addView(v);//añadimos la vista al contenedor que casteamos
                v.setVisibility(View.VISIBLE);// Visibility to VISIBLE
                v.setX(dragEvent.getX());
                v.setY(dragEvent.getY());
                //linea_com v.setTag(dragData);

                //ViewGroup contenedor1 = (ConstraintLayout) view.getParent();
                //ViewGroup contenedor2 = (LinearLayout) contenedor1.getParent();
                //ViewGroup contenedor3 = (CoordinatorLayout) contenedor2.getParent();
                //cerrarMenus(contenedor3);

                MoverVista moverVista = new MoverVista(v, v.getX(), v.getY());
                moverVista.comprobarPosicion(context);


                ConfiComponente confiComponente=new ConfiComponente(v,context);
                confiComponente.abrirDialogReferencia();


                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        v.setOnTouchListener(touch);
                        return false;
                    }
                });


                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (estadoComponentes.equalsIgnoreCase(ESPERA_ENTRADA)) {

                            if (v instanceof SalidaDigitalView){
                            //if (!dragData.equalsIgnoreCase(MainActivity.IMAGE_ENTRADA)) {
                                EntradaView entradaView = (EntradaView) vistaEnEspera;
                                SalidaView salidaView = (SalidaView) v;

                                //esta intrucción compara si el salida ya esta unida una entrada, y si es asi
                                //la elimina de esa entrada para poder conectarla a la otra
                                if (salidaView.isUnido()) {
                                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                                }

                                entradaView.addSalida(salidaView);
                                estadoComponentes = "";
                                quitarColorFilter((RelativeLayout)view.getParent());
                            } else
                                Toast.makeText(context, "Selecione una salida", Toast.LENGTH_SHORT).show();


                        } else if (estadoComponentes.equalsIgnoreCase(ESPERA_SALIDA)) {
                            if (v instanceof EntradaView){
                            //if (!dragData.equalsIgnoreCase(MainActivity.IMAGE_SALIDA)) {
                                SalidaView salidaView = (SalidaView) vistaEnEspera;
                                EntradaView entradaView = (EntradaView) v;

                                if (salidaView.isUnido()) {
                                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                                }
                                //salidaView.setEntrada(entradaView);
                                entradaView.addSalida(salidaView);

                                //salidaView.dibujarLinea(new PointF(vistaEnEspera.getX(),vistaEnEspera.getY()));
                                estadoComponentes = "";
                                quitarColorFilter((RelativeLayout)view.getParent());
                            } else
                                Toast.makeText(context, "Selecione una entrada", Toast.LENGTH_SHORT).show();


                        } else {
                            final DialogConfigItem dialog = new DialogConfigItem(context, view);
                            dialog.setContentView(R.layout.dialog_item_confi);

                            TextView textView=dialog.findViewById(R.id.id_referencia);
                            if (v instanceof EntradaDigitalView )
                            textView.setText(((EntradaDigitalView)view).getReferencia());

                            Button eliminar = dialog.findViewById(R.id.bton_eliminar);
                            eliminar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.eliminarVista();
                                    dialog.dismiss();
                                }
                            });

                            Button unir = dialog.findViewById(R.id.bton_unir);
                            unir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (v instanceof EntradaView){
                                    //if (dragData.equalsIgnoreCase(MainActivity.IMAGE_ENTRADA)) {
                                        estadoComponentes = ESPERA_ENTRADA;


                                    } else {
                                        estadoComponentes = ESPERA_SALIDA;
                                    }
                                    estadoDragAndDrop((ViewGroup) v.getParent());
                                    vistaEnEspera = v;
                                    dialog.dismiss();
                                }
                            });
                            dialog.findViewById(R.id.bton_guardar).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });


                            dialog.show();
                        }
                    }
                });


                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                // Does a getResult(), and displays what happened.
                //if (event.getResult())
                //Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();

                //else
                //Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();


                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
/*
    public void cerrarMenus(ViewGroup padre) {

        LinearLayout layoutEntradas = (LinearLayout) padre.findViewById(R.id.layout_entradas);
        LinearLayout layoutSalidad = padre.findViewById(R.id.layout_salidas);


        Animation animation;

        if (layoutEntradas.getVisibility() == View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(padre.getContext(), R.anim.desaparecer_left);
            animation.setFillAfter(true);
            layoutEntradas.startAnimation(animation);
            layoutEntradas.setVisibility(View.INVISIBLE);

        }
        if (layoutSalidad.getVisibility() == View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(padre.getContext(), R.anim.desaparecer_right);
            animation.setFillAfter(true);
            layoutSalidad.startAnimation(animation);
            layoutSalidad.setVisibility(View.INVISIBLE);
        }


    }
*/



    @TargetApi(23)
    public void estadoDragAndDrop(ViewGroup viewGroup){


        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v=viewGroup.getChildAt(i);

            if(estadoComponentes.equalsIgnoreCase(ESPERA_ENTRADA)){

                if (v instanceof EntradaView){
                //if (v.getTag().toString().equalsIgnoreCase(MainActivity.IMAGE_ENTRADA)){
                    EntradaView entradaView=(EntradaView)v;

                    entradaView.setColorFilter(context.getResources().getColor(R.color.fondo_imagen_omitir,null));



                }

            }else if (estadoComponentes.equalsIgnoreCase(ESPERA_SALIDA)){
                if (v instanceof SalidaView){
                //if (v.getTag().toString().equalsIgnoreCase(MainActivity.IMAGE_SALIDA)){
                    SalidaView salidaView=(SalidaView) v;

                    salidaView.setColorFilter(context.getResources().getColor(R.color.fondo_imagen_omitir,null));
                }
            }
        }

    }
    public void quitarColorFilter(RelativeLayout viewGroup){
        for (int i=0; i<viewGroup.getChildCount();i++){

            AppCompatImageView imageView=(AppCompatImageView)viewGroup.getChildAt(i);

            imageView.clearColorFilter();




        }
    }



    public String getEstadoComponentes(){
        return estadoComponentes;
    }
}



