package com.example.kirjasovellus.tabBooks;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

/**
 * Fragment kirjan muokkaamiseen
 */
public class EditBookFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false);
    }

    /**
     * Asettaa toiminnallisuuden kirjan muokkaamiseen ja poistamiseen.
     * Tallenna-nappia painettaessa kirjan uudet tiedot luetaan käyttäjältä
     * ja päivitetään tietokantaan. Poista-nappia painetaessa kysytään varmistus käyttäjältä
     * ja poistetaan kirja tietokannasta.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
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
            book = (Book) bundle.getParcelable("book");
        }

        // Layout komponentit
        EditText etEditBookTitle = getView().findViewById(R.id.etEditBookTitle);
        RecyclerView rvEditBookGenres = getView().findViewById(R.id.rvEditBookGenres);
        EditText etEditBookPageCount = getView().findViewById(R.id.etEditBookPageCount);
        Button btnSaveEdits = getView().findViewById(R.id.btnSaveBookEdits);
        Button btnDeleteBook = getView().findViewById(R.id.btnDeleteBook);
        Button btnEditBookCancel = getView().findViewById(R.id.btnEditBookCancel);
        TextView tvEditError = getView().findViewById(R.id.tvEditError);

        // Hakee kaikki genret tietokannasta
        Genre[] datasetAllGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Ylläpitää käyttäjän asettamia genrejä kirjalle. Annetaan viittauksena 'GenreListAdapter'ille.
        ArrayList<Genre> selectedGenres = new ArrayList();
        for (int id : book.genreIds) {
            for (Genre g : datasetAllGenres) {
                if (id == g.genreId) {
                    selectedGenres.add(g);
                }
            }
        }

        // Asetetaan dataa Layout komponenteille kirjan perusteella
        etEditBookTitle.setText(book.title);
        etEditBookPageCount.setText(""  + book.pageCount);
        tvEditError.setText("");

        // Genrelistan koodi. 'GenreListAdapter'ssa näytää tietokannassa olevat genret
        rvEditBookGenres.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEditBookGenres.setAdapter(new GenreListAdapter(datasetAllGenres, selectedGenres));

        // Final book, jota voidaan muokata eventeissä.
        Book finalBook = book;

        // Tarkstaa käyttäjän syötteen. Jos syöte on oikeanlainen, tallentaa käyttäjän tekemät muutokset tietokantaan
        btnSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEditError.setText("");
                if (etEditBookTitle.getText().length() < 1) {
                    tvEditError.setText(tvEditError.getText() + getString(R.string.edit_book_error_title));
                }
                if (etEditBookPageCount.getText().length() == 0) {
                    tvEditError.setText(tvEditError.getText() + getString(R.string.edit_book_error_page_count));
                }

                if (tvEditError.getText().length() == 0) {
                    tvEditError.setText("");
                    finalBook.title = etEditBookTitle.getText().toString();
                    finalBook.pageCount = Integer.parseInt(etEditBookPageCount.getText().toString());

                    int[] genreIds = new int[selectedGenres.size()];
                    int index = 0;
                    for (Genre g : selectedGenres) {
                        genreIds[index] = g.genreId;
                        index++;
                    }
                    finalBook.genreIds = genreIds;
                    MainActivity.bookDatabase.bookDao().insertAll(finalBook);
                    MainActivity.fragmentManager.popBackStack();
                    MainActivity.fragmentManager.popBackStack();
                    MainActivity.fragmentManager.beginTransaction()
                            .replace(R.id.contentContainer, BookDetailsFragment.class, bundle)
                            .addToBackStack("back")
                            .commit();

                    Toast success = new Toast(getContext());
                    success.setText(R.string.saved_success);
                    success.show();
                }
            }
        });

        // Peruuta -nappi palaa edelliseen näkymään
        btnEditBookCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentManager.popBackStack();
            }
        });

        /* Poista-nappi kysyy, haluaako käyttäjä varmasti poistaa genren.
        /* Jos hyväksyy -> poistaa tietokannasta. */
        btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setMessage(R.string.edit_book_delete_book_warning);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.bookDatabase.bookDao().deleteBook(finalBook.BookId);
                        MainActivity.fragmentManager.popBackStack();
                        MainActivity.fragmentManager.popBackStack();

                        Toast success = new Toast(getContext());
                        success.setText(R.string.deleted_success);
                        success.show();
                    }
                });

                builder.setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });
    }
}