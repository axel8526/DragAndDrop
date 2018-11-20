package com.example.usuario.pracdraganddrop.componentes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import com.example.usuario.pracdraganddrop.R;


public class LineaUnir extends View {
    private PointF comSalida;
    private PointF comEntrada;
    public LineaUnir(Context context, PointF sal, PointF ent) {
        super(context);
        comEntrada=ent;
        comSalida=sal;
    }
    public void onDraw(Canvas canvas){

        Paint paint=new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.color_linea));
        paint.setStrokeWidth(10);
        canvas.drawLine(comSalida.x,comSalida.y,comEntrada.x,comEntrada.y,paint);
    }
}
