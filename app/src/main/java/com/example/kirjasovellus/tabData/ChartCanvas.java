package com.example.kirjasovellus.tabData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.database.Day;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class ChartCanvas extends View {

    private Day[] days;
    private Bar[] bars;
    private int lineCount = 24;

    private int barWidth = 100;
    private int base = 500;
    private int xTranslation = 200;

    private Rect borderRect;
    private Paint borderPaint;
    private Paint linePaint;

    private int scale;

    private double avgHours;

    public ChartCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        days = MainActivity.bookDatabase.dayDao().getAllDays();

        barWidth = 700/days.length;


        System.out.println("PÄIVIÄ: " + days.length);
        bars = new Bar[days.length];

        Double sum = 0.0;
        Double maxHours = 0.0;

        for (Day d : days) {
            System.out.println(d.date + "  -  " + d.hours);
            if (d.hours > maxHours) maxHours = d.hours;
            sum += d.hours;
        }
        avgHours = sum/days.length;

        int maxHoursInt = (int) Math.ceil(maxHours);

        lineCount = maxHoursInt;

        scale = (barWidth * days.length / maxHoursInt);

        int index = 0;
        for (Day d : days) {

            String label;
            String dateStr = d.date.toString();
            String[] dateSplit = dateStr.split(" ");
            label = dateSplit[0];

            Bar b = new Bar((int)(d.hours * scale), barWidth, barWidth * days.length, label);
            bars[index] = b;
            index++;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        xTranslation = canvas.getWidth()/2 - (barWidth * bars.length)/2;

        borderRect = new Rect(xTranslation, 0, xTranslation + barWidth * days.length, barWidth * days.length);
        borderPaint = new Paint();
        borderPaint.setColor(Color.DKGRAY);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5f);

        linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(new DashPathEffect(new float[] {20f,50f}, 0f));
        linePaint.setStrokeWidth(1f);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.LTGRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(1f);
        textPaint.setTextSize(30f);

        Paint labelPaint = new Paint();
        labelPaint.setColor(Color.LTGRAY);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setStrokeWidth(1f);
        labelPaint.setTextSize(30f);

        int pos = 0;
        for (Bar b : bars) {

            b.setPosition(pos, xTranslation);

            if (b.getHeight() > (avgHours + 1) * scale) {
                b.setFillColor(Color.rgb(100,100, 255));
            }
            else if (b.getHeight() < (avgHours - 1) * scale) {
                b.setFillColor(Color.rgb(220,220, 255));
            }
            else {
                b.setFillColor(Color.rgb(150,150, 255));
            }

            b.drawFill(canvas);
            pos++;

            canvas.drawText("" + b.getLabel().charAt(0), b.getPosition() - 10, borderRect.bottom + 30, labelPaint);

        }

        for (int i = 0; i < lineCount; i++) {
            Path p = new Path();

            p.moveTo(xTranslation, i * borderRect.bottom/lineCount);
            p.lineTo(borderRect.right, i * borderRect.bottom/lineCount);
            canvas.drawPath(p, linePaint);
            canvas.drawText((lineCount - i) + "h", xTranslation - 60, i * borderRect.bottom/lineCount, textPaint);
        }

        canvas.drawRect(borderRect, borderPaint);

        for (Bar b : bars) {
            b.drawBorder(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec , heightMeasureSpec);
        //System.out.println(heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, 750);
    }
}
