package com.example.circle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView img_show;
    private ImageView img_colorpick;
    private Bitmap baseBitmap;
    private Paint paint;
    private Canvas canvas;
    private int COUNT = 30;
    private int NUMBER = 360 / COUNT;

    private AlertDialog.Builder builder;
    AlertDialog alertDialog;
    int width = 0;
    int height = 0;

    int mSelectedColor = 0;
    ColorPickerDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_show = (ImageView) findViewById(R.id.img_show);
        img_colorpick = (ImageView) findViewById(R.id.img_colorpick);
        img_colorpick.setOnClickListener(this);
        initColorPicker();
        initsettingsDialog();
        img_show.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                width = img_show.getWidth();
                height = img_show.getHeight();
                init();
                img_show.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initColorPicker() {
        mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL);
        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                paint.setColor(color);
            }
        });
    }

    private void initsettingsDialog() {
        View view = getLayoutInflater().inflate(R.layout.popupwindow_layout, null);
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        EditText et_count = (EditText) view.findViewById(R.id.et_count);
    }


    private void setPaintColor(int color) {
        paint.setColor(color);
    }

    private void clearCanvas() {
        canvas.restore();
    }

    private void setCount(String c) {
        try {
            int count = Integer.valueOf(c);
            if (360 % count == 0) {
                COUNT = count;
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        baseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 创建一张画布
        canvas = new Canvas(baseBitmap);
        // 画布背景为灰色
//        canvas.drawColor(Color.GRAY);
        // 创建画笔
        paint = new Paint();
        // 画笔颜色为红色
        paint.setColor(Color.BLACK);
        // 宽度5个像素
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

//        canvas.drawCircle(width / 2, height / 2, width / 2 - 50, paint);
        paint.setColor(Color.RED);
        // 先将灰色背景画上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);
        img_show.setImageBitmap(baseBitmap);

        img_show.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取手按下时的坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取手移动后的坐标
                        int stopX = (int) event.getX();
                        int stopY = (int) event.getY();
                        // 在开始和结束坐标间画一条线

                        canvas.drawLine(startX, startY, stopX, stopY, paint);
                        for (int i = 1; i < COUNT; i++) {
                            canvas.rotate(NUMBER, width / 2, height / 2);
                            canvas.drawLine(startX, startY, stopX, stopY, paint);
                        }
                        // 实时更新开始坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        img_show.setImageBitmap(baseBitmap);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_colorpick:
                dialog.show(getFragmentManager(), "color_dialog_test");
                break;
            case R.id.img_settings:
                builder.show();
                break;
        }
    }
}
