package com.example.usuario.pracdraganddrop.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;
import com.example.usuario.pracdraganddrop.control.MenuComponentes;
import com.example.usuario.pracdraganddrop.control.OrganizarJson;
import com.example.usuario.pracdraganddrop.interfaces.EstadoDragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ComponenteEstado;
import com.google.gson.Gson;

public class DragAndDropActivity extends AppCompatActivity implements EstadoDragAndDrop {


    private MenuComponentes menuComponentes;
    private DragAndDrop dragAndDrop;
    private RelativeLayout layoutDragAndDrop, layoutLines;
    private LinearLayout layoutComponente;

    private Button btonAbrirMenu, btonCancelar, btonEnviar;

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        findViews();



    }

    public void onResume(){
        super.onResume();

        iniciarControles();

    }

    public void findViews() {
        layoutComponente = findViewById(R.id.layout_entradas);
        layoutDragAndDrop = findViewById(R.id.left_layout);
        layoutLines=findViewById(R.id.layou_lines);

        btonAbrirMenu = findViewById(R.id.bton_abrir_entradas);
        btonCancelar = findViewById(R.id.bton_cancelar);
        btonEnviar = findViewById(R.id.bton_enviar);
    }

    public void iniciarControles() {
        menuComponentes = new MenuComponentes(this, layoutComponente);
        menuComponentes.addInputs(new InputCView(this,MenuComponentes.DI,R.drawable.icon_sensor));
        menuComponentes.addOutput(new OutputCView(this,MenuComponentes.DO,R.drawable.icon_salida));

        //se comenta linea para probar patron singleton
        //dragAndDrop = new DragAndDrop(this, layoutDragAndDrop);

        dragAndDrop=DragAndDrop.getInstance();
        dragAndDrop.setContext(this);
        dragAndDrop.setLayoutComponentes(layoutDragAndDrop);
        dragAndDrop.setLayoutLineas(layoutLines);
        dragAndDrop.startDragAndDrop();


    }
@TargetApi(21)
    public void clickBotones(View view) {

        Intent intent=new Intent(this,Configuracion.class);
        switch (view.getId()) {
            case R.id.bton_abrir_entradas:

                menuComponentes.abrirMenu(dragAndDrop.getComponentes());

                break;
            case R.id.left_layout:
                menuComponentes.cerrarMenus();

                break;

            case R.id.bton_cancelar:
                estadoDragAndDrop(new ComponenteEstado(null, DragAndDrop.ESTADO_NORMAL));
                break;

            case R.id.bton_enviar:
                OrganizarJson organizarJson=new OrganizarJson(this);
                String json=organizarJson.getJson(dragAndDrop.getComponentes());

                if(json.length()==2){
                    Toast.makeText(this, "Coloca componentes", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Toast.makeText(this,json, Toast.LENGTH_LONG).show();
                //Toast.makeText(this,organizarJson.getJson(dragAndDrop.getSalidasDigitales()), Toast.LENGTH_LONG).show();



                intent.putExtra(Configuracion.CLAVE,json);
                startActivity(intent);
                //finish();

                break;

            case R.id.bton_stop:
                intent.putExtra(Configuracion.CLAVE,"STOP");
                startActivity(intent);

                break;
        }

    }

    @Override
    public void estadoDragAndDrop(ComponenteEstado estado) {
        dragAndDrop.estadoDragAndDrop(estado);
        menuComponentes.cerrarMenus();

        if (estado.getEstadoDragAndDrop() != DragAndDrop.ESTADO_NORMAL) {

            estadoBoton(btonAbrirMenu,getResources().getColor(R.color.color_inhabilitar),false);
            estadoBoton(btonEnviar,getResources().getColor(R.color.color_inhabilitar),false);


            btonCancelar.setEnabled(true);
            btonCancelar.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.aparecer_centro);
            animation.setFillAfter(true);
            btonCancelar.startAnimation(animation);


            layoutDragAndDrop.getBackground().setColorFilter(getResources().getColor(R.color.color_filter_dad), PorterDuff.Mode.MULTIPLY);
            layoutDragAndDrop.invalidate();

        } else {
            estadoBoton(btonAbrirMenu,0,true);
            estadoBoton(btonEnviar,0,true);


            btonCancelar.setEnabled(false);
            if (btonCancelar.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.desaparecer_right);
                animation.setFillAfter(true);
                btonCancelar.startAnimation(animation);
            }
            btonCancelar.setVisibility(View.INVISIBLE);


            layoutDragAndDrop.getBackground().clearColorFilter();
            layoutDragAndDrop.invalidate();

        }
    }
    public void estadoBoton(Button button, int color, boolean estado){
        if (!estado) {
            button.setEnabled(estado);
            button.setTextColor(color);
            button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            button.invalidate();
        }else{
            button.setEnabled(estado);

            button.setTextColor(getResources().getColor(android.R.color.white));
            button.getBackground().clearColorFilter();
            button.invalidate();
        }
    }
}
