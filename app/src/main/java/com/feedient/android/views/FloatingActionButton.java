package com.feedient.android.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class FloatingActionButton extends View {
    private Context context;
    private Paint mButtonPaint;
    private Paint mDrawablePaint;
    private Bitmap mBitmap;
    private int mScreenHeight;
    private float currentY;
    boolean mHidden;

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.mHidden = false;

        init(Color.WHITE);
    }

    @SuppressLint("NewApi")
    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public void setColor(int fabColor) {
        init(fabColor);
    }

    public void setDrawable(Drawable fabDrawable) {
        mBitmap = ((BitmapDrawable) fabDrawable).getBitmap();
        invalidate();
    }

    public void setDrawable(int drawable) {
        mBitmap = ((BitmapDrawable) getResources().getDrawable(drawable)).getBitmap();
        invalidate();
    }


    public void init(int fabColor) {
        setWillNotDraw(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(fabColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        invalidate();

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenHeight = size.y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setClickable(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
        canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2, (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setAlpha(1.0f);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setAlpha(0.6f);
        }

        return super.onTouchEvent(event);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void hideFab() {
        if (!mHidden) {
            currentY = getY();
            ObjectAnimator mHideAnimation = ObjectAnimator.ofFloat(this, "Y", mScreenHeight);
            mHideAnimation.setInterpolator(new AccelerateInterpolator());
            mHideAnimation.start();
            mHidden = true;
        }
    }

    public void showFab() {
        if (mHidden) {
            ObjectAnimator mShowAnimation = ObjectAnimator.ofFloat(this, "Y", currentY);
            mShowAnimation.setInterpolator(new DecelerateInterpolator());
            mShowAnimation.start();
            mHidden = false;
        }
    }
}
