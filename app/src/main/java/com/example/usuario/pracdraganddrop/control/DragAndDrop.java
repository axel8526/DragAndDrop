package com.example.usuario.pracdraganddrop.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
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

    //inicia codigo para clase con patron singleton

    private static DragAndDrop dragAndDrop;
    private RelativeLayout layoutLineas;

    private DragAndDrop(){

    }

    public static DragAndDrop getInstance(){
        if(dragAndDrop==null){
            dragAndDrop=new DragAndDrop();
        }

        return dragAndDrop;
    }
    public void setContext(Context context){
        this.context=context;
    }

    public void startDragAndDrop(){
        activarReceptor();
        componenteEstado=new ComponenteEstado();
        componenteEstado.setEstadoDragAndDrop(ESTADO_NORMAL);
    }

    public void setLayoutComponentes(RelativeLayout layoutDragAndDrop) {
        this.layoutDragAndDrop = layoutDragAndDrop;


    }

    public void setLayoutLineas(RelativeLayout layoutLineas) {
        this.layoutLineas = layoutLineas;
    }
    public ViewGroup getLayoutComponentes(){
        return layoutDragAndDrop;
    }

    public ViewGroup getLayoutLines(){
        return layoutLineas;
    }
    //termina codigo de para clase con patron singleton////////

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

                    int tamVista=(int)(60*context.getResources().getDisplayMetrics().density);
                    ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(tamVista,tamVista);



                    v.setLayoutParams(params);

                    RelativeLayout container = (RelativeLayout) view;//se castea un linear layout de layout receptor
                    container.addView(v);//añadimos la vista al contenedor que casteamos
                    v.setVisibility(View.VISIBLE);// Visibility to VISIBLE
                    v.setX(dragEvent.getX());
                    v.setY(dragEvent.getY());

                   // ((InputCView)v).setReference("AI_V1");


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
                System.out.println("onclick");

            }
        });
    }


    @TargetApi(23)
    public void estadoDragAndDrop(ComponenteEstado estado){
        componenteEstado=estado;

        for (int i = 0; i < layoutDragAndDrop.getChildCount(); i++) {
            View v=layoutDragAndDrop.getChildAt(i);

            if(componenteEstado.getEstadoDragAndDrop()==ESPERA_ENTRADA){

                if(v instanceof InputCView){
                    InputCView input=(InputCView)v;
                    input.setColorFilterImage(R.color.fondo_imagen_omitir);
                }

            }else if (componenteEstado.getEstadoDragAndDrop()==ESPERA_SALIDA){
                if(v instanceof OutputCView){
                    OutputCView output=(OutputCView)v;
                    output.setColorFilterImage(R.color.fondo_imagen_omitir);
                }
            }else{
                 if(v instanceof InputCView ){
                    InputCView input=(InputCView)v;
                    input.clearColorFilter();
                }else if(v instanceof OutputCView ){
                    OutputCView input=(OutputCView)v;
                    input.clearColorFilter();
                }
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

}
