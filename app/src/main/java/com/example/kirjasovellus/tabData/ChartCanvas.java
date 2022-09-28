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
import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Day;

/**
 * Luokka, joka muodostaa pylväistä ({@link Bar}) pylväskaavion.
 */
public class ChartCanvas extends View {

    private Day[] days;
    private Bar[] bars;
    private int lineCount = 24;

    private int barWidth;
    private int base = 500;
    private int xTranslation = 200;

    private Rect borderRect;

    // Tyylejä kaavion komponenteille
    private Paint borderPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint textPaint  = new Paint();;
    private Paint labelPaint  = new Paint();;

    // Kaavion korkeuden skaala. Muuttuu dynaamisesti suurimman arvon mukaan.
    private int scale;

    // Aikavälin tuntien keskiarvo. Käytetään värittämään keskiarvoa alemmat, ylemmät ja
    // lähellä olevat päivät eri värisiksi.
    private double avgHours;

    public ChartCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Rakentaa kaavion annetusta listasta päiviä.
     * @param daysReversed Lista päivistä käänteisessä järjestyksessä
     */
    public void initialize(Day[] daysReversed) {

        // Asetetaan päivät taulukkoon kääänteisessä järjestyksessä
        this.days = new Day[daysReversed.length];
        for (int i = 0; i < daysReversed.length; i++) {
            this.days[i] = daysReversed[((daysReversed.length - 1) - i)];
        };

        // Asetetaan palkkien leveys niin, että ne mahtuvat leveydelle 700
        barWidth = 700/days.length;

        // Alustetaan taulukko palkeille
        bars = new Bar[days.length];

        Double sum = 0.0;
        Double maxHours = 0.0;

        // Käydään läpi päivien tunnit ja lasketaan niiden summa, keskiarvo ja maksimi
        for (Day d : days) {
            if (d.hours > maxHours) maxHours = d.hours;
            sum += d.hours;
        }
        avgHours = sum/days.length;

        // Kaavion vaakaviivojen määrä pyöristetään ylös, jotta suurin data ei leikkaannu pois
        int maxHoursInt = (int) Math.ceil(maxHours);
        if (maxHoursInt == 0) maxHoursInt = 1;
        lineCount = maxHoursInt;

        // Skaalaus-arvon avulla skaalataan kaavio niin, että korkein arvo osuu aina kaavion ylälaitaan
        scale = (barWidth * days.length / maxHoursInt);

        // Käydään läpi päivät ja muodostetaan niistä palkkeja. Palkit asetetaan omaan listaansa.
        // Palkille annetaan myös päivämäärästä päivän ensimmäinen kirjain labeliksi.
        int index = 0;
        for (Day d : days) {

            String label;
            String dateStr = d.date.toString();
            String[] dateSplit = dateStr.split(" ");
            label = dateSplit[0];

            if (label.equals("Mon")) label = getContext().getString(R.string.label_mon);
            if (label.equals("Tue")) label = getContext().getString(R.string.label_tue);
            if (label.equals("Wed")) label = getContext().getString(R.string.label_wed);
            if (label.equals("Thu")) label = getContext().getString(R.string.label_thu);
            if (label.equals("Fri")) label = getContext().getString(R.string.label_fri);
            if (label.equals("Sat")) label = getContext().getString(R.string.label_sat);
            if (label.equals("Sun")) label = getContext().getString(R.string.label_sun);

            Bar b = new Bar((int)(d.hours * scale), barWidth, barWidth * days.length, label);
            bars[index] = b;
            index++;
        }

    }

    /**
     * Piirtää kaavion pylväät, reunuksen ja labelit näytölle.
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Tyylimuotoilua kaavion eri komponenteille
        borderPaint.setColor(Color.DKGRAY);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5f);

        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(new DashPathEffect(new float[] {20f,50f}, 0f));
        linePaint.setStrokeWidth(1f);

        textPaint.setColor(Color.LTGRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(1f);
        textPaint.setTextSize(30f);

        labelPaint.setColor(Color.LTGRAY);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setStrokeWidth(1f);
        labelPaint.setTextSize(30f);

        // Muuttuja kaavion keskittämistä varten
        xTranslation = canvas.getWidth()/2 - (barWidth * bars.length)/2;

        // Asetetaan kaavion kehykselle koko ja sijainti
        borderRect = new Rect(xTranslation, 0, xTranslation + barWidth * days.length, barWidth * days.length);

        // Käydään läpi palkkien listaa ja asetetaan palkkien sijainnit ja väritys.
        // Pos-muuttuja on palkin "sijanumero" kaaviossa, xTranslation auttaa kaavion keskityksessä.
        // Palkit väritetään sen mukaan, kuinka ne eroavat keskimääräisestä palkin korkeudesta:
        // Keskiarvon arvoaluetta laajennetaan puolella tunnilla ylös ja alas. Kaikki alemmat värjätään
        // haaleammiksi ja ylemmät voimakkaammaksi.
        // Lopuksi piirretään vielä näkymään label palkin alapuolelle.
        int pos = 0;
        for (Bar b : bars) {
            b.setPosition(pos, xTranslation);

            if (b.getHeight() > (avgHours + 0.5) * scale) {
                b.setFillColor(getContext().getColor(R.color.blue_dark_main));
            } else if (b.getHeight() < (avgHours - 0.5) * scale) {
                b.setFillColor(getContext().getColor(R.color.blue_light_main));
            } else {
                b.setFillColor(getContext().getColor(R.color.blue_main));
            }

            b.drawFill(canvas);
            canvas.drawText("" + b.getLabel().charAt(0), b.getPosition() - 10, borderRect.bottom + 30, labelPaint);
            pos++;
        }

        // Käydään läpi vaakaviivojen listaa ja piirretään ne. Vaakaviivoja on tunnin välein.
        // Vaakaviivan eteen asetetaan label tuntimäärästä, jota se kuvastaa.
        for (int i = 0; i < lineCount; i++) {
            Path p = new Path();

            p.moveTo(xTranslation, i * borderRect.bottom/lineCount);
            p.lineTo(borderRect.right, i * borderRect.bottom/lineCount);
            canvas.drawPath(p, linePaint);
            canvas.drawText((lineCount - i) + "h", xTranslation - 60, i * borderRect.bottom/lineCount, textPaint);
        }

        // Piirretään kaavion reunus
        canvas.drawRect(borderRect, borderPaint);

        // Lopuksi piirretään palkkien reunukset, jotta ne näkyvät "ylimmällä tasolla".
        for (Bar b : bars) {
            b.drawBorder(canvas);
        }

        Rect rect1 = new Rect();
    }

    /**
     * Asettaa kankaalle korkeuden, jotta sitä voidaan käyttää paremmin layouteissa.
     * @param widthMeasureSpec widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, 750);
    }
}
