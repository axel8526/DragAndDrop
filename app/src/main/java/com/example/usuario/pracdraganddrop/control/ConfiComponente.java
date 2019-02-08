package com.example.usuario.pracdraganddrop.control;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;
import com.example.usuario.pracdraganddrop.dialogs.DialogDI;
import com.example.usuario.pracdraganddrop.dialogs.DialogDO;
import com.example.usuario.pracdraganddrop.interfaces.EstadoDragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;
import com.example.usuario.pracdraganddrop.utils.Utilities;

import java.util.ArrayList;

public class ConfiComponente {

    private String referencia;
    private int estado;
    private Dialog dialog;
    private Animation animation;
    private View view;
    private Context context;
    private RelativeLayout container;
    private Utilities utils;
    public ConfiComponente(View view, Context context){
        this.context=context;
        utils=new Utilities(context);
        this.view=view;
        container=(RelativeLayout)view.getParent();
    }

    public void abrirDialogReferencia(){

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_configuracion_com);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        utils.iniciarAnimacion((ViewGroup) dialog.findViewById(R.id.layout_referencia),0,dialog);


        final Spinner spinner=dialog.findViewById(R.id.config_spinner1);
        spinner.setAdapter(llenarSpinner());


        Button btonListo=dialog.findViewById(R.id.bton_ok);
        btonListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.iniciarAnimacion((LinearLayout) dialog.findViewById(R.id.layout_referencia),1,dialog);
            }
        });
        dialog.show();


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                asignarReferencia(spinner.getSelectedItem().toString());

            }
        });

       /*
        final AlertDialog.Builder dBuilder=new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vistaDialog= inflater.inflate(R.layout.dialog_configuracion_com,null);
        final Spinner spinner=(Spinner)vistaDialog.findViewById(R.id.config_spinner1);
        spinner.setAdapter(llenarSpinner());



        dBuilder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                asignarReferencia(spinner.getSelectedItem().toString());
            }
        });
        dBuilder.setView(vistaDialog);


        AlertDialog dialog=dBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/


    }

    public void opcionRealizar(final View viewClick, final ComponenteEstado componenteEstado){

        if (componenteEstado.getEstadoDragAndDrop()== DragAndDrop.ESPERA_ENTRADA){
           if(viewClick instanceof OutputCView){
                InputCView entradaView = (InputCView) componenteEstado.getVistaEsperando();
                OutputCView salidaView = (OutputCView) viewClick;

                //esta intrucción compara si el salida ya esta unida una entrada, y si es asi
                //la elimina de esa entrada para poder conectarla a la otra
                if (salidaView.isUnido()) {
                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                }

                entradaView.addOutput(salidaView);
                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESTADO_NORMAL);
            }else
                Toast.makeText(context, "Selecione una salida", Toast.LENGTH_SHORT).show();


        }else if(componenteEstado.getEstadoDragAndDrop()==DragAndDrop.ESPERA_SALIDA){

            if(viewClick instanceof InputCView){
                OutputCView salidaView = (OutputCView) componenteEstado.getVistaEsperando();
                InputCView entradaView = (InputCView) viewClick;

                if (salidaView.isUnido()) {
                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                }

                entradaView.addOutput(salidaView);

                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESTADO_NORMAL);
            }else{
                Toast.makeText(context, "Selecione una entrada", Toast.LENGTH_SHORT).show();
            }


        }else{

             if(viewClick instanceof InputCView){
                abrirDialogInput(viewClick,componenteEstado);
            }else if(viewClick instanceof OutputCView){
                abrirDialogOutput(viewClick,componenteEstado);
            }


        }
        ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);
    }
    private void abrirDialogOutput(View view,ComponenteEstado componenteEstado){
        DialogDO dialogDO=new DialogDO(context);
        dialogDO.abreDialogConfiguracionSalida(view,componenteEstado);
    }
    private void abrirDialogInput(View view,ComponenteEstado componenteEstado){
        DialogDI dialogDI=new DialogDI(context);
        dialogDI.abreDialogConfiguracionEntrada(view,componenteEstado);
    }

    private ArrayAdapter llenarSpinner(){

        //Spinner spinner=dialog.findViewById(R.id.config_spinner1);

        ArrayAdapter adapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,getReferencias(view));
        //spinner.setAdapter(adapter);

        return adapter;
    }


    private ArrayList<String> getReferencias(View view){
        String[] datos=null;

        if (view instanceof InputCView){
            InputCView input=(InputCView)view;

            switch (input.getReference()){
                case MenuComponentes.DI:
                    datos=context.getResources().getStringArray(R.array.digital_input);

                    break;
                case MenuComponentes.AIV:
                    break;
                case  MenuComponentes.AII:
                    break;
                case  MenuComponentes.BUTTON:
                    break;
            }


        } else if (view instanceof OutputCView){
            OutputCView output=(OutputCView) view;

            switch (output.getReference()){
                case MenuComponentes.DO:
                    datos=context.getResources().getStringArray(R.array.digital_output);
                    break;
                case MenuComponentes.AOV:
                    break;
                case  MenuComponentes.AOI:
                    break;
                case  MenuComponentes.LED:
                    break;
            }


        }
        return comprobarReferences(datos);
    }

    private ArrayList<String> comprobarReferences(String[] datos){

        ArrayList<String> llenarDatos=new ArrayList<>();
        for (int i=0; i<datos.length;i++){
            boolean comprueba=false;
            for (int j=0; j<container.getChildCount();j++){

                    if (container.getChildAt(j) instanceof InputCView){

                        InputCView input=(InputCView)container.getChildAt(j);

                        if (datos[i].equalsIgnoreCase(input.getReference())){
                            comprueba=true;
                            break;
                        }
                    }else if(container.getChildAt(j) instanceof OutputCView){
                        OutputCView output=(OutputCView) container.getChildAt(j);

                        if (datos[i].equalsIgnoreCase(output.getReference())){
                            comprueba=true;
                            break;
                        }
                    }
            }
            if (!comprueba){
                llenarDatos.add(datos[i]);
            }
        }
        return llenarDatos;

    }
    public void asignarReferencia(String referencia){

         if(view instanceof InputCView){
            ((InputCView)view).setReference(referencia);

        }else if(view instanceof OutputCView){
            ((OutputCView)view).setReference(referencia);
        }


    }



}
