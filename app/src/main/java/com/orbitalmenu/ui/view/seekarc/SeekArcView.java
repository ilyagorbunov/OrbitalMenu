package com.orbitalmenu.ui.view.seekarc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;


public class SeekArcView extends View {

    private Paint mPaint;
    private Path mPath;

    private PointF mPointFstart; //start point
    private PointF mPointFset; //set point
    private PointF mPointFend; //ent point


    // TODO: 11.08.2016 delet unuse constructors
    public SeekArcView(Context context){
        super(context);
        init();
    }

    public SeekArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeekArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPointFstart = new PointF();
        mPointFset = new PointF();
        mPointFend = new PointF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mPointFstart.set(0, bottom - top - 30);
        mPointFset.set((right - left) / 2, -(bottom - top) / 4);
        mPointFend.set(right, bottom - top - 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(mPointFstart.x, mPointFstart.y);
        mPath.quadTo(mPointFset.x, mPointFset.y, mPointFend.x, mPointFend.y);
        canvas.drawPath(mPath, mPaint);
    }

}
