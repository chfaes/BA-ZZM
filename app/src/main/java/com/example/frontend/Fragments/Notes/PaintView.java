package com.example.frontend.Fragments.Notes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.frontend.Globals;

import java.util.ArrayList;

public class PaintView extends View {
    public static int BRUSH_SIZE = 8;
    public static final int DEFAULT_COLOR = Color.BLUE;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<PenPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
   // private Bitmap mBitmap;
    //private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int paintWidth = Globals.getInstance().getFragmentWidth()/4*3;
    private int paintHeight = Globals.getInstance().getFragmentHeight();
    private Bitmap mBitmap = Bitmap.createBitmap(paintWidth, paintHeight, Bitmap.Config.ARGB_8888);
    private Canvas mCanvas = new Canvas(mBitmap);

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void init() {
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);
        for (PenPath pp : paths) {
            mPaint.setColor(pp.color);
            mPaint.setStrokeWidth(pp.strokeWidth);
            mPaint.setMaskFilter(null);

            if (pp.emboss) {
                mPaint.setMaskFilter(mEmboss);
            } else if (pp.blur) {
                mPaint.setMaskFilter(mBlur);
            }
            mCanvas.drawPath(pp.path, mPaint);
        }
/*
        if(Globals.getInstance().getCurrentNoteChanged()){
            Bitmap mutableBitmap = Globals.getInstance().getCurrentNote().copy(Bitmap.Config.ARGB_8888, true);
            mCanvas.drawBitmap(mutableBitmap, 0, 0, mBitmapPaint);
            paths.clear();
            Globals.getInstance().setCurrentNoteChanged(false);
        }*/
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        PenPath pp = new PenPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(pp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp(){
        mPath.lineTo(mX,mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap drawing) {
        clear();
        Bitmap mutableBitmap = drawing.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas.drawBitmap(mutableBitmap,0,0, mBitmapPaint);
        //mCanvas.setBitmap(mutableBitmap);
        invalidate();
    }
}
