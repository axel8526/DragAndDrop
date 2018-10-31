package com.example.usuario.pracdraganddrop.modelos;

import android.view.View;

public class ComponenteEstado {

    private View vistaEsperando;
    private int estadoDragAndDrop;

    public ComponenteEstado(View vistaEsperando, int estadoDragAndDrop) {
        this.vistaEsperando = vistaEsperando;
        this.estadoDragAndDrop = estadoDragAndDrop;
    }
    public ComponenteEstado(){

    }

    public View getVistaEsperando() {
        return vistaEsperando;
    }

    public void setVistaEsperando(View vistaEsperando) {
        this.vistaEsperando = vistaEsperando;
    }

    public int getEstadoDragAndDrop() {
        return estadoDragAndDrop;
    }

    public void setEstadoDragAndDrop(int estadoDragAndDrop) {
        this.estadoDragAndDrop = estadoDragAndDrop;
    }
}
