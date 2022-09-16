package com.example.kirjasovellus.tabData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bar {

    private Rect rect;

    private Paint fill;
    private Paint border;

    private int height;
    private int width;
    private int base = 256;
    private int position;

    private String label;

    public Bar (int height, int width, int base, String label) {
        this.height = height;
        this.width = width;
        this.base = base;

        this.label = label;

        rect = new Rect(0, base - height, width, base);

        fill = new Paint();
        fill.setColor(Color.CYAN);
        fill.setStyle(Paint.Style.FILL);

        border = new Paint();
        border.setColor(Color.DKGRAY);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(3f);
    }

    public int getHeight() {
        return height;
    }

    public String getLabel() {
        return label;
    }

    public int getPosition() {
        return position;
    }

    public void setFillColor(int c) {
        fill.setColor(c);
    }

    public void drawFill(Canvas canvas) {
        canvas.drawRect(rect, fill);
    }

    public void drawBorder(Canvas canvas) {
        canvas.drawRect(rect, border);
    }

    public void setPosition(int pos, int translationX) {
        rect.left = pos * width + translationX;
        rect.right = pos * width + width + translationX;
        this.position = (rect.right - rect.left)/2 + rect.left;
    }
}
