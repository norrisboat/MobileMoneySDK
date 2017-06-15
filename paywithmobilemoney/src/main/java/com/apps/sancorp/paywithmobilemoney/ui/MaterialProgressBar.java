package com.apps.sancorp.paywithmobilemoney.ui;

/**
 * Created by norris on 2/19/16.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.apps.sancorp.paywithmobilemoney.R;


public class MaterialProgressBar extends View {

    private final CircularProgressDrawable mDrawable;
    private int progressBarColor = Color.parseColor("#000000");
    private int progressBarBorderWidth = 8;

    public MaterialProgressBar(Context context) {
        this(context, null);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

        mDrawable = new CircularProgressDrawable(progressBarColor, progressBarBorderWidth);
        mDrawable.setCallback(this);
        if (getVisibility() == VISIBLE) {
            mDrawable.start();
        }
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar,0, 0);
        try{
            progressBarBorderWidth = typedArray.getInt(R.styleable.MaterialProgressBar_progressBarBorderWidth,progressBarBorderWidth);
            progressBarColor = typedArray.getColor(R.styleable.MaterialProgressBar_progressBarColor,progressBarColor);
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mDrawable.draw(canvas);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mDrawable != null) {
            if (visibility == VISIBLE) {
                mDrawable.start();
            } else {
                mDrawable.stop();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawable.setBounds(0, 0, w, h);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mDrawable || super.verifyDrawable(who);
    }
}
