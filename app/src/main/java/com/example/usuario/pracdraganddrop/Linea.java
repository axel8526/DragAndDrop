package com.example.usuario.pracdraganddrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class Linea extends View {

    private float y=50;
    private float x=50;
    String accion="acion";
    private Path path=new Path();


    public Linea(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas){
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.YELLOW);

        if (accion=="down")
            path.moveTo(x,y);
        if(accion=="move")
            path.lineTo(x,y);

        canvas.drawPath(path,paint);

    }

    public boolean onTouchEvent(MotionEvent e){

        x=e.getX();
        y=e.getY();

        if (e.getAction()==MotionEvent.ACTION_DOWN)accion="down";
        if (e.getAction()==MotionEvent.ACTION_MOVE)accion="move";

        invalidate();

        return true;

    }
}
