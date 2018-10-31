package com.example.usuario.pracdraganddrop;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;

public class DialogConfigItem extends Dialog {

    private View itemView;

    public DialogConfigItem(@NonNull Context context, View itemView) {
        super(context);

        this.itemView=itemView;
    }

    public void eliminarVista(){
        if (itemView instanceof EntradaView){
            ((EntradaView)itemView).eleminarEntrada();
        }else{
            ((SalidaView)itemView).eliminarSalida();
        }
        ViewGroup viewGroup=(ViewGroup)itemView.getParent();
        viewGroup.removeView(itemView);
    }


}
