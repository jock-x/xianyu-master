package com.sun.bingo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.sun.bingo.R;

public class TabIconView extends View {

    private int mColor = 0xFF009688;
    private Bitmap mIconBitmap;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha;

    private Rect mIconRect;

    public TabIconView(Context context) {
        this(context, null);
    }

    public TabIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public TabIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconWithTextView);
        int index = typedArray.getIndexCount();

        for (int i = 0; i < index; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IconWithTextView_iwtv_icon:
                    BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.IconWithTextView_iwtv_color:
                    mColor = typedArray.getColor(attr, 0xFF009688);
                    break;
            }
        }

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom());

        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (iconWidth) / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);

        int alpha = (int) Math.ceil(255 * mAlpha);

        // 内存去准备mBitmap , setAlpha , 纯色 ，xfermode ， 图标
        setupTargetBitmap(alpha);

        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    // 在内存中绘制可变色的Icon
    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    // 重绘
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

}
