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

import java.util.ArrayList;

public class InputCView extends LinearLayout {

    private String reference;
    private int imageResouce;
    private TextView textView;
    private ImageView imageView;
    private Context context;


    private ArrayList<OutputCView> outputsView;
    private boolean conectado;

    public InputCView(Context context,String ref, int imageResource) {
        super(context);
        this.context=context;
        reference=ref;
        this.imageResouce=imageResource;

        outputsView=new ArrayList<>();

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



    public ArrayList<OutputCView> getOutputsView() {
        return outputsView;
    }


    public void addOutput(OutputCView outputView) {
        outputsView.add(outputView);
        conectado = true;
        dibujarUltimaLinea();
    }

    public void dibujarLineas() {
        for (int i = 0; i < outputsView.size(); i++) {
            OutputCView salidaView = outputsView.get(i);
            salidaView.setEntrada(this);
            salidaView.setUnido(true);

            PointF posicionSalida = new PointF(salidaView.getX(), salidaView.getY());
            salidaView.dibujarLinea(posicionSalida);

        }
    }

    public void dibujarUltimaLinea() {
        OutputCView outputView = outputsView.get(outputsView.size() - 1);
        outputView.setEntrada(this);
        outputView.setUnido(true);

        PointF posicionSalida = new PointF(outputView.getX(), outputView.getY());
        outputView.dibujarLinea(posicionSalida);
    }

    public void eliminarInput() {
        ViewGroup container = (RelativeLayout) this.getParent();
        ViewGroup contenedorPadre=(ConstraintLayout)container.getParent();
        ViewGroup containerLines=(RelativeLayout)contenedorPadre.findViewById(R.id.layou_lines);

        if (conectado) {
            for (int i = 0; i < outputsView.size(); i++) {
                containerLines.removeView(outputsView.get(i).getLineaUnir());
                outputsView.get(i).setUnido(false);
                outputsView.get(i).setEntrada(null);
            }

        }
    }
    public void eliminarUnaSalida(OutputCView salidaView){
        int index=-1;
        for (int i=0; i<outputsView.size();i++){
            if (salidaView.getX()==outputsView.get(i).getX() && salidaView.getY()==outputsView.get(i).getY())index=i;
        }
        outputsView.remove(index);
        dibujarLineas();


    }

    public void setColorFilterImage(int color){
        imageView.setColorFilter(context.getResources().getColor(color));
    }
    public void clearColorFilter(){
        imageView.clearColorFilter();
    }
}
