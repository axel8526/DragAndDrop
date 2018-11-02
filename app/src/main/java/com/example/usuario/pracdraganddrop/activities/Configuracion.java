package com.example.usuario.pracdraganddrop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Configuracion extends AppCompatActivity {

   private String ip="nada", json;
   private ProgressBar bar;
   private boolean controller, controllerCatch=true;
   private TextView mensaje;
   private Button conectar, ingresar;
   private EditText editIp;
   private ImageView imageView;
   public static final String CLAVE="JSON";

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

        Bundle bundle= getIntent().getExtras();
        json= bundle.getString(CLAVE);

    }



    public void proceso(){
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bar.setVisibility(View.INVISIBLE);
                pruebaInternet();
            }
        }, 3000);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pruebaInternet(){
        ConnectivityManager conexion = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conexion.getActiveNetworkInfo();

        if(network !=null && network.isConnected()){
            if (ip.equalsIgnoreCase("nada")){
                fail();
                mensaje.setText("No se encuentra una IP registrada");
            } else {
                socketProcess();
            }
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_no_wifi));
            imageView.setVisibility(View.VISIBLE);
            mensaje.setText("No se encuentra conexión a internet");

        }
    }


    ObjectOutputStream oos;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void socketProcess(){

        while (controllerCatch){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    Socket  socket= new Socket(ip, 1500);
                        oos= new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(json);
                        socket.close();
                        controllerCatch= false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

       mensajes();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mensajes(){


        if(oos!=null){
            mensaje.setText("Conexión exitosa. \n Información enviada.");
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
