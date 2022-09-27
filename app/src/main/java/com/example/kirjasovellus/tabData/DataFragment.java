package com.example.kirjasovellus.tabData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Day;
import com.example.kirjasovellus.database.Genre;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    /**
     * Asettaa toiminnallisuuden Data-näkymälle.
     * Hakee tietokannasta dataa kirjoista, genreistä ja päivistä.
     * Muodostaa pylväskaavion lukutunneista/päivä määrätyltä aikaväliltä.
     * Analysoi dataa ja muotoilee sitä layout-komponentteihin luettavaan muotoon.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Päivitetään Menu-napit
        View menuview = (View) getView().getParent().getParent();
        Button btnData = menuview.findViewById(R.id.btnData);
        Button btnBooks = menuview.findViewById(R.id.btnBooks);
        Button btnToday = menuview.findViewById(R.id.btnToday);
        btnData.setEnabled(false);
        btnBooks.setEnabled(true);
        btnToday.setEnabled(true);

        // Layout komponentit
        ChartCanvas chartCanvas = getView().findViewById(R.id.chartCanvas);
        Button btnToggleHoursPeriod = getView().findViewById(R.id.btnToggleHoursPeriod);
        TextView tvHoursStats = getView().findViewById(R.id.tvHoursStats);
        TextView tvSumStats = getView().findViewById(R.id.tvSumStats);
        TextView tvGenreStats = getView().findViewById(R.id.tvGenreStats);

        // Rakentaa kaavion viimeiseltä x päivältä. ALussa aina 7 päivää
        chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(7));

        // Haetaan dataa tietokannasta taulukoihin
        Book[] allBooks = MainActivity.bookDatabase.bookDao().getAllBooks();
        Genre[] allGenres = MainActivity.bookDatabase.genreDao().getAllGenres();
        Day[] allDays = MainActivity.bookDatabase.dayDao().getAllDays();

        // Apulistoja datan analysointiin
        ArrayList<Genre> favouriteGenres = new ArrayList();
        ArrayList<Book> booksInFavouriteGenres = new ArrayList<>();

        // HashMap, jossa on avaimena Genre. Arvona on lista kirjoista, joille genre on asetettu.
        HashMap<Genre, ArrayList<Book>> genresWithBooks = new HashMap<>();
        for (Genre g : allGenres) {
            genresWithBooks.put(g, new ArrayList<Book>());
        }

        // Muuttujat pitävät yllä tietoa datasta.
        // Luetttujen kirjojen määrä, luettujen sivujen määrä ja suosituimman genren kirjojen määrä.
        int finishedCount = 0;
        int pagesRead = 0;
        int biggestGenreSize = 0;

        // Käy läpi kirjat ja kerää dataa ominaisuuksista.
        for (Book b : allBooks) {
            if (b.finished) {
                finishedCount++;
                pagesRead += b.pageCount;
                for (int i : b.genreIds) { // Käy läpi kirjan genret
                    Genre g = null;
                    for (Genre h : allGenres) {
                        if (h.genreId == i) {
                            g = h;
                            break;
                        }
                    }
                    genresWithBooks.get(g).add(b); // Lisää HashMappiin kirjan genren alle.
                }
            }
        }

        // Käy läpi genret ja ottaa talteen suurimman genren kirjojen määrän
        for (Genre g : genresWithBooks.keySet()) {
            if (genresWithBooks.get(g).size() > biggestGenreSize)
                biggestGenreSize = genresWithBooks.get(g).size();
        }

        // Käy uudelleen läpi genret, mutta tällä kertaa lisää listaan genret, joiden koko on
        // yhtä suuri, kuin suosituimman genren kirjojen määrä.
        for (Genre g : genresWithBooks.keySet()) {
            if (genresWithBooks.get(g).size() == biggestGenreSize) {
                favouriteGenres.add(g);
            }
        }

        // Käy läpi listan genreistä, joiden koko on yhtä suuri,
        // kuin suosituimman genren kirjojen määrä. Kerää listan kirjoista,
        // jotka kuuluvat suosituimpiin genreihin.
        for (Genre g : favouriteGenres) {
            ArrayList<Book> booksInGenre = genresWithBooks.get(g);
            for (Book b : booksInGenre) {
                if (booksInFavouriteGenres.size() == 0) {
                    booksInFavouriteGenres.add(b);
                } else {
                    // Tarkistetaan, onko kirjaa samalla id:llä listassa. Jos ei ole, lisätään listaan.
                    Boolean found = false;
                    for (Book c : booksInFavouriteGenres) {
                        if (c.BookId == b.BookId) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        booksInFavouriteGenres.add(b);
                    }
                }
            }
        }

        // Muuttujia, jotka pitävät yllä tietoa ajanjakson tuntien summasta
        double hours7 = 0;
        double hours14 = 0;
        double hours28 = 0;
        double hoursAll = 0;

        // Käy läpi päiviä ja kasvattaa tuntien summia
        for (int i = 0; i < allDays.length; i++) {
            if (i < 7) {
                hours7 += allDays[i].hours;
            }
            if (i < 14) {
                hours14 += allDays[i].hours;
            }
            if (i < 28) {
                hours28 += allDays[i].hours;
            }
            hoursAll += allDays[i].hours;
        }

        // Muuttujia, jotka pitävät yllä tietoa ajanjakson tuntien keskiarvosta
        double avg7 = hours7/7;
        double avg14 = hours14/14;
        double avg28 = hours28/28;


        // Kasataan dataa luettavaan muotoon layout-komponentteihin
        tvHoursStats.setText(getString(R.string.hours_past_seven_days) + String.format("%.2f", hours7) + getString(R.string.avg_format_start) + String.format("%.2f", avg7) +  getString(R.string.avg_format_end) + "\n" +
                getString(R.string.hours_past_fourteen_days) + String.format("%.2f", hours14) + getString(R.string.avg_format_start) + String.format("%.2f", avg14) +  getString(R.string.avg_format_end)  +"\n" +
                getString(R.string.hours_past_twenty_eight_days) + String.format("%.2f", hours28) + getString(R.string.avg_format_start) + String.format("%.2f", avg28) +  getString(R.string.avg_format_end)  +"\n" +
                getString(R.string.hours_all_time) + String.format("%.2f", hoursAll));

        tvSumStats.setText(getString(R.string.sum_data_1) + allBooks.length
                + getString(R.string.sum_data_2) + finishedCount + ". "
                + getString(R.string.sum_data_3) + pagesRead + getString(R.string.sum_data_4));

        tvGenreStats.setText(R.string.favourite_genres);
        for (int i = 0; i < favouriteGenres.size(); i++) {
            tvGenreStats.setText(tvGenreStats.getText() + "\"" + favouriteGenres.get(i).name + "\" , ");
        }
        tvGenreStats.setText(tvGenreStats.getText() + "(" + booksInFavouriteGenres.size() + getString(R.string.out_of) + finishedCount + getString(R.string.finished_books) + ")");

        // Nappi, jolla käyttäjä voi valita 7, 14 ja 28 päivän kaavioista
        btnToggleHoursPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = btnToggleHoursPeriod;
                if (b.getText().toString().equals("7")) {
                    b.setText("14");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(14));
                    chartCanvas.invalidate();
                }
                else if (b.getText().toString().equals("14")) {
                    b.setText("28");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(28));
                    chartCanvas.invalidate();
                }
                else if (b.getText().toString().equals("28")) {
                    b.setText("7");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(7));
                    chartCanvas.invalidate();
                }
            }
        });

        MainActivity.stopLoading();
    }
}