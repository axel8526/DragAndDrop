package com.example.usuario.pracdraganddrop.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Configuracion extends AppCompatActivity {

    public static final String CLAVE = "JSON";
    public static final String CONEXION="CONECTADO";
    private String ip = "nada", json;
    private ProgressBar bar;
    private boolean controller;
    private TextView mensaje;
    private Button conectar, ingresar;
    private EditText editIp;
    private ImageView imageView;
    private Dialog dialogLoad;
    private Activity thisActivity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        thisActivity=this;
        dialogLoad = new Dialog(this);
        dialogLoad.setContentView(R.layout.activity_configuracion);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        bar = dialogLoad.findViewById(R.id.progress_bar);
        mensaje = dialogLoad.findViewById(R.id.mensaje_usuario);
        imageView = dialogLoad.findViewById(R.id.image_usuario);
        conectar = dialogLoad.findViewById(R.id.boton_conectar);
        ingresar = dialogLoad.findViewById(R.id.boton_ip);
        editIp = dialogLoad.findViewById(R.id.text_ip);

        bar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        bar.setVisibility(View.VISIBLE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            json = bundle.getString(CLAVE);
        } else {

            json = CONEXION;
        }



        dialogLoad.show();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = sharedPreferences.getString("IP", "Nada");
        proceso();

        dialogLoad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Intent intent=new Intent(getApplicationContext(),Configuracion.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    public void proceso() {
        //Toast.makeText(thisActivity, ip, Toast.LENGTH_SHORT).show();
        if(pruebaInternet()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enviaDatos(ip, 1500 + "", json);
                }
            },1000);

        }

    }


    public boolean pruebaInternet() {
        ConnectivityManager conexion = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conexion.getActiveNetworkInfo();

        if (network != null && network.isConnected()) {
            if (ip.equalsIgnoreCase("nada")) {
                fail();
                mensaje.setText("No se encuentra una IP registrada");

            } else {
                return true;
            }
        } else {
            bar.setVisibility(View.INVISIBLE);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_wifi));
            imageView.setVisibility(View.VISIBLE);
            mensaje.setText("No se encuentra conexión a internet");


        }
        return false;
    }
    CountDownTimer timer;
    Conectar conectarPi;
    public void enviaDatos(String ip, String puerto, String json) {
        conectarPi = new Conectar();
        conectarPi.execute(ip, puerto, json);

        timer= new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                Toast.makeText(thisActivity, "Error al conectar", Toast.LENGTH_SHORT).show();
                mensajes(false);
            }
        }.start();


    }


    public void mensajes(boolean comprobarEnvio) {


        if (comprobarEnvio) {
            bar.setVisibility(View.INVISIBLE);
            mensaje.setText(json.equalsIgnoreCase("CONECTADO") ? "Conexión exitosa" : "Información enviada.");
            imageView.setVisibility(View.VISIBLE);
            conectar.setVisibility(View.INVISIBLE);
            ingresar.setVisibility(View.INVISIBLE);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            new CountDownTimer(1500, 100) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    iniciarDragAndDrop();
                }
            }.start();


        } else {
            fail();
            mensaje.setText("ERROR\n1. Verifique su conexión a internet \n2. Verifique el servidor \n3. Digite nueva IP.  ");

        }
    }

    @TargetApi(21)
    public void iniciarDragAndDrop() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.desaparecer_centro);
        animation.setFillAfter(true);
        dialogLoad.findViewById(R.id.layout_configuracion).startAnimation(animation);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialogLoad.dismiss();
                Intent intent = new Intent(getApplicationContext(), DragAndDropActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void fail() {
        bar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fail));
        conectar.setVisibility(View.VISIBLE);
        ingresar.setVisibility(View.VISIBLE);
        editIp.setText(ip);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIp.setVisibility(View.VISIBLE);
                controller = true;
                ingresar.setEnabled(false);
            }
        });

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller) ip = editIp.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Configuracion.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("IP", ip);
        editor.apply();
    }

    private class Conectar extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            String ip = params[0];
            int puerto = Integer.parseInt(params[1]);
            String json = params[2];


           try {
               Socket socket;socket = new Socket(ip, puerto);
               ObjectOutputStream salidaDatos = new ObjectOutputStream(socket.getOutputStream());
               salidaDatos.writeObject(json);
               socket.close();


                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            timer.cancel();
            if(aBoolean) {
                Toast.makeText(thisActivity, json.equals(CONEXION) ? "Conexión exitosa!!" : "Enviado!", Toast.LENGTH_SHORT).show();
            }
            mensajes(aBoolean);
        }

    }
}
