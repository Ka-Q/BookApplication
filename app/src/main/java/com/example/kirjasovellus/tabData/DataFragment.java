package com.example.kirjasovellus.tabData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout komponentit
        ChartCanvas chartCanvas = getView().findViewById(R.id.chartCanvas);
        Button btnToggleHoursPeriod = getView().findViewById(R.id.btnToggleHoursPeriod);
        TextView tvHoursStats = getView().findViewById(R.id.tvHoursStats);
        TextView tvSumStats = getView().findViewById(R.id.tvSumStats);
        TextView tvGenreStats = getView().findViewById(R.id.tvGenreStats);

        // Rakentaa kaavion viimeiseltä x päivältä. ALussa aina 7 päivää
        chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(7));

        // Analysoidaan dataa tietokannasta
        Book[] allBooks = MainActivity.bookDatabase.bookDao().getAllBooks();
        Genre[] allGenres = MainActivity.bookDatabase.genreDao().getAllGenres();
        Day[] allDays = MainActivity.bookDatabase.dayDao().getAllDays();

        ArrayList<Genre> favouriteGenres = new ArrayList();
        ArrayList<Book> booksInFavouriteGenres = new ArrayList<>();

        HashMap<Genre, ArrayList<Book>> genresWithBooks = new HashMap<>();
        for (Genre g : allGenres) {
            genresWithBooks.put(g, new ArrayList<Book>());
        }

        int finishedCount = 0;
        int pagesRead = 0;
        int biggestGenreSize = 0;

        for (Book b : allBooks) {
            if (b.finished) {
                finishedCount++;
                pagesRead += b.pageCount;

                for (int i : b.genreIds) {
                    Genre g = null;
                    for (Genre h : allGenres) {
                        if (h.genreId == i) {
                            g = h;
                            break;
                        }
                    }
                    genresWithBooks.get(g).add(b);
                }
            }
        }

        for (Genre g : genresWithBooks.keySet()) {
            if (genresWithBooks.get(g).size() > biggestGenreSize)
                biggestGenreSize = genresWithBooks.get(g).size();
        }

        for (Genre g : genresWithBooks.keySet()) {
            if (genresWithBooks.get(g).size() == biggestGenreSize) {
                favouriteGenres.add(g);
                for (Book b : genresWithBooks.get(g)) {
                    if (!booksInFavouriteGenres.contains(genresWithBooks.get(g))){
                        booksInFavouriteGenres.add(b);
                    }
                }
            }
        }

        double hours7 = 0;
        double hours14 = 0;
        double hours28 = 0;
        double hoursAll = 0;

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

        double avg7 = hours7/7;
        double avg14 = hours14/14;
        double avg28 = hours28/28;
        double avgAll = hours28/allDays.length;

        tvHoursStats.setText("Hours read past 7 days: " + String.format("%.2f", hours7) + "h (avg " + String.format("%.2f", avg7) +  "h/day)" + "\n" +
                "Hours read past 14 days: " + String.format("%.2f", hours14) + "h (avg " + String.format("%.2f", avg14) +  "h/day)"  +"\n" +
                "Hours read past 28 days: " + String.format("%.2f", hours28) + "h (avg " + String.format("%.2f", avg28) +  "h/day)"  +"\n" +
                "Hours read all-time: " + String.format("%.2f", hoursAll));




        tvSumStats.setText("You've added a total of " + allBooks.length
                + " books, of which you've finished " + finishedCount
                + ". You've read at least " + pagesRead + " pages in total.");

        tvGenreStats.setText("Your favourite genre(s): ");
        for (int i = 0; i < favouriteGenres.size(); i++) {
            tvGenreStats.setText(tvGenreStats.getText() + "\"" + favouriteGenres.get(i).name + "\" , ");
        }
        tvGenreStats.setText(tvGenreStats.getText() + "(" + booksInFavouriteGenres.size() + " out of " + finishedCount + " finished books)");


        // Käyttäjä voi valita 7, 14 ja 28 päivän kaavioista
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