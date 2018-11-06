package com.example.usuario.pracdraganddrop.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class Configuracion extends AppCompatActivity {

    private final static String ALEX="192.168.1.56";
   private String ip="nada", json;
   private ProgressBar bar;
   private boolean controller, controllerCatch=true;
   private TextView mensaje;
   private Button conectar, ingresar;
   private EditText editIp;
   private ImageView imageView;
   private Dialog dialogLoad;
   public static final String CLAVE="JSON";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        dialogLoad=new Dialog(this);
        dialogLoad.setContentView(R.layout.activity_configuracion);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        bar=dialogLoad.findViewById(R.id.progress_bar);
        mensaje= dialogLoad.findViewById(R.id.mensaje_usuario);
        imageView= dialogLoad.findViewById(R.id.image_usuario);
        conectar= dialogLoad.findViewById(R.id.boton_conectar);
        ingresar= dialogLoad.findViewById(R.id.boton_ip);
        editIp= dialogLoad.findViewById(R.id.text_ip);

        bar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);



        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            json= bundle.getString(CLAVE);
        }else{

            json="vacio";
        }

        dialogLoad.show();
        proceso();


    }



    public void proceso(){
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bar.setVisibility(View.INVISIBLE);
                pruebaInternet();
            }
        }, 2000);
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
                //socketProcess();
                if (enviaDatos(ip,1500+"",json)){
                    mensajes(true);
                }else{
                    mensajes(false);
                }
            }
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_no_wifi));
            imageView.setVisibility(View.VISIBLE);
            mensaje.setText("No se encuentra conexión a internet");

        }
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void socketProcess(){

        Toast.makeText(this, ip, Toast.LENGTH_SHORT).show();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*try {
                    Socket  socket= new Socket(ip, 777);
                    while(controllerCatch){
                        oos= new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(json);
                        socket.close();
                        controllerCatch= false;
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    try {
                        Socket socket=new Socket(ip,777);

                        DataOutputStream flujoSalida=new DataOutputStream(socket.getOutputStream());
                        flujoSalida.writeUTF(json);
                        socket.close();



                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }).start();


    }


    public boolean enviaDatos(String ip, String puerto, String json){
        Conectar conectar=new Conectar();
        conectar.execute(ip,puerto,json);


        try {
            return conectar.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;

    }
    private class Conectar extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            String ip=params[0];
            int puerto=Integer.parseInt(params[1]);
            String json=params[2];

            try {
                Socket socket=new Socket(ip,puerto);
                ObjectOutputStream salidaDatos=new ObjectOutputStream(socket.getOutputStream());
                salidaDatos.writeObject(json);

                socket.close();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mensajes(boolean comprobarEnvio){


        if(comprobarEnvio){
            mensaje.setText(json!="vacio" ?"Información enviada." : "Conexión exitosa." );
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(getDrawable(R.drawable.ic_check));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    iniciarDragAndDrop();
                }
            },2000);
        } else{
            fail();
            mensaje.setText("Error en la conexión. \n Verifique su conexión a internet o digite de nuevo la IP." );

        }
    }
    @TargetApi(21)
    public void iniciarDragAndDrop(){
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.desaparecer_centro);
        animation.setFillAfter(true);
        dialogLoad.findViewById(R.id.layout_configuracion).startAnimation(animation);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent= new Intent(getApplicationContext(), DragAndDropActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


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
