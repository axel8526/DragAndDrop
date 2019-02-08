package com.example.usuario.pracdraganddrop.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;
import com.example.usuario.pracdraganddrop.interfaces.EstadoDragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;
import com.example.usuario.pracdraganddrop.modelos.ConfigDO;
import com.example.usuario.pracdraganddrop.utils.Utilities;

public class DialogDO extends Dialog {

    private Context context;
    private Spinner spinnerEstado, spinnerEntrada, spinnerSalida;
    private TextView textReferencia,nombreEntrada, nombreSalida;
    private CheckBox habilitarIntervalo;
    private EditText numIntervalo;
    private ImageView imageViewIcono;

    public DialogDO(@NonNull Context context) {
        super(context);
        this.context=context;

        setContentView(R.layout.dialog_item_confi);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();
    }

    private void findViews(){
        habilitarIntervalo=findViewById(R.id.dialog_confi_habilitar_intervalo);
        numIntervalo=findViewById(R.id.dialog_confi_intervalo_edtext);
        imageViewIcono=findViewById(R.id.dialog_confi_icono);
        textReferencia=findViewById(R.id.dialog_confi_nombre);
        spinnerEstado=findViewById(R.id.confi_salida_spinner1);
        spinnerEntrada=findViewById(R.id.confi_spinner2);
        spinnerSalida=findViewById(R.id.confi_spinner3);
        nombreEntrada=findViewById(R.id.confi_nombre_entrada);
        nombreSalida=findViewById(R.id.confi_nombre_salida);
    }

    public void abreDialogConfiguracionSalida(final View viewClick, final ComponenteEstado componenteEstado){
        final OutputCView output=(OutputCView)viewClick;

        imageViewIcono.setImageResource(output.getImageResouce());
        textReferencia.setText(output.getReference());

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


        spinnerEstado.setSelection(output.getValue());

        if (output.isUnido()){
            if (output.getConfigDO()==null){
                output.setConfigDO(new ConfigDO(0,0));
            }
            findViewById(R.id.confi_contenedor_intervalo).getLayoutParams().height=0;
            output.setIntervalo(false);


            findViewById(R.id.contenedor_configuracion).getLayoutParams().height=
                    LinearLayout.LayoutParams.WRAP_CONTENT;
            spinnerEntrada.setSelection(output.getConfigDO().getEstadoEntrada());
            spinnerSalida.setSelection(output.getConfigDO().getEstadoSalida());
            nombreEntrada.setText(((InputCView)output.getEntradaView()).getReference());
            nombreSalida.setText(output.getReference());
        }else{
            findViewById(R.id.contenedor_configuracion).getLayoutParams().height=0;
            findViewById(R.id.confi_contenedor_intervalo).getLayoutParams().height=
                    LinearLayout.LayoutParams.WRAP_CONTENT;

            output.setIntervalo(true);
        }

        if (output.isIntervalo()){
            habilitarIntervalo.setChecked(true);
            numIntervalo.setText(output.getTiempoIntervalo()+"");
        }else{
            habilitarIntervalo.setChecked(false);
            numIntervalo.setEnabled(false);
            numIntervalo.setText("");
        }


        findViewById(R.id.bton_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                output.setConfigDO(null);
                output.eliminarSalida();
                ((ViewGroup)output.getParent()).removeView(output);
                new Utilities(context).iniciarAnimacion((ViewGroup) findViewById(R.id.layout_salida_digital),1,DialogDO.this);


            }
        });

        findViewById(R.id.bton_unir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                componenteEstado.setEstadoDragAndDrop(DragAndDrop.ESPERA_SALIDA);
                componenteEstado.setVistaEsperando(viewClick);
                ((EstadoDragAndDrop)context).estadoDragAndDrop(componenteEstado);

                new Utilities(context).iniciarAnimacion((ViewGroup) findViewById(R.id.layout_salida_digital),1,DialogDO.this);


            }
        });

        findViewById(R.id.bton_guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utilities(context).iniciarAnimacion((ViewGroup) findViewById(R.id.layout_salida_digital),1,DialogDO.this);

            }
        });


        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int estado=spinnerEstado.getSelectedItemPosition();
                ConfigDO configDO=new ConfigDO(spinnerEntrada.getSelectedItemPosition(),spinnerSalida.getSelectedItemPosition());

                if (output.isUnido())output.setConfigDO(configDO);
                else output.setConfigDO(null);

                if (habilitarIntervalo.isChecked()){
                    output.setIntervalo(true);

                    if (numIntervalo.getText().toString().isEmpty()){
                        output.setTiempoIntervalo(10000);

                    }else{
                        output.setTiempoIntervalo(Long.parseLong(numIntervalo.getText().toString().trim()));
                    }

                }
                else {
                    output.setIntervalo(false);
                    output.setTiempoIntervalo(0);
                }

                output.setValue(estado);
            }
        });
        new Utilities(context).iniciarAnimacion((ViewGroup) findViewById(R.id.layout_salida_digital),0,DialogDO.this);
        show();
    }


}
