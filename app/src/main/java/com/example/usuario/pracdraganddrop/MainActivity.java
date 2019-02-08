package com.example.usuario.pracdraganddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;
import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout layoutEntradas;
    private Animation animation;
    private MyDragEventListener myDrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Linea linea=new Linea(this);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //setContentView(linea);
        findViews();
       implementEvents();



        /*RelativeLayout layout = findViewById(R.id.left_layout);
        BitmapDrawable fondo = (BitmapDrawable) layout.getBackground();
        Canvas canvas = new Canvas(fondo.getBitmap());
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.YELLOW);
        canvas.drawLine(0, 0, 100, 100, paint);*/
    }

    private void findViews() {


        layoutEntradas=findViewById(R.id.layout_entradas);



    }

    private void implementEvents() {

        //imageView.setOnLongClickListener(clickLong);
        //imagen2.setOnLongClickListener(clickLong);

        //imageView.setOnTouchListener(touch);
        //imagen2.setOnTouchListener(touch);


         myDrag=new MyDragEventListener(this);

        findViewById(R.id.left_layout).setOnDragListener(myDrag);

    }
    public View.OnTouchListener touch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:

                    //EntradaView entradaView=new EntradaView(getApplicationContext());
                    //SalidaView salidaView=new SalidaView(getApplicationContext());




                    //ImageView imagen2=new ImageView(getApplicationContext());
                    //imagen2.setImageDrawable(((ImageView)view).getDrawable());

                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                    //ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                    view.startDrag(null,shadowBuilder,getNuevoComponente(view),0);

                   /* if (view.getTag().toString().equalsIgnoreCase(IMAGE_ENTRADA)){
                        entradaView.setImageDrawable(((EntradaView)view).getDrawable());
                        view.startDrag(data//datos movidos
                                , shadowBuilder //sombra
                                , entradaView//imagen con la cual se hace el arrastre
                                , 0//flags
                        );
                    }else{
                        salidaView.setImageDrawable(((SalidaView)view).getDrawable());
                        view.startDrag(data//datos movidos
                                , shadowBuilder //sombra
                                , salidaView//imagen con la cual se hace el arrastre
                                , 0//flags
                        );
                    }
                   /* view.startDrag(data//datos movidos
                            , shadowBuilder //sombra
                            , imagen2//imagen con la cual se hace el arrastre
                            , 0//flags
                    );*/

                   cerrarMenus();
                    return true;


            }
            return false;
        }
    };

   public Object getNuevoComponente(View view){
       if (view instanceof EntradaDigitalView){
           EntradaDigitalView entradaView=new EntradaDigitalView(getApplicationContext());
           entradaView.setImageDrawable(((EntradaView)view).getDrawable());
           return entradaView;

       }else if(view instanceof SalidaDigitalView){
           SalidaDigitalView salidaView=new SalidaDigitalView(getApplicationContext());
           salidaView.setImageDrawable(((SalidaDigitalView)view).getDrawable());
           return salidaView;

       }
       EntradaDigitalView entradaView=new EntradaDigitalView(getApplicationContext());
       entradaView.setImageDrawable(((EntradaView)view).getDrawable());
       return entradaView;
   }

   public void entradasArraste(View.OnTouchListener touch){
       for (int i=0; i<layoutEntradas.getChildCount();i++){
           ViewGroup viewGroup=(LinearLayout)layoutEntradas.getChildAt(i);
           viewGroup.getChildAt(1).setOnTouchListener(touch);
       }
   }



    public void clickBotones(View v){
        switch (v.getId()){
            case R.id.bton_abrir_entradas:

                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.aparecer_left);
                animation.setFillAfter(true);
                layoutEntradas.startAnimation(animation);
                layoutEntradas.setVisibility(View.VISIBLE);
                layoutEntradas.setEnabled(true);
                entradasArraste(touch);
                break;

            case R.id.left_layout:
                cerrarMenus();
                break;

        }
    }

    public void cerrarMenus(){
        if (layoutEntradas.getVisibility()==View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.desaparecer_left);
            animation.setFillAfter(true);
            layoutEntradas.startAnimation(animation);
            layoutEntradas.setVisibility(View.INVISIBLE);

        }

        entradasArraste(null);

    }





}
