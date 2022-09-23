package com.example.kirjasovellus.tabBooks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;
import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class BooksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    /**
     * Asettaa toiminnallisuuden kirjalistan tarkasteluun, kirjojen hakemiseen, järjestämiseen ja
     * hakutulosten rajaamiseen. Tämän näkymän haut tehdään pääosin erillisessä säikeesssä.
     * Kirjat näytetään recyclerView-komponentissa, jonka listan alkiota painettaessa avautuu
     * {@link BookDetailsFragment} kirjan tiedoilla.
     *
     * Näytöllä olevasta FAB-napista painamalla avautuu valikko, josta on pääsy {@link AddBookFragment},
     * {@link AddGenreFragment} ja {@link EditGenreFragment} -näkymiin.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // FragmentManager MainActivity:stä
        FragmentManager fragmentManager = MainActivity.fragmentManager;

        // Haetaan kirjojen ja genrejen datat tietokannasta omiin taulukoihinsa.
        //Book[] datasetBooks = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleAsc("");
        //Genre[] datasetGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        Book[] datasetBooks;
        Genre[] datasetGenres;

        final Book[][] dataset = {new Book[0]};
        final Genre[][] datasetG = {new Genre[0]};

        Observer bookObserver = new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                dataset[0] = new Book[books.size()];
                for (int i = 0; i < books.size(); i++) {
                    dataset[0][i] = books.get(i);
                }
                RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);
                rvBookList.setAdapter(new BookListAdapter(dataset[0], datasetG[0]));
            }
        };

        MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleAscLive("").observe(this.getViewLifecycleOwner(), bookObserver);

        datasetBooks = dataset[0];

        Observer genreObserver = new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                datasetG[0] = new Genre[genres.size()];
                for (int i = 0; i < genres.size(); i++) {
                    datasetG[0][i] = genres.get(i);
                }
                RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);
                rvBookList.setAdapter(new BookListAdapter(dataset[0], datasetG[0]));

                /* Kerää genrejen nimet merkkijonolistaan ja asettaa listan spinneriin. Spinnerin
                 * ensimmäinen valinta on varattu "kaikki"-valinnalle. */
                String genreNames[] = new String[datasetG[0].length + 1];
                genreNames[0] = "-All-";
                for (int i = 1; i < genreNames.length; i++) {
                    genreNames[i] =  datasetG[0][i-1].symbol + " " + datasetG[0][i-1].name;
                }
                Spinner genreSelect = getView().findViewById(R.id.genreSelect);
                ArrayAdapter<String> genreNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, genreNames);
                genreSelect.setAdapter(genreNameAdapter);
            }
        };

        MainActivity.bookDatabase.genreDao().getAllGenresLive().observe(this.getViewLifecycleOwner(), genreObserver);

        datasetGenres = datasetG[0];

        // Main Layout komponentit
        EditText searchBox = getView().findViewById(R.id.searchBox);
        Button btnSearch = getView().findViewById(R.id.btnSearch);
        Button btnMore = getView().findViewById(R.id.btnMore);
        ConstraintLayout moreContainer = getView().findViewById(R.id.moreContainer);
        Button btnSort = getView().findViewById(R.id.btnSort);
        Spinner genreSelect = getView().findViewById(R.id.genreSelect);
        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);

        // Kirjalistan koodi. 'BookListAdapter'ssa näytää tietokannassa olevat kirjat
        //rvBookList.setVisibility(View.GONE);
        rvBookList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookList.setAdapter(new BookListAdapter(datasetBooks, datasetGenres));

        /* FAB-menu Layout komponentit. FAB-menussa napit, joilla voidaan lisätä kirja tai genre
         * sekä muokata genrejä. */
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        ConstraintLayout fabMenuContainer = getView().findViewById(R.id.fabMenuContainer);
        Button btnAddBook = getView().findViewById(R.id.btnAddBook);
        Button btnAddGenre = getView().findViewById(R.id.btnAddGenre);
        Button btnEditGenre = getView().findViewById(R.id.btnEditGenre);

        /* Kerää genrejen nimet merkkijonolistaan ja asettaa listan spinneriin. Spinnerin
         * ensimmäinen valinta on varattu "kaikki"-valinnalle. */
        String genreNames[] = new String[1];
        genreNames[0] = "-All-";
        ArrayAdapter<String> genreNameAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, genreNames);
        genreSelect.setAdapter(genreNameAdapter);

        // FAB-onclick, jossa avataan FAB-menu
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabMenuContainer.getVisibility() ==View.GONE) {
                    fabMenuContainer.setVisibility(View.VISIBLE);
                } else {
                    fabMenuContainer.setVisibility(View.GONE);
                }
            }
        });

        /* Piilottaa FAB-komponentit listaa vieritettäessä, jotta FAB-menu ei peittäisi
         * kirjan tietoja. */
        rvBookList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    fab.setVisibility(View.INVISIBLE);
                    fabMenuContainer.setVisibility(View.GONE);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    fab.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        //Lisää kirja onclick, jossa vaihdetaan fragmenttia 'AddBookFragment'tiin.
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, AddBookFragment.class, null)
                        .addToBackStack("back")
                        .commit();
            }
        });

        // Lisää genre onclick, jossa vaihdetaan fragmenttia 'AddGenreFragment'tiin.
        btnAddGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, AddGenreFragment.class, null)
                        .addToBackStack("back")
                        .commit();
            }
        });

        // Muokkaa genreä onclick, jossa vaihdetaan fragmenttia 'EditGenreFragment'tiin.
        btnEditGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, EditGenreFragment.class, null)
                        .addToBackStack("back")
                        .commit();
            }
        });

        // Hakuasetusten toggle-nappi
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreContainer.getVisibility() == View.GONE) {
                    moreContainer.setVisibility(View.VISIBLE);
                    btnMore.setText("-");
                }
                else {
                    moreContainer.setVisibility(View.GONE);
                    btnMore.setText("+");
                }
            }
        });

        // tekee haun painettaessa softkeyboardin 'Done' tai 'Next' nappia
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                    search(getView(), dataset[0], bookObserver);
                }
                return false;
            }
        });

        // tekee haun painettaessa hakunappia
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view, dataset[0], bookObserver);
                view.invalidate();
            }
        });

        btnSort.setText("\uD83D\uDD24 ⬇️");
        /* Hakutulosten järjestyksen toggle-nappi. Järjestykselle on neljä vaihtoehtoa:
         * - Aakkosellinen nouseva
         * - Aakkosellinen laskeva
         * - Lisäyspäivä nouseva
         * - Lisäyspäivä laskeva
         */
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬇️")) {
                    btnSort.setText("\uD83D\uDD24 ⬆️");
                }
                 else if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬆️")) {
                    btnSort.setText("\uD83D\uDD5D ⬇️");
                }
                else if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬇️")) {
                    btnSort.setText("\uD83D\uDD5D ⬆️");
                }
                else if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬆️")) {
                    btnSort.setText("\uD83D\uDD24 ⬇️");
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /* Hakee kirjoja järjestetysti tietokannasta. Rajaa hakua kirjan nimen ja/tai valitun
     * genren mukaan. Päivittää haun jälkeen listan. */

    /**
     * Tekee asynkronisen haun tietokantaan.
     * Asettaa hakukriteerejä vastaavat tulokset viittauksena annettuun taulukkoon.
     * Hakukriteerit luetaan layout-komponenteista.
     * Kriteerejä ovat: Kirjan nimi ja genre.
     * Tulokset voidaan järjestää aakkosellisesti nousevasti ja laskevasti tai lisäysjärjestyksessä
     * nousevasti ja laskevasti.
     * Kun haku on tehty, päivitetään RecyclerView
     * @param view view
     * @param dataset viittaus listaan kirjoista
     * @param bookObserver viittaus Observer-olioon
     */
    private void search(View view, Book[] dataset, Observer<List<Book>> bookObserver){

        MainActivity.startLoading();

        // Piilottaa androidin softkeyboardin, jos näkyvissä.
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Layout komponentteja
        EditText searchBox = getView().findViewById(R.id.searchBox);
        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);
        Spinner genreSelect = getView().findViewById(R.id.genreSelect);
        Button btnSort = getView().findViewById(R.id.btnSort);

        // String text:ssä käyttäjän antamat hakusanat.
        String text = searchBox.getText().toString();

        // String genreName:ssa käyttäjän valitsema genre, jonka mukaan rajataan.
        String genreName = (String)genreSelect.getSelectedItem();
        int indexOfStart = 0;
        for (int i = 0; i < genreName.length(); i++) {
            if (genreName.charAt(i) == ' ') {
                indexOfStart = i + 1;
                break;
            }
        }
        genreName = genreName.substring(indexOfStart);

        /* Alla haetaan kirjoja tietokannasta annetuilla hakuparametreilla. Halusin toteuttaa
         * hakutulosten järjestämisen tietokannassa, mutta Room ei valitettavasti tue järjestyksen
         * antamista parametreilla. Tämän vuoksi jouduin tehdä jokaiselle järjestykselle oman
         * ORDER BY queryn. */

        // Queryt jos rajataan genrellä:
        if (genreSelect.getSelectedItemId() != 0) {
            Genre genre = MainActivity.bookDatabase.genreDao().getGenresOnName(genreName)[0];

            if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬇️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnTitleAscLive(text, genre.genreId).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬆️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnTitleDescLive(text, genre.genreId).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬇️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnIdAscLive(text, genre.genreId).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬆️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnIdDescLive(text, genre.genreId).observe(this.getViewLifecycleOwner(), bookObserver);
            }
        }
        // Queryt, jos ei rajata genrellä:
        else {
            if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬇️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleAscLive(text).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD24 ⬆️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleDescLive(text).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬇️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnIdAscLive(text).observe(this.getViewLifecycleOwner(), bookObserver);
            }
            if (btnSort.getText().toString().equals("\uD83D\uDD5D ⬆️")) {
                MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnIdDescLive(text).observe(this.getViewLifecycleOwner(), bookObserver);
            }
        }

        // Päivittää listan uusilla hakutuloksilla.
        BookListAdapter adapter = (BookListAdapter) rvBookList.getAdapter();
        adapter.updateDataset(dataset);
    }

    private static class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

        Book[] localDataset;
        Genre[] genreDataset;

        /**
         * Kontruktori BookListAdapter:ille.
         * Jos annettu kirjataulukko on tyhjä, asetetaan taulukkoon vain yksi kirja,
         * jonka nimenä on virheviesti.
         * @param dataset Näytettävät kirjat taulukossa
         * @param newGenreDataset tietokannan genret taulukossa
         */
        public BookListAdapter(Book[] dataset, Genre[] newGenreDataset) {
            localDataset = dataset;
            genreDataset = newGenreDataset;

            if (dataset.length == 0) {
                Book[] noDB = new Book[1];
                Book b = new Book();
                b.BookId = -1;
                b.title = MainActivity.getContext().getString(R.string.books_no_books_in_db);
                b.genreIds = new int[0];
                b.finished = false;
                noDB[0] = b;
                updateDataset(noDB);
            }
        }

        /**
         * Päivittää näkymän uudella datalla
         * @param newDataset lista uutta dataa
         */
        public void updateDataset(Book[] newDataset){
            localDataset = newDataset;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_item, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Asettaa toiminnallisuutta Recyclerview-komponenttiin.
         * Jokaisella listan kirjalla on nimi, lista genrejä ja tieto siitä, onko kirja luettu vai ei.
         * Kirjaa painamalla avautuu {@link BookDetailsFragment} kirjan tiedoilla.
         * @param holder holder
         * @param position position
         */
        @Override
        public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {

            // Bundelssa kaikki tiedot kirjasta
            Bundle arguments = new Bundle();
            arguments.putString("title", localDataset[position].title);
            arguments.putInt("id", localDataset[position].BookId);
            arguments.putParcelable("book", localDataset[position]);

            // Asetetaan kirjan arvoja layout-komponentteihin
            holder.title.setText(localDataset[position].title);

            // Hakee genret tietokannasta
            Genre[] allGenres = genreDataset;

            // String genreString on kirjan genrejen symbolit peräkkäin.
            String genreString = "";
            if (localDataset[position].genreIds.length == 0) {
                holder.genre.setText(genreString);
            } else {
                for (int id : localDataset[position].genreIds) {
                    for (Genre g : allGenres) {
                        if (id == g.genreId) {
                            genreString += g.symbol + " ";
                        }
                    }
                }
                if (genreString.length() != 0) genreString = genreString.substring(0, genreString.length() - 1);
                holder.genre.setText(genreString);
            }
            // Asettaa merkinnän siitä, onko kirja merkattu luetuksi vai ei.
            String read = "✖";
            if (localDataset[position].finished) {
                read = "✔";
            }
            holder.read.setText(read);

            // Käyttäjän painaessa kirjaa, avautuu kirjan laajemmat tiedot 'BookDetailsFragment':ssa.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (localDataset[holder.getAdapterPosition()].BookId == -1) {
                        FloatingActionButton fab = v.getRootView().findViewById(R.id.fab);
                        ConstraintLayout fabMenuContainer = v.getRootView().findViewById((R.id.fabMenuContainer));
                        fab.setVisibility(View.VISIBLE);
                        fabMenuContainer.setVisibility(View.GONE);
                    } else {
                        FragmentManager fragmentManager = MainActivity.fragmentManager;
                        fragmentManager.beginTransaction()
                                .replace(R.id.contentContainer, BookDetailsFragment.class, arguments)
                                .addToBackStack("back")
                                .commit();
                    }
                }
            });
            // Piilottaa FAB-menun komponentteja vierittäessä, jotta ne eivät peittäisi sisältöä.
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    FloatingActionButton fab = view.getRootView().findViewById(R.id.fab);
                    ConstraintLayout fabMenuContainer = view.getRootView().findViewById((R.id.fabMenuContainer));
                    fab.setVisibility(View.INVISIBLE);
                    fabMenuContainer.setVisibility(View.GONE);
                    return false;
                }
            });

            MainActivity.stopLoading();
        }

        @Override
        public int getItemCount() {
            return localDataset.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView title; // Nimi
            public final TextView genre; // Genre
            public final TextView read;  // Onko luettu?

            public ViewHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.tvTitleLabel);
                genre = (TextView) itemView.findViewById(R.id.tvGenreLabel);
                read = (TextView) itemView.findViewById(R.id.tvReadLabel);
            }
        }
    }
}