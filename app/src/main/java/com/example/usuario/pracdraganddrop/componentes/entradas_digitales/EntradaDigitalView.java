package com.example.usuario.pracdraganddrop.componentes.entradas_digitales;

import android.content.Context;
import android.util.AttributeSet;

import com.example.usuario.pracdraganddrop.componentes.EntradaView;

public class EntradaDigitalView extends EntradaView {

    private String referencia="";
    private int estado;

    public EntradaDigitalView(Context context) {
        super(context);
    }

    public EntradaDigitalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EntradaDigitalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
