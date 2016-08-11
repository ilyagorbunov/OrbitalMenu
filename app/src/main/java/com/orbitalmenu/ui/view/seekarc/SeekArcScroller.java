package com.orbitalmenu.ui.view.seekarc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Scroller;

public class SeekArcScroller extends View{

    private Paint mPaint;
    private Scroller mScroller;
    private OnSmoothScrollListener mListener;


    public SeekArcScroller(Context context) {
        super(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mScroller = new Scroller(context);
    }

    public void setListener(OnSmoothScrollListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mListener != null) {
                mListener.onSmoothScroll(mScroller.getCurrX());
                postInvalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.reset();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2, mPaint);
    }


    /**
     * Auto-scrolling
     * @param start
     * @param distance
     */
    public void smoothScrollLevel(int start, int distance) {
        mScroller.startScroll(start, 0, distance, 0, 200);
        postInvalidate();
    }

    public interface OnSmoothScrollListener {
        void onSmoothScroll(int currentX);
    }
}



