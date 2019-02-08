package com.example.usuario.pracdraganddrop.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;
import com.example.usuario.pracdraganddrop.interfaces.EstadoDragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;
import com.example.usuario.pracdraganddrop.utils.Utilities;

public class DialogDI extends Dialog {

    private ImageView imageView;
    private TextView textNombre;
    private Context context;

    public DialogDI(@NonNull Context context) {
        super(context);
        this.context=context;

        setContentView(R.layout.dialog_confi_entradas);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();
    }
    public void findViews(){
        imageView=findViewById(R.id.dialog_confi_icono);
        textNombre=findViewById(R.id.dialog_confi_nombre);
    }
    public void abreDialogConfiguracionEntrada(final View viewClick, final ComponenteEstado componenteEstado){
        final InputCView input=(InputCView) viewClick;



        imageView.setImageResource(input.getImageResouce());
        textNombre.setText(input.getReference());

        findViewById(R.id.bton_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               input.eliminarInput();
                ((ViewGroup)input.getParent()).removeView(input);
               new Utilities(context).iniciarAnimacion((LinearLayout) findViewById(R.id.layout_entradas_digital),1,DialogDI.this);


            }
        });

        findViewById(R.id.bton_unir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESPERA_ENTRADA);
                componenteEstado.setVistaEsperando(viewClick);
                ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);


                new Utilities(context).iniciarAnimacion((LinearLayout) findViewById(R.id.layout_entradas_digital),1,DialogDI.this);


            }
        });

        findViewById(R.id.bton_guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utilities(context).iniciarAnimacion((LinearLayout) findViewById(R.id.layout_entradas_digital),1,DialogDI.this);

            }
        });
        new Utilities(context).iniciarAnimacion((LinearLayout) findViewById(R.id.layout_entradas_digital),0,DialogDI.this);
        show();
    }
}
