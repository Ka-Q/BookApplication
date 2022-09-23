package com.example.kirjasovellus.tabData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

// Luokka, joka muodostaa pylvään pylväsdiagrammiin (ChartCanvas)
public class Bar {

    // Pylvään muoto
    private Rect rect;

    // Tyylit täytölle ja reunalle
    private Paint fill;
    private Paint border;

    // Tiedot pylvään kokeudesta, leveydestä, alareunan korkeudesta ja x-sijainnista
    private int height;
    private int width;
    private int base;
    private int position;

    // Pylvään label. Käytännössä Pylvään kuvastaman viikonpäivän alkukirjain
    private String label;

    public Bar (int height, int width, int base, String label) {
        this.height = height;
        this.width = width;
        this.base = base;

        this.label = label;

        rect = new Rect(0, base - height, width, base);

        // Tyylien määritys
        fill = new Paint();
        fill.setColor(Color.CYAN);
        fill.setStyle(Paint.Style.FILL);

        border = new Paint();
        border.setColor(Color.DKGRAY);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(3f);
    }

    // Paluttaa korkeuden
    public int getHeight() {
        return height;
    }

    // Palauttaa labelin
    public String getLabel() {
        return label;
    }

    // Palauttaa x-sijainnin
    public int getPosition() {
        return position;
    }

    // Asettaa uuden täyttövärin
    public void setFillColor(int c) {
        fill.setColor(c);
    }

    // Piirtää täytön
    public void drawFill(Canvas canvas) {
        canvas.drawRect(rect, fill);
    }

    // Piirtää reunuksen
    public void drawBorder(Canvas canvas) {
        canvas.drawRect(rect, border);
    }

    // Asettaa x-sijainnin
    public void setPosition(int pos, int translationX) {
        rect.left = pos * width + translationX;
        rect.right = pos * width + width + translationX;
        this.position = (rect.right - rect.left)/2 + rect.left;
    }
}
