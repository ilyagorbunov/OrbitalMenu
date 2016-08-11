package com.orbitalmenu.ui.view.orbit;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.math.BigDecimal;


public class OrbitView extends FrameLayout implements OrbitScroller.OnSmoothScrollListener{

    private PointF mPointFstart;
    private PointF mPointFset;
    private PointF mPointFend;
    private PointF mCircleCenter;
    private int mTop;
    private int mRight;
    private int mBottom;
    private int mLeft;
    private float currentX; // current scroller coordinates
    private final static float SEGMENT = 6f;
    private int currentSegment = 1;

    private OnProgressChangedListener mListener;

    private OrbitScroller mOrbitScroller;

    public OrbitView(Context context) {
        super(context);
        init();
    }

    public OrbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrbitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPointFstart = new PointF();
        mPointFset = new PointF();
        mPointFend = new PointF();
        mCircleCenter = new PointF();
    }

    public void setListener(OnProgressChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mOrbitScroller = (OrbitScroller) getChildAt(1);
        mOrbitScroller.setListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
        mPointFstart.set(0, bottom - top - 30);
        mPointFset.set((right - left) / 2, -(bottom - top) / 4);
        mPointFend.set(right, bottom - top - 30);
        currentX = (right - left) / SEGMENT;
        changeScrollerLayout(currentX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                float distance = (downX - mCircleCenter.x) * (downX - mCircleCenter.x) + (downY - mCircleCenter.y) * (downY - mCircleCenter.y);
                // caluculate distance to scroller center
                return !(distance - (mOrbitScroller.getMeasuredWidth() / 2 + 20) * (mOrbitScroller.getMeasuredWidth() / 2 + 20) > 0);
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                currentX = moveX; // change scroller coordinates
                changeScrollerLayout(currentX);
                currentSegment = getSegment(moveX);
                if (mListener != null) {
                    mListener.OnProgressChanged(currentSegment);
                }
                break;
            default:
                mOrbitScroller.smoothScrollSegment((int) currentX, (int) ((mRight - mLeft) / SEGMENT * currentSegment - currentX));
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Change scroller position
     *
     * @param currentX current coordinates
     */
    private void changeScrollerLayout(float currentX) {
        float t = (currentX / (mRight - mLeft));
        float x = (1 - t) * (1 - t) * mPointFstart.x + 2 * (t) * (1 - t) * mPointFset.x + t * t * mPointFend.x;
        float y = (1 - t) * (1 - t) * mPointFstart.y + 2 * (t) * (1 - t) * mPointFset.y + t * t * mPointFend.y;
        mCircleCenter.set(x, y);
        mOrbitScroller.layout((int) (mCircleCenter.x - mOrbitScroller.getMeasuredWidth() / 2), (int) (mCircleCenter.y - mOrbitScroller.getMeasuredWidth() / 2), (int) (mCircleCenter.x + mOrbitScroller.getMeasuredWidth() / 2), (int) (mCircleCenter.y + mOrbitScroller.getMeasuredWidth() / 2));
    }

    /**
     * Segment for auto-scrolling
     *
     * @param x coordinate
     * @return segment
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

    @Override
    public void onSmoothScroll(int currentX) {
        changeScrollerLayout(currentX);
    }


    public interface OnProgressChangedListener {
        void OnProgressChanged(int segment);
    }
    
    
}
