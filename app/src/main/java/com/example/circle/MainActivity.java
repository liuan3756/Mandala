package com.example.circle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 21;
    private static final String TAG = "MainActivity";
    private ImageView img_show;
    private ImageView img_colorpick;
    private ImageView img_settings;
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
        img_settings = (ImageView) findViewById(R.id.img_settings);
        img_settings.setOnClickListener(this);
        img_colorpick.setOnClickListener(this);
        doNext();

    }

    private void doNext() {
        initColorPicker();
        initsettingsDialog();
        img_show.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                width = img_show.getWidth();
                height = img_show.getHeight();
//                img_show.setX((width - height) / 2);
//                img_show.setLayoutParams(new RelativeLayout.LayoutParams(height, height));
//                width = height;
                init();
                img_show.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext();
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
        final int[] arr_count = {1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 18, 20, 24, 30, 36};
        View view = getLayoutInflater().inflate(R.layout.popupwindow_layout, null);
        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        final TextView tv_show_count = (TextView) view.findViewById(R.id.tv_show_count);
        Button btn_clear = (Button) view.findViewById(R.id.btn_yes);
        btn_clear.setOnClickListener(this);
        SeekBar sb_select_count = (SeekBar) view.findViewById(R.id.sb_select_count);
        sb_select_count.setMax(arr_count.length - 1);
        Log.d(TAG, "initsettingsDialog: sb progress" + sb_select_count.getProgress());
        sb_select_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: progress " + progress);
                tv_show_count.setText(arr_count[progress] + "");
                COUNT = arr_count[progress];
                NUMBER = 360 / COUNT;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                Toast.makeText(MainActivity.this, "保存中。。。", Toast.LENGTH_SHORT).show();
                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                baseBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.flush();
                fos.close();
                Toast.makeText(MainActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    });


    private void init() {
        int www = Math.max(width, height);
        baseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 创建一张画布
        canvas = new Canvas(baseBitmap);
        // 画布背景为白色
        canvas.drawColor(Color.WHITE);
        // 创建画笔
        paint = new Paint();
        // 宽度1个像素
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
                alertDialog.show();
                break;
            case R.id.btn_yes:
                Message msg = handler.obtainMessage();
                msg.what = 0;
                handler.sendMessage(msg);
//                RotateAnimation a = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                a.setDuration(500);
//                a.setRepeatCount(70);
//                LinearInterpolator lin = new LinearInterpolator();
//                a.setInterpolator(lin);
//                img_show.startAnimation(a);
                break;
        }
    }
}
