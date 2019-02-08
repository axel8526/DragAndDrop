package com.example.usuario.pracdraganddrop.componentes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import com.example.usuario.pracdraganddrop.R;

public class LineaUnir extends View {

    private PointF inicio,fin;
    public LineaUnir(Context context, PointF inicio, PointF fin) {
        super(context);
        this.inicio=inicio;
        this.fin=fin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(inicio.x,inicio.y,fin.x,fin.y,paint);
    }
}
