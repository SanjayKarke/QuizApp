package com.sanjay.quizapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomSliderView extends View {
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private float percentage;

    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);

        foregroundPaint = new Paint();
        foregroundPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        float foregroundWidth = percentage * width;

        canvas.drawRect(0, 0, (float) width, height, backgroundPaint);
        canvas.drawRect(0, 0, foregroundWidth, height, foregroundPaint);
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        invalidate();
    }
}