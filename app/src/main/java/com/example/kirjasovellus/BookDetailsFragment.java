package com.example.kirjasovellus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;

import java.util.Date;

public class BookDetailsFragment extends Fragment {

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Alustava kirja, jolla on oletustiedot
        Book book = new Book();
        book.title = "Title";
        book.genreIds = new int[0];
        book.pageCount = 0;
        book.finished = false;
        book.finishDate = null;

        // Ottaa argumenteista bundlen ja asettaa kirja-olioon
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            book  = (Book) bundle.getParcelable("book");
        }

        // Hakee kirjan genreId:illä genret ja asettaa Merkkijonoon
        String bookGenresString = "";
        for (int id : book.genreIds) {
            Genre g = MainActivity.bookDatabase.genreDao().getGenresOnId(id)[0];
            bookGenresString += g.symbol + " " + g.name + "\n";
        }

        // Tarkistaa, onko käyttäjä merkannut kirjan luetuksi. Jos on, niin näyttää päivämäärän
        String finishedString = "Finished?: ";
        if (book.finished) {
            Date finishDate = book.finishDate;
            String dateString = finishDate.toLocaleString();
            String[] dateSplit = dateString.split(" ");
            dateString = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];

            finishedString += "Yes, finished on " + dateString;
        } else {
            finishedString += "No";
        }

        // Layout komponentit
        TextView tvBookDetailsTitle = getView().findViewById(R.id.tvBookDetailsTitle);
        TextView tvBookDetailsGenres = getView().findViewById(R.id.tvBookDetailsGenres);
        TextView tvBookDetailsPageCount = getView().findViewById(R.id.tvBookDetailsPageCount);
        TextView tvBookDetailsfinished = getView().findViewById(R.id.tvBookDetailsFinished);

        tvBookDetailsTitle.setText(book.title);
        tvBookDetailsGenres.setText(bookGenresString);
        tvBookDetailsPageCount.setText("Page count: " + book.pageCount);
        tvBookDetailsfinished.setText(finishedString);

    }
}