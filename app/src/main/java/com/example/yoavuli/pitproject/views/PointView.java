package com.example.yoavuli.pitproject.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.example.yoavuli.pitproject.Constants;
import com.example.yoavuli.pitproject.listeners.PositionListener;

import static com.example.yoavuli.pitproject.Constants.dimens.POINT_RADIUS;

public class PointView extends View {

    private final PositionListener mListener;
    private final Paint pointPaint = new Paint();

    public PointView(Context context, PositionListener listener) {
        super(context);
        pointPaint.setColor(Constants.Colors.POINT_COLOR);
        mListener = listener;
        setTouchListener();
    }

    /**
     * setter for the view TouchListener.
     * keeping track on the touch motionEvent and update the view position
     *
     */
    private void setTouchListener() {
        OnTouchListener mTouchListener = new OnTouchListener() {

            private float mLastTouchY;
            private float mLastTouchX;
            private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
            private int counter = 0; // counter to draw only a second  for better rendering

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int action = motionEvent.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        final int pointerIndex = motionEvent.getActionIndex();
                        final float x = motionEvent.getX(pointerIndex);
                        final float y = motionEvent.getY(pointerIndex);
                        mLastTouchX = x;
                        mLastTouchY = y;
                        mActivePointerId = motionEvent.getPointerId(0);
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        counter++;
                        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                        final float x = motionEvent.getX(pointerIndex);
                        final float y = motionEvent.getY(pointerIndex);
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        setPositions(dx, dy);
                        mLastTouchX = x;
                        mLastTouchY = y;

                        if (counter % 2 == 0) {
                            counter = 0;
                            mListener.onPositionUpdated();
                        }
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_POINTER_UP: {

                        final int pointerIndex = motionEvent.getActionIndex();
                        final int pointerId = motionEvent.getPointerId(pointerIndex);

                        if (pointerId == mActivePointerId) {
                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                            mLastTouchX = motionEvent.getX(newPointerIndex);
                            mLastTouchY = motionEvent.getY(newPointerIndex);
                            setX(mLastTouchX);
                            setY(mLastTouchY);
                            mActivePointerId = motionEvent.getPointerId(newPointerIndex);
                        }
                        break;
                    }
                }
                return true;
            }

            /**update the view position after validate the position is in the parent view
             * @param dx x delta
             * @param dy y delta
             * @return
             */
            private void setPositions(float dx, float dy) {

                // prevent the point to be drag outside of the parent layout on the X axis
                if (getX() + dx + 2 * POINT_RADIUS >= ((View) getParent()).getWidth())
                    setX(((View) getParent()).getWidth() - 2 * POINT_RADIUS);
                else if (getX() + dx < 0)
                    setX(0);
                else
                    setX(getX() + dx);


                // prevent the point to be drag outside of the parent layout on the Y axis
                if (getY() + dy + 2 * POINT_RADIUS > ((View) getParent()).getHeight())
                    setY(((View) getParent()).getHeight() - 2 * POINT_RADIUS);
                else if (getY() + dy < 0)
                    setY(0);
                else
                    setY(getY() + dy);

            }
        };
        setOnTouchListener(mTouchListener);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        int x = (int) getX();
        int y = (int) getY();
        super.layout(x, y, x + POINT_RADIUS * 2, y + POINT_RADIUS * 2);
        setX(x);
        setY(y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getX() + POINT_RADIUS, getY() + POINT_RADIUS, POINT_RADIUS, pointPaint);
    }

}
