package com.example.usuario.pracdraganddrop.componentes;

import android.content.Context;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usuario.pracdraganddrop.R;
import com.example.usuario.pracdraganddrop.control.DragAndDrop;
import com.example.usuario.pracdraganddrop.modelos.ConfigDO;

public class OutputCView extends LinearLayout {

    private String reference;
    private int imageResouce;
    private TextView textView;
    private ImageView imageView;
    private Context context;

    private boolean unido;
    private InputCView inputView;
    private LineaUnir lineaUnir;


    //atributos que pertenecia a objeto SalidaDigitalView
    private ConfigDO configDO;
    private boolean intervalo;
    private long tiempoIntervalo=0;

    private int value;

    public OutputCView(Context context,String ref, int imageResource) {
        super(context);
        this.context=context;
        reference=ref;
        this.imageResouce=imageResource;
        value=0;

        textView=new TextView(context);
        textView.setText(reference);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setTextSize(9);
        textView.setGravity(Gravity.CENTER);

        imageView=new ImageView(context);
        imageView.setImageResource(imageResource);
        ViewGroup.LayoutParams tamImage=new ViewGroup.LayoutParams(
                getPixelesXdp(50), ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(tamImage);

        LayoutParams params=new LayoutParams(getPixelesXdp(55), getPixelesXdp(60));
        setLayoutParams(params);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);




        addView(textView);
        addView(imageView);
    }
    public int getPixelesXdp(int dp){
        return (int)(dp* getContext().getResources().getDisplayMetrics().density);
    }
    public int getImageResouce(){
        return imageResouce;
    }
    public String getReference(){
        return reference;
    }
    public void setReference(String references){
        this.reference=references;
        textView.setText(this.reference);
    }

    public void setUnido(boolean estado){
        unido=estado;
    }

    public void dibujarLinea(PointF inicio){
        DragAndDrop dragAndDrop=DragAndDrop.getInstance();
        dragAndDrop.setContext(this.getContext());
        ViewGroup containerLines=null;

        if(dragAndDrop!=null){
            containerLines=dragAndDrop.getLayoutLines();
        }

        //se comentan lineas para probsar patron singleton
        // ViewGroup viewGroup=(RelativeLayout)this.getParent();
        //ViewGroup contenedorPadre=(ConstraintLayout)viewGroup.getParent();
        //ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);



        if (lineaUnir!=null){
            containerLines.removeView(lineaUnir);
        }
        if (inputView!=null) {

            int width=inputView.getWidth()/2;
            int height=inputView.getHeight()/2;

            int width2=this.getWidth()/2;
            int height2=this.getHeight()/2;

            inicio.x=inicio.x+width2;
            inicio.y=inicio.y+height2;

            PointF entrada = new PointF(inputView.getX()+width, inputView.getY()+height);
            lineaUnir = new LineaUnir(containerLines.getContext(), inicio, entrada);
            //se comenta para probar patron singleton
            // lineaUnir = new LineaUnir(viewGroup.getContext(), inicio, entrada);

            containerLines.addView(lineaUnir);
        }
    }
    public LineaUnir getLineaUnir(){
        return lineaUnir;
    }
    public void setEntrada(InputCView inputView){
        this.inputView=inputView;
    }
    public InputCView getEntradaView(){
        return inputView;
    }
    public void eliminarSalida(){
        ViewGroup container=(RelativeLayout)this.getParent();
        ViewGroup contenedorPadre=(ConstraintLayout)container.getParent();
        ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);
        if (unido){

            containerLines.removeView(lineaUnir);
            inputView.eliminarUnaSalida(this);
            unido=false;
        }

    }
    public boolean isUnido(){
        return unido;
    }

    public ConfigDO getConfigDO() {
        return configDO;
    }

    public void setConfigDO(ConfigDO configDO) {
        this.configDO = configDO;
    }

    public boolean isIntervalo() {
        return intervalo;
    }

    public void setIntervalo(boolean intervalo) {
        this.intervalo = intervalo;
    }

    public long getTiempoIntervalo() {
        return tiempoIntervalo;
    }

    public void setTiempoIntervalo(long tiempoIntervalo) {
        this.tiempoIntervalo = tiempoIntervalo;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColorFilterImage(int color){
        imageView.setColorFilter(context.getResources().getColor(color));
    }
    public void clearColorFilter(){
        imageView.clearColorFilter();
    }
}
