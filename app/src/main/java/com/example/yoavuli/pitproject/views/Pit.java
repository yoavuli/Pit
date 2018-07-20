package com.example.yoavuli.pitproject.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.PointF;
import android.util.AttributeSet;

import android.view.ViewGroup;

import com.example.yoavuli.pitproject.Constants;
import com.example.yoavuli.pitproject.listeners.PositionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.yoavuli.pitproject.Constants.dimens.POINT_RADIUS;

public class Pit extends ViewGroup implements PositionListener {

    private final Paint axisPaint = new Paint();
    private final Paint edgePaint = new Paint();
    private final Paint framePaint = new Paint();

    public Pit(Context context) {
        super(context);
        init();
    }

    public Pit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * initialize the view
     */
    private void init() {
        setWillNotDraw(false);
        axisPaint.setColor(Constants.Colors.AXIS_COLOR);
        axisPaint.setStrokeWidth(Constants.dimens.AXIS_WIDTH);
        edgePaint.setColor(Constants.Colors.EDGE_COLOR);
        framePaint.setColor(Constants.Colors.FRAME_COLOR);
    }

    /**
     * initialize the starting points with random coordinates on the Pit layout
     */
    private void initPoints() {

        for (int i = 0; i < Constants.General.NUM_OF_START_POINTS; i++) {
            PointView pointView = new PointView(getContext(), this);
            pointView.setX((float) Math.random() * (getWidth() - 2 * POINT_RADIUS));
            pointView.setY((float) Math.random() * (getHeight() - 2 * POINT_RADIUS));
            addView(pointView);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() == 0)
            initPoints(); //this is called here to get view height and width
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawAxis(canvas);
        drawFrame(canvas);
        drawPoints(canvas);
        drawEdges(canvas);
    }

    //draw the axis for the view

    /**
     * draw the axis for the view
     * @param canvas the canvas to draw on him
     */
    private void drawAxis(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, axisPaint); // draw  the X axis
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), axisPaint); // draw  the Y axis

    }

    /**
     * draw a thin frame for the view
     * @param canvas the canvas to draw on him
     */
    private void drawFrame(Canvas canvas) {
        canvas.drawLine(0, 0, 0, getHeight(), framePaint);
        canvas.drawLine(0, 0, getWidth(), 0, framePaint);
        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, framePaint);
        canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight(), framePaint);
    }

    /**
     * draw all the current points
     * @param canvas the canvas to draw on him
     */
    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).draw(canvas);
        }

    }

    /**
     * draw the edges between sorted points
     * @param canvas the canvas to draw on him
     */
    private void drawEdges(Canvas canvas) {
        ArrayList<PointF> pointArray = new ArrayList<>();
        sortPoints(pointArray);
        for (int i = 0; i < pointArray.size() - 1; i++) {
            canvas.drawLine(pointArray.get(i).x + POINT_RADIUS, pointArray.get(i).y + POINT_RADIUS
                    , pointArray.get(i + 1).x + POINT_RADIUS, pointArray.get(i + 1).y + POINT_RADIUS, edgePaint);
        }
    }

    //

    /**
     * create a ArrayList PointsF represent current points sorted by their x value (if x value even use y)
     * @param pointsArray an empty array to be filled with sorted points coordinates
     */
    private void sortPoints(ArrayList<PointF> pointsArray) {
        for (int i = 0; i < getChildCount(); i++) {
            pointsArray.add(new PointF(getChildAt(i).getX(), getChildAt(i).getY()));
        }
        Collections.sort(pointsArray, new Comparator<PointF>() {
            @Override
            public int compare(PointF point, PointF other) {
                if (point.x != other.x)
                    return (int) (point.x - other.x);
                else
                    return (int) (point.y - other.y);
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }



    /**
     * after a point position update call invalidate method to redraw the Pit
     */
    @Override
    public void onPositionUpdated() {
        invalidate();
    }

    /**
     * create a new point at axis origin (0,0)
     */
    public void addPoint() {
        PointView pointView = new PointView(getContext(), this);
        pointView.setX((getWidth() / 2 - POINT_RADIUS));
        pointView.setY((getHeight() / 2 - POINT_RADIUS));
        addView(pointView);
    }
}
