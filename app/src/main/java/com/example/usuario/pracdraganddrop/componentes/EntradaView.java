package com.example.usuario.pracdraganddrop.componentes;

import android.content.Context;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.R;

import java.util.ArrayList;


public class EntradaView extends AppCompatImageView  {


    private ArrayList<SalidaView> salidaViews;
    private boolean conectado;
    private final String TIPO = "ENTRADA";



    public EntradaView(Context context) {
        super(context);
        salidaViews = new ArrayList<>();
    }

    public EntradaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EntradaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addSalida(SalidaView salidaView) {
        salidaViews.add(salidaView);
        conectado = true;
        dibujarUltimaLinea();
    }

    public void dibujarLineas() {
        for (int i = 0; i < salidaViews.size(); i++) {
            SalidaView salidaView = salidaViews.get(i);
            salidaView.setEntrada(this);
            salidaView.setUnido(true);

            PointF posicionSalida = new PointF(salidaView.getX(), salidaView.getY());
            salidaView.dibujarLinea(posicionSalida);

        }
    }


    public void dibujarUltimaLinea() {
        SalidaView salidaView = salidaViews.get(salidaViews.size() - 1);
        salidaView.setEntrada(this);
        salidaView.setUnido(true);

        PointF posicionSalida = new PointF(salidaView.getX(), salidaView.getY());
        salidaView.dibujarLinea(posicionSalida);
    }

    public void eleminarEntrada() {
        ViewGroup container = (RelativeLayout) this.getParent();
        ViewGroup contenedorPadre=(ConstraintLayout)container.getParent();
        ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);

        if (conectado) {
            for (int i = 0; i < salidaViews.size(); i++) {
                containerLines.removeView(salidaViews.get(i).getLineaUnir());
                salidaViews.get(i).setUnido(false);
                salidaViews.get(i).setEntrada(null);
            }

        }
    }
    public void eliminarUnaSalida(SalidaView salidaView){
        int index=-1;
        for (int i=0; i<salidaViews.size();i++){
            if (salidaView.getX()==salidaViews.get(i).getX() && salidaView.getY()==salidaViews.get(i).getY())index=i;
        }
        salidaViews.remove(index);
        dibujarLineas();


    }
    public String getTIPO(){
        return TIPO;
    }



}



