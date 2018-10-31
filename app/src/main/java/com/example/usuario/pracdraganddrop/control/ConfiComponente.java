package com.example.usuario.pracdraganddrop.control;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.DialogConfigItem;
import com.example.usuario.pracdraganddrop.Linea;
import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;
import com.example.usuario.pracdraganddrop.interfaces.EstadoDragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;
import com.example.usuario.pracdraganddrop.modelos.ConfigDO;

import java.util.ArrayList;

public class ConfiComponente {

    private String referencia;
    private int estado;
    private Dialog dialog;
    private Animation animation;
    private View view;
    private Context context;
    private RelativeLayout container;
    public ConfiComponente(View view, Context context){
        this.context=context;
        this.view=view;
        container=(RelativeLayout)view.getParent();
    }

    public void abrirDialogReferencia(){

        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_configuracion_com);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        iniciarAnimacion((ViewGroup) dialog.findViewById(R.id.layout_referencia),0,dialog);


        final Spinner spinner=dialog.findViewById(R.id.config_spinner1);
        spinner.setAdapter(llenarSpinner());


        Button btonListo=dialog.findViewById(R.id.bton_ok);
        btonListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarAnimacion((LinearLayout) dialog.findViewById(R.id.layout_referencia),1,dialog);
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
            if (viewClick instanceof SalidaView){

                EntradaView entradaView = (EntradaView) componenteEstado.getVistaEsperando();
                SalidaView salidaView = (SalidaView) viewClick;

                //esta intrucción compara si el salida ya esta unida una entrada, y si es asi
                //la elimina de esa entrada para poder conectarla a la otra
                if (salidaView.isUnido()) {
                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                }

                entradaView.addSalida(salidaView);
                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESTADO_NORMAL);

            } else
                Toast.makeText(context, "Selecione una salida", Toast.LENGTH_SHORT).show();


        }else if(componenteEstado.getEstadoDragAndDrop()==DragAndDrop.ESPERA_SALIDA){

            if (viewClick instanceof EntradaView){

                SalidaView salidaView = (SalidaView) componenteEstado.getVistaEsperando();
                EntradaView entradaView = (EntradaView) viewClick;

                if (salidaView.isUnido()) {
                    salidaView.getEntradaView().eliminarUnaSalida(salidaView);
                }

                entradaView.addSalida(salidaView);
                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESTADO_NORMAL);

            } else
                Toast.makeText(context, "Selecione una entrada", Toast.LENGTH_SHORT).show();

        }else{

            if (viewClick instanceof SalidaView){
                abreDialogConfiguracionSalida(viewClick,componenteEstado);
            }else if (viewClick instanceof EntradaView){
                abreDialogConfiguracionEntrada(viewClick,componenteEstado);
            }


        }
        ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);
    }
    private void abreDialogConfiguracionSalida(final View viewClick, final ComponenteEstado componenteEstado){
        final SalidaDigitalView salidaView=(SalidaDigitalView)viewClick;

        final DialogConfigItem dialogConfigItem=new DialogConfigItem(context,viewClick);
        dialogConfigItem.setContentView(R.layout.dialog_item_confi);
        dialogConfigItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        final Spinner spinnerEstado, spinnerEntrada, spinnerSalida;
        TextView textReferencia,nombreEntrada, nombreSalida;
        final CheckBox habilitarIntervalo=dialogConfigItem.findViewById(R.id.dialog_confi_habilitar_intervalo);
        final EditText numIntervalo=dialogConfigItem.findViewById(R.id.dialog_confi_intervalo_edtext);
        ImageView imageViewIcono=dialogConfigItem.findViewById(R.id.dialog_confi_icono);
        textReferencia=dialogConfigItem.findViewById(R.id.dialog_confi_nombre);
        spinnerEstado=dialogConfigItem.findViewById(R.id.confi_salida_spinner1);
        spinnerEntrada=dialogConfigItem.findViewById(R.id.confi_spinner2);
        spinnerSalida=dialogConfigItem.findViewById(R.id.confi_spinner3);
        nombreEntrada=dialogConfigItem.findViewById(R.id.confi_nombre_entrada);
        nombreSalida=dialogConfigItem.findViewById(R.id.confi_nombre_salida);

        imageViewIcono.setImageDrawable(((ImageView)viewClick).getDrawable());
        textReferencia.setText(salidaView.getReferencia());

        habilitarIntervalo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    numIntervalo.setEnabled(true);
                    numIntervalo.setText("10000");

                }else{
                    numIntervalo.setEnabled(false);
                    numIntervalo.setText("");
                }
            }
        });


        spinnerEstado.setSelection(salidaView.getEstado());

        if (salidaView.isIntervalo()){
            habilitarIntervalo.setChecked(true);
            numIntervalo.setText(salidaView.getTiempoIntervalo()+"");
        }else{
            habilitarIntervalo.setChecked(false);
            numIntervalo.setEnabled(false);
            numIntervalo.setText("");
        }

        if (salidaView.isUnido()){
            if (salidaView.getConfigDO()==null){
                salidaView.setConfigDO(new ConfigDO(0,0));
            }
            dialogConfigItem.findViewById(R.id.contenedor_configuracion).getLayoutParams().height=
                    LinearLayout.LayoutParams.WRAP_CONTENT;
            spinnerEntrada.setSelection(salidaView.getConfigDO().getEstadoEntrada());
            spinnerSalida.setSelection(salidaView.getConfigDO().getEstadoSalida());
            nombreEntrada.setText(((EntradaDigitalView)salidaView.getEntradaView()).getReferencia());
            nombreSalida.setText(salidaView.getReferencia());
        }else{
            dialogConfigItem.findViewById(R.id.contenedor_configuracion).getLayoutParams().height=0;
        }






        dialogConfigItem.findViewById(R.id.bton_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salidaView.setConfigDO(null);
                dialogConfigItem.eliminarVista();
                iniciarAnimacion((ViewGroup) dialogConfigItem.findViewById(R.id.layout_salida_digital),1,dialogConfigItem);

            }
        });

        dialogConfigItem.findViewById(R.id.bton_unir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESPERA_SALIDA);
                    componenteEstado.setVistaEsperando(viewClick);
                    ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);

                iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_salida_digital),1,dialogConfigItem);


            }
        });

        dialogConfigItem.findViewById(R.id.bton_guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarAnimacion((ViewGroup) dialogConfigItem.findViewById(R.id.layout_salida_digital),1,dialogConfigItem);

            }
        });

        dialogConfigItem.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int estado=spinnerEstado.getSelectedItemPosition();
                ConfigDO configDO=new ConfigDO(spinnerEntrada.getSelectedItemPosition(),spinnerSalida.getSelectedItemPosition());

                if (salidaView.isUnido())salidaView.setConfigDO(configDO);
                else salidaView.setConfigDO(null);

                if (habilitarIntervalo.isChecked()){
                    salidaView.setIntervalo(true);

                    if (numIntervalo.getText().toString().isEmpty()){
                       salidaView.setTiempoIntervalo(10000);

                    }else{
                        salidaView.setTiempoIntervalo(Long.parseLong(numIntervalo.getText().toString().trim()));
                    }

                }
                else {
                    salidaView.setIntervalo(false);
                    salidaView.setTiempoIntervalo(0);
                }

                salidaView.setEstado(estado);
            }
        });
        iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_salida_digital),0,dialogConfigItem);
        dialogConfigItem.show();
    }
    private void abreDialogConfiguracionEntrada(final View viewClick, final ComponenteEstado componenteEstado){
        EntradaDigitalView entradaDigitalView=(EntradaDigitalView)viewClick;

        final DialogConfigItem dialogConfigItem=new DialogConfigItem(context,viewClick);
        dialogConfigItem.setContentView(R.layout.dialog_confi_entradas);
        dialogConfigItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imageView=dialogConfigItem.findViewById(R.id.dialog_confi_icono);
        imageView.setImageDrawable(((ImageView)viewClick).getDrawable());

        TextView textNombre=dialogConfigItem.findViewById(R.id.dialog_confi_nombre);
        textNombre.setText(entradaDigitalView.getReferencia());

        dialogConfigItem.findViewById(R.id.bton_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfigItem.eliminarVista();
                iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_entradas_digital),1,dialogConfigItem);

            }
        });

        dialogConfigItem.findViewById(R.id.bton_unir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESPERA_ENTRADA);
                    componenteEstado.setVistaEsperando(viewClick);
                    ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);


                iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_entradas_digital),1,dialogConfigItem);


            }
        });

        dialogConfigItem.findViewById(R.id.bton_guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_entradas_digital),1,dialogConfigItem);

            }
        });
        iniciarAnimacion((LinearLayout) dialogConfigItem.findViewById(R.id.layout_entradas_digital),0,dialogConfigItem);
        dialogConfigItem.show();
    }

    private ArrayAdapter llenarSpinner(){

        //Spinner spinner=dialog.findViewById(R.id.config_spinner1);

        ArrayAdapter adapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,getReferencias(view));
        //spinner.setAdapter(adapter);

        return adapter;
    }
    private ArrayList<String> getReferencias(View view){
        String[] datos;
        ArrayList<String> llenarDatos=new ArrayList<>();


        if (view instanceof EntradaDigitalView){
            datos=context.getResources().getStringArray(R.array.digital_input);

            for (int i=0; i<datos.length;i++){
                boolean comprueba=false;
                for (int j=0; j<container.getChildCount();j++){

                    if (container.getChildAt(j) instanceof EntradaDigitalView){
                        if (datos[i].equalsIgnoreCase(((EntradaDigitalView)container.getChildAt(j)).getReferencia())){
                            comprueba=true;
                        }
                    }
                }
                if (!comprueba){
                    llenarDatos.add(datos[i]);
                }
            }

        }else if(view instanceof SalidaDigitalView){
            datos=context.getResources().getStringArray(R.array.digital_output);
            for (int i=0; i<datos.length;i++){
                boolean comprueba=false;
                for (int j=0; j<container.getChildCount();j++){

                    if (container.getChildAt(j) instanceof SalidaDigitalView){
                        if (datos[i].equalsIgnoreCase(((SalidaDigitalView)container.getChildAt(j)).getReferencia())){
                            comprueba=true;
                        }
                    }
                }
                if (!comprueba){
                    llenarDatos.add(datos[i]);
                }
            }
        }


        return llenarDatos;
    }
    public void asignarReferencia(String referencia){

        if (view instanceof EntradaDigitalView){
            EntradaDigitalView entrada=(EntradaDigitalView)view;
            entrada.setReferencia(referencia);
        }else if (view instanceof SalidaDigitalView){
            SalidaDigitalView salida=(SalidaDigitalView)view;
            salida.setReferencia(referencia);
        }

    }

    public void iniciarAnimacion(ViewGroup viewGroup, int action, final Dialog dialog){
        if (action==0){
            animation=AnimationUtils.loadAnimation(context,R.anim.aparecer_centro);
            viewGroup.startAnimation(animation);

        }else{
            animation=AnimationUtils.loadAnimation(context,R.anim.desaparecer_right);
            viewGroup.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
