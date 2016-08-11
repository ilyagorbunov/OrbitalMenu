package com.orbitalmenu.ui.view.orbits;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.math.BigDecimal;

public class OrbitViewTopDouble extends View {

    private Scroller mScroller;
    private Paint mPaint;
    private Path mPath;
    private PointF mPointFstart; // start point
    private PointF mPointFset; // set point
    private PointF mPointFend; // end point
    private PointF circleCenter;
    private int mTop;
    private int mRight;
    private int mBottom;
    private int mLeft;
    private float currentX; // current coordinates
    private int currentSegment; // current segment

    private final static float RADIUS = 160f; // Scroller radius
    private final static float SEGMENT = 20f; // Path segments

    public OrbitViewTopDouble(Context context) {
        super(context);
        init(context);
    }

    public OrbitViewTopDouble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrbitViewTopDouble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        mPointFstart = new PointF();
        mPointFset = new PointF();
        mPointFend = new PointF();
        circleCenter = new PointF();
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            currentX = mScroller.getCurrX();
            postInvalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
        mPointFstart.set(0, (bottom - top)/3);
        mPointFset.set((right - left) / 2, (bottom - top) / 8);
        mPointFend.set(right, (bottom - top)/4);
        currentX = (right - left) * 16 / SEGMENT;
        currentSegment = 10;
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

        // Take x-coordinate, calculate coordinates center, draw path
        float t = (currentX / (mRight - mLeft));
        float x = (1 - t) * (1 - t) * mPointFstart.x + 2 * (t) * (1 - t) * mPointFset.x + t * t * mPointFend.x;
        float y = (1 - t) * (1 - t) * mPointFstart.y + 2 * (t) * (1 - t) * mPointFset.y + t * t * mPointFend.y;
        circleCenter.set(x, y);
        mPaint.reset();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                float distance = (downX - circleCenter.x) * (downX - circleCenter.x) + (downY - circleCenter.y) * (downY - circleCenter.y);
                // calculate distance to the center of scroller
                return !(distance - (RADIUS + 20) * (RADIUS + 20) > 0);
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                currentX = moveX; // Set x-coordinate for scroller
                invalidate();
                currentSegment = getSegment(moveX);
                break;
            default:
                // auto-scrolling
                mScroller.startScroll((int) currentX, 0, (int) ((mRight - mLeft) / SEGMENT * currentSegment - currentX), 0, 200);
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Calculate segment
     *
     * @param x coordinate
     * @return
     */
    private int getSegment(float x) {
        float ratio = (x / (mRight - mLeft)) * SEGMENT;
        int result = new BigDecimal(ratio).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        if (result < 1) {
            result = 1;
        } else if (result > (SEGMENT - 1)) {
            result = (int) (SEGMENT - 1);
        }
        return result;
    }

}
