package com.example.steven.drawtest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by steven on 2016/7/24.
 */

public class NewView extends ImageView {
    private String s;
    private Path path;
    private Paint paint,paint1;
    private float length;
    private Path drawPath=new Path();
    private float[]po=new float[2];
    private float lastnum=0;
    private float []points=new float[40000];
    private boolean open=false;
    float max=0;
    float min=Float.MAX_VALUE;
    private ArrayList<Path> list=new ArrayList<>();
private  float hei;
    public NewView(Context context) throws ParseException {
        this(context,null);
    }

    public NewView(Context context, AttributeSet attrs) throws ParseException {
        this(context, attrs,0);
    }

    public NewView(Context context, AttributeSet attrs, int defStyleAttr) throws ParseException {
        super(context, attrs, defStyleAttr);
        s=getResources().getString(R.string.git);
        path=new SvgPathParser().parsePath(s);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
paint1=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setStrokeWidth(2);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        paint1.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
       // drawPath.setFillType(Path.FillType.WINDING);
        final PathMeasure measure1=new PathMeasure(path,true);
        length=measure1.getLength();

  ValueAnimator animator=ValueAnimator.ofFloat(0,length);
animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float num= (float) animation.getAnimatedValue();
measure1.getSegment(0,num,drawPath,true);
measure1.getPosTan(num,po,null);
                if(po[1]>max) max=po[1];
                if(po[1]<min) min=po[1];
             //list.add(drawPath);
              //  lastnum=num;
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
open=true;
                hei=getMeasuredHeight();
                ValueAnimator animator1=ValueAnimator.ofFloat(max,min);
                animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        hei= (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animator1.setDuration(5000);
                animator1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
       animator.start();

        //measure1.getSegment(0,length/2,drawPath,true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(50,50);
        canvas.scale(2f,2f);
       // canvas.drawCircle(po[0],po[1],10,paint);
        canvas.drawPath(drawPath,paint);
//canvas.drawPoints((Float[]) list.toArray(new Float[list.size()]),paint);
        canvas.drawText(length+"",20,20,paint);
if(open)
{
 canvas.save();
    canvas.clipPath(path);
    canvas.drawRect(0,hei,getMeasuredWidth(),getMeasuredHeight(),paint1);
}

       // canvas.scale(1.2f,1.2f);
        // canvas.drawPoints(points,paint);

    }
}
