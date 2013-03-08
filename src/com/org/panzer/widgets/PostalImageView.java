/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.panzer.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *
 * @author panzer
 */
public class PostalImageView extends ImageView {

    public PostalImageView(Context context) {
        super(context);
    }

    public PostalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(10);
//        Paint pd = new Paint();
//        pd.setColor(Color.RED);
//        pd.setStrokeWidth(10);
////        canvas.drawLine(0, 0, 100, 100, p);
//        RectF rf = new RectF(-20, 0, 20, 20);
//        canvas.drawOval(rf, pd);
//        canvas.drawArc(rf, -90, 180, true, p);
        
        canvas.drawText("Darien", 0, 10, p);
        
        super.onDraw(canvas);
    }
}
