package com.example.kirjasovellus.tabData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

// Luokka, joka muodostaa pylvään pylväsdiagrammiin (ChartCanvas)

/**
 * Luokka, joka muodostaa pylvään pylväsdiagrammiin ({@link ChartCanvas})
 */
public class Bar {

    // Pylvään muoto
    private Rect rect;

    // Tyylit täytölle ja reunalle
    private Paint fill;
    private Paint border;

    // Pylvään sijainnin ja koon ominaisuudet
    private int height;
    private int width;
    private int base;
    private int position;

    // Pylvään label. Käytännössä Pylvään kuvastaman viikonpäivän alkukirjain
    private String label;

    /**
     * Konstruktori pylväälle.
     * Rakennetaan pylväs ja tyylit.
     * @param height pylvään korkeus
     * @param width pylvään leveys
     * @param base pylvään alareunan y-sijainti
     * @param label pylvään label
     */
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

    /**
     * Metodi pylvään korkeuden palautukseen.
     * @return Paluttaa pylvään korkeuden.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Metodi pylvään labelin palautukseen.
     * @return Paluttaa pylvään labelin.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Metodi pylvään x-sijainnin palautukseen.
     * @return Palauttaa pylvään x-sijainnin.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Metodi pylvään täyttövärin asettamiseen.
     * @param c uusi väri
     */
    public void setFillColor(int c) {
        fill.setColor(c);
    }

    /**
     * Metodi pylvään täyttötyylin piirtämiseen.
     * @param canvas ChartCanvasin canvas
     */
    public void drawFill(Canvas canvas) {
        canvas.drawRect(rect, fill);
    }

    /**
     * Metodi pylvään reunatyylin piirtämiseen.
     * @param canvas ChartCanvasin canvas
     */
    public void drawBorder(Canvas canvas) {
        canvas.drawRect(rect, border);
    }

    /**
     * Metodi pylvään sijainnin asettamiseen.
     * @param pos pylvään järjestysnumero
     * @param translationX kaavion x-sijainti
     */
    public void setPosition(int pos, int translationX) {
        rect.left = pos * width + translationX;
        rect.right = pos * width + width + translationX;
        this.position = (rect.right - rect.left)/2 + rect.left;
    }
}
