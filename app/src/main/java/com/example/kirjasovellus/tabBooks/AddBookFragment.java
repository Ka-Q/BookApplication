package com.example.kirjasovellus.tabBooks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import java.util.ArrayList;

/**
 * Fragment kirjan lisäämiselle
 */
public class AddBookFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    /**
     * Asettaa toiminnallisuuden kirjan lisäys -näkymään.
     * Hakee tietokannasta genret ja näyttää new käyttäjälle.
     * Käyttäjä syöttää tiedot lisättävästä kirjasta.
     * Tiedot talletetaan tietokantaan.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hakee kaikki genret tietokannasta
        Genre[] datasetAllGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Ylläpitää käyttäjän asettamia genrejä kirjalle. Annetaan viittauksena 'GenreListAdapter'ille.
        ArrayList<Genre> selectedGenres = new ArrayList();

        // Layout komponentit
        RecyclerView rvGenreList = getView().findViewById(R.id.rvGenreList);
        EditText etBookTitle = getView().findViewById(R.id.etBookTitle);
        EditText etPageCount = getView().findViewById(R.id.etPageCount);
        Button btnSaveBook = getView().findViewById(R.id.btnSaveBook);
        TextView tvErrorMsg = getView().findViewById(R.id.tvErrorMsg);


        // Genrelistan koodi. 'GenreListAdapter'ssa näytää tietokannassa olevat genret
        rvGenreList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGenreList.setAdapter(new GenreListAdapter(datasetAllGenres, selectedGenres));

        /* Tallennusnappi kerää syötetyn datan käyttöliittymästä ja tarkistaa, täyttääkö se vaatimukset.
        * Mikäli vaatimukset täyttyvät, tallentaa uuden kirjan tietokantaan. Muulloin näyttää käyttäjälle
        * virheviestin.*/
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book b = new Book();
                b.BookId = 0;
                b.title = etBookTitle.getText().toString();
                try {
                    b.pageCount = Integer.parseInt(etPageCount.getText().toString());
                } catch (Exception e){
                    b.pageCount = -1;
                }
                b.finished = false;
                b.finishDate = null;

                int[] genreList = new int[selectedGenres.size()];
                int i = 0;
                for (Genre g: selectedGenres) {
                    genreList[i] = g.genreId;
                    i++;
                }

                b.genreIds = genreList;

                if (b.title.length() > 0 && b.pageCount >= 0){
                    MainActivity.bookDatabase.bookDao().insertAll(b);
                    tvErrorMsg.setText("");
                    FragmentManager fragmentManager = MainActivity.fragmentManager;
                    fragmentManager.popBackStack();
                    Toast success = new Toast(getContext());
                    success.setText(R.string.saved_success);
                    success.show();
                }
                else {
                    tvErrorMsg.setText(R.string.add_book_error_data);
                }
            }
        });
    }
}