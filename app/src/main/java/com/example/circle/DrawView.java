package com.example.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 碧桃鹦鹉 on 2016/11/14.
 */

public class DrawView extends View {
    private static final String TAG = "DrawView";
    int startX;
    int startY;
    int stopX;
    int stopY;
    Paint paint;
    int width;
    int height;

    int tempX;
    int tempY;
    Path mPath = new Path();
    Canvas cacheCanvas = new Canvas();
    private static final int COUNT = 10;

    private static final int NUMBER = 360 / COUNT;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = measureDimension(1080, widthMeasureSpec);
        height = measureDimension(1080, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        // 画笔颜色为红色
//        paint.setColor(Color.BLACK);
        paint.setColor(Color.RED);
        // 宽度5个像素
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw:startX=" + startX + " startY=" + startY + " stopX=" + stopX + " stopY=" + stopY);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
//        for (int i = 0; i < COUNT; i++) {
//            canvas.rotate(NUMBER, width / 2, height / 2);
//            canvas.drawLine(startX, startY, stopX, stopY, paint);
//            canvas.drawPath(mPath, paint);
//        }
        startX = tempX;
        startY = tempY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取手按下时的坐标
                startX = (int) event.getX();
                startY = (int) event.getY();

                mPath.moveTo(startX, startY);
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取手移动后的坐标
                stopX = (int) event.getX();
                stopY = (int) event.getY();
                // 在开始和结束坐标间画一条线
                Log.d(TAG, "onTouchEvent:startX=" + startX + " startY=" + startY + " stopX=" + stopX + " stopY=" + stopY);
                mPath.lineTo(stopX, stopY);
                tempX = (int) event.getX();
                tempY = (int) event.getY();
                invalidate();
//                startX = (int) event.getX();
//                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
//                stopX = (int) event.getX();
//                stopY = (int) event.getY();
                mPath.lineTo(stopX, stopY);
                break;
        }
//        invalidate();
        return true;
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
