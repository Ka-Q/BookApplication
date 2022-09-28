package com.example.kirjasovellus.tabBooks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment yksittäisen kirjan tarkasteluun
 */
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

    /**
     * Asettaa toiminnallisuuden kirjan tarkastelu -näkymään.
     * Saa argumenteissaan tiedot tarkasteltavasta kirjasta Bundlessa.
     * Jos tiedot ovat virheelliset, näytetään oletusdataa.
     * Listätään toiminnallisuus myös muistiinpanojen muokkaamiseen ja tallennukseen.
     * Lisätään toiminnallisuus myös kirjan muokkaus -napille, jota painettaessa avautuu
     * {@link EditBookFragment} kirjan tiedoilla.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Hakee genret tietokannasta
        Genre[] allGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Alustava kirja, jolla on oletustiedot
        System.out.println();
        Book book = new Book();
        book.title = "Title";
        book.genreIds = new int[0];
        book.pageCount = 0;
        book.finished = false;
        book.finishDate = null;

        // Ottaa argumenteista bundlen ja asettaa kirja-olioon
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            book = (Book) bundle.getParcelable("book");
        }

        // Hakee kirjan genreId:illä genret ja asettaa Merkkijonoon
        String bookGenresString = getString(R.string.details_genres);
        for (int id : book.genreIds) {
            for(Genre g : allGenres) {
                if (id == g.genreId) {
                    bookGenresString +=  "\n" + g.symbol + " " + g.name;
                }
            }
        }

        // Tarkistaa, onko käyttäjä merkannut kirjan luetuksi. Jos on, niin näyttää päivämäärän
        String finishedString = getString(R.string.details_finished);
        if (book.finished) {
            Date finishDate = book.finishDate;
            String dateString = finishDate.toLocaleString();
            String[] dateSplit = dateString.split(" ");
            dateString = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];

            finishedString += getString(R.string.details_finished_positive) + dateString;
        } else {
            finishedString += getString(R.string.details_finished_negative);
        }

        // Layout komponentit
        TextView tvBookDetailsTitle = getView().findViewById(R.id.tvBookDetailsTitle);
        TextView tvBookDetailsGenres = getView().findViewById(R.id.tvBookDetailsGenres);
        TextView tvBookDetailsPageCount = getView().findViewById(R.id.tvBookDetailsPageCount);
        TextView tvBookDetailsfinished = getView().findViewById(R.id.tvBookDetailsFinished);
        EditText etBookDetailsNotes = getView().findViewById(R.id.etBookDetailsNotes);
        Button btnSaveNotes = getView().findViewById(R.id.btnSaveNotes);
        Button btnMarkAsFinished = getView().findViewById(R.id.btnMarkAsFinished);
        Button btnEditBook = getView().findViewById(R.id.btnEditBook);

        // Asetetaan dataa Layout komponenteille kirjan perusteella
        tvBookDetailsTitle.setText(book.title);
        tvBookDetailsGenres.setText(bookGenresString);
        tvBookDetailsPageCount.setText(getString(R.string.details_page_count) + book.pageCount);
        tvBookDetailsfinished.setText(finishedString);
        if (!book.finished) {
            btnMarkAsFinished.setText(R.string.details_mark_as_finished);
        } else {
            btnMarkAsFinished.setText(R.string.details_mark_as_unfinished);
        }
        etBookDetailsNotes.setText(book.notes);

        // Final book muokattavaksi eventeissä
        Book finalBook = book;

        // Tallentaa muistiinpanot tietokantaan;
        btnSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalBook.notes = etBookDetailsNotes.getText().toString();
                MainActivity.bookDatabase.bookDao().insertAll(finalBook);
                Toast.makeText(getContext(), R.string.details_notes_saved, Toast.LENGTH_SHORT).show();
            }
        });

        /* Jos kirja merkattu luetuksi, merkaa lukemattomaksi ja toisinpäin. Asettaa nykyisen ajan
         * päivämääräksi. Tallentaa tiedot tietokantaan ja "päivittää" sivun.*/
        btnMarkAsFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!finalBook.finished) {
                    finalBook.finished = true;
                    finalBook.finishDate = Calendar.getInstance().getTime();
                } else {
                    finalBook.finished = false;
                    finalBook.finishDate = null;
                }
                MainActivity.bookDatabase.bookDao().insertAll(finalBook);

                /* Sivun "päivittäminen" tehty korvaamalla sivu kopiolla ja palaamalla heti takaisin vanhalle sivulle,
                 * jonne tiedot ovat päivittyneet. Näin sivu ei ole kahteen kertaan backstack:ssä.
                 * Outo toteutus, mutta toimii käytännössä halutulla tavalla.*/
                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, BookDetailsFragment.class, bundle)
                        .addToBackStack("back")
                        .commit();
                MainActivity.fragmentManager.popBackStack();
            }
        });

        // Avaa kirjan muokkausnäkymän
        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, EditBookFragment.class, bundle)
                        .addToBackStack("back")
                        .commit();
            }
        });

    }
}