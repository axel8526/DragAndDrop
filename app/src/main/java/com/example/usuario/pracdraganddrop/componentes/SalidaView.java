package com.example.usuario.pracdraganddrop.componentes;

import android.content.Context;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.usuario.pracdraganddrop.R;

public class SalidaView extends AppCompatImageView {

    private boolean unido;
    private EntradaView entradaView;
    private LineaUnir lineaUnir;
    private final String TIPO="SALIDA";

    public SalidaView(Context context) {
        super(context);
        unido=false;
    }

    public SalidaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SalidaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUnido(boolean estado){
        unido=estado;
    }

    public void dibujarLinea(PointF inicio){
        ViewGroup viewGroup=(RelativeLayout)this.getParent();
        ViewGroup contenedorPadre=(ConstraintLayout)viewGroup.getParent();
        ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);



        if (lineaUnir!=null){
            containerLines.removeView(lineaUnir);
        }
        if (entradaView!=null) {

            int width=entradaView.getWidth()/2;
            int height=entradaView.getHeight()/2;

            int width2=this.getWidth()/2;
            int height2=this.getHeight()/2;

            inicio.x=inicio.x+width2;
            inicio.y=inicio.y+height2;

            PointF entrada = new PointF(entradaView.getX()+width, entradaView.getY()+height);
            lineaUnir = new LineaUnir(viewGroup.getContext(), inicio, entrada);

            containerLines.addView(lineaUnir);
        }
    }
    public LineaUnir getLineaUnir(){
        return lineaUnir;
    }
    public void setEntrada(EntradaView entradaView){
        this.entradaView=entradaView;
    }
    public EntradaView getEntradaView(){
        return entradaView;
    }
    public void eliminarSalida(){
        ViewGroup container=(RelativeLayout)this.getParent();
        ViewGroup contenedorPadre=(ConstraintLayout)container.getParent();
        ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);
        if (unido){

            containerLines.removeView(lineaUnir);
            entradaView.eliminarUnaSalida(this);
            unido=false;
        }

    }
    public boolean isUnido(){
        return unido;
    }

    private String getTIPO(){
        return TIPO;
    }
}
