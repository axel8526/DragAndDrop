package com.example.usuario.pracdraganddrop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;

import java.io.IOException;
import java.net.Socket;

public class Configuracion extends AppCompatActivity {

   private String ip="nada";
    ProgressBar bar;
   private boolean controller, controllerCatch=true;
    TextView mensaje;
    Button conectar, ingresar;
    EditText editIp;
    ImageView imageView;
    Socket socket;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        bar= findViewById(R.id.progress_bar);
        mensaje= findViewById(R.id.mensaje_usuario);
        imageView= findViewById(R.id.image_usuario);
        conectar= findViewById(R.id.boton_conectar);
        ingresar= findViewById(R.id.boton_ip);
        editIp= findViewById(R.id.text_ip);

        proceso();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void proceso(){
        bar.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bar.setVisibility(View.INVISIBLE);
                conectarServicio();
            }
        }, 4000);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void conectarServicio(){
        if (ip.equalsIgnoreCase("nada")){
            fail();
            mensaje.setText("No se encuentra una IP registrada");
        } else {
            socketProcess();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void socketProcess(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                     socket = new Socket("192.168.0.18", 9999);
                    controllerCatch=false;
                }catch (IOException e){};

            }
        }).start();

        dontFall();

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void dontFall(){

        if(socket.isConnected()){
            mensaje.setText("Conexión exitosa. \n Abriendo Drag and Drop");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(getDrawable(R.drawable.ic_check));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= new Intent(getApplicationContext(), DragAndDropActivity.class);
                    startActivity(intent);

                }
            },2000);
        } else{
            fail();
            mensaje.setText("Error en la conexión. \n Verifique su conexión a internet o digite de nuevo la IP." );

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fail(){
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_fail));
        conectar.setVisibility(View.VISIBLE);
        ingresar.setVisibility(View.VISIBLE);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIp.setVisibility(View.VISIBLE);
                controller= true;
                ingresar.setEnabled(false);
            }
        });

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller) ip= editIp.getText().toString();

                Intent intent= new Intent(getApplicationContext(), Configuracion.class);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("IP", ip);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        ip= sharedPreferences.getString("IP", "Nada");


    }
}
