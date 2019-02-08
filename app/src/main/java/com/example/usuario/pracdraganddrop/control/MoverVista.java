package com.example.usuario.pracdraganddrop.control;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;

import java.util.ArrayList;

public class MoverVista {

    private View view;
    private float moveX, moveY;
    private int maxWith, maxHeigh;
    private ViewGroup container;


    public MoverVista(View view, float moveX, float moveY) {
        this.view = view;
        this.moveX = moveX;
        this.moveY = moveY;

        container=(RelativeLayout)view.getParent();
        maxWith=container.getWidth();
        maxHeigh=container.getHeight();


    }
    //encargado de verificar las dimensiones del padre para que la vista no se salga
    public void calcularMovimiento() {
        if (container.getX() < moveX && container.getY() < moveY &&
                moveX + view.getWidth() < maxWith && moveY + view.getHeight() < maxHeigh) {

            view.setX(moveX);
            view.setY(moveY);



        }
    }
    //metodo para que la vista siga moviendose cuando el cursor esta fuera del contenedor
    public void verificarMovimiento(){
        calcularMovimiento();

        if (moveX<container.getX() && moveY<container.getY() ||
                moveX+view.getWidth()>maxWith && moveY+view.getHeight()>maxHeigh ||
                moveX+view.getWidth()>maxWith && moveY<container.getY() ||
                moveX<container.getX() && moveY+view.getHeight()>maxHeigh) {
        }else{
            if (moveX < container.getX() || moveX + view.getWidth() > maxWith) {
                view.setY(moveY);

            }
            if (moveY < container.getY() || moveY + view.getHeight() > maxHeigh) {
                view.setX(moveX);
            }
        }

        mueveLinea();

    }

    public void mueveLinea(){

        if (view instanceof InputCView){

            InputCView entradaView = (InputCView) view;
            entradaView.dibujarLineas();
        } else if(view instanceof  OutputCView){
            OutputCView salidaView = (OutputCView) view;
            salidaView.dibujarLinea(new PointF(view.getX(), view.getY()));
        }
    }
    public void comprobarPosicion(Context context){
        ArrayList<View> componentes=new ArrayList<>();

        //llenamos el arraylist con todos los componentes a excepcion de la vista que se esta moviendo
        for (int i=0; i<container.getChildCount();i++){
            if (view.getY()==container.getChildAt(i).getY() && view.getX()==container.getChildAt(i).getX()){

            }else{
                componentes.add(container.getChildAt(i));
            }

        }

        float tamViewX=moveX+view.getWidth();
        float tamViewY=moveY+view.getHeight();

        for (int i=0; i<componentes.size();i++){
            View comp=componentes.get(i);
            PointF pivot0=new PointF(comp.getX(),comp.getY());
            PointF pivot1=new PointF(comp.getX()+comp.getWidth(),comp.getY()+comp.getHeight());



                if (moveX > pivot0.x && moveY > pivot0.y && moveX < pivot1.x && moveY < pivot1.y
                        || tamViewX > pivot0.x && tamViewY > pivot0.y && tamViewX < pivot1.x && tamViewY < pivot1.y
                        || moveX > pivot0.x && tamViewY < pivot1.y && moveX < pivot1.x && tamViewY > pivot0.y
                        || tamViewX < pivot1.x && moveY > pivot0.y && tamViewX > pivot0.x && moveY < pivot1.y) {

                        ViewGroup padre=(RelativeLayout)view.getParent();

                        if (moveX+view.getWidth()>padre.getWidth()){
                            moveY=moveY-10;

                            if (moveY<padre.getY()){
                                moveX=padre.getX();
                                moveY=padre.getY();
                            }
                        }else{
                            moveX=moveX+10;
                        }

                    //Toast.makeText(context, moveX+"", Toast.LENGTH_SHORT).show();
                    comprobarPosicion(context);

                } else {
                    //Toast.makeText(context, "NO entro", Toast.LENGTH_SHORT).show();

                        view.setX(moveX);
                        view.setY(moveY);
                        mueveLinea();


                }
        }
    }



    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
