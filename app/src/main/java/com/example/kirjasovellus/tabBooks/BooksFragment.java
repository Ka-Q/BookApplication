package com.example.kirjasovellus.tabBooks;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class BooksFragment extends Fragment {

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentManager fragmentManager = MainActivity.fragmentManager;
        Book[] datasetBooks = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleAsc("");
        Genre[] datasetGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Main Layout komponentit
        EditText searchBox = getView().findViewById(R.id.searchBox);
        Button btnSearch = getView().findViewById(R.id.btnSearch);
        Button btnMore = getView().findViewById(R.id.btnMore);
        ConstraintLayout moreContainer = getView().findViewById(R.id.moreContainer);
        Button btnSort = getView().findViewById(R.id.btnSort);
        Spinner genreSelect = getView().findViewById(R.id.genreSelect);
        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);

        // Kirjalistan koodi. 'BookListAdapter'ssa näytää tietokannassa olevat kirjat
        rvBookList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookList.setAdapter(new BookListAdapter(datasetBooks));

        // FAB-menu Layout komponentit
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        ConstraintLayout fabMenuContainer = getView().findViewById(R.id.fabMenuContainer);
        Button btnAddBook = getView().findViewById(R.id.btnAddBook);
        Button btnAddGenre = getView().findViewById(R.id.btnAddGenre);
        Button btnEditGenre = getView().findViewById(R.id.btnEditGenre);

        // Kerää genrejen nimet Merkkijonolistaan ja asettaa listan spinneriin
        String genreNames[] = new String[datasetGenres.length + 1];
        genreNames[0] = "-All-";
        for (int i = 1; i < genreNames.length; i++) {
            genreNames[i] =  datasetGenres[i-1].symbol + " " + datasetGenres[i-1].name;
        }
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

        // Piilottaa FAB-komponentit listaa vieritettäessä
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
                    search(getView());
                }
                return false;
            }
        });

        // tekee haun painettaessa hakunappia
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
                view.invalidate();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("YEE");
                if (btnSort.getText().toString().equals("A-Z")) {
                    btnSort.setText("Z-A");
                }
                 else if (btnSort.getText().toString().equals("Z-A")) {
                    btnSort.setText("N-O");
                }
                else if (btnSort.getText().toString().equals("N-O")) {
                    btnSort.setText("O-N");
                }
                else if (btnSort.getText().toString().equals("O-N")) {
                    btnSort.setText("A-Z");
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    // Hakee kirjoja tietokannasta. Rajaa hakua kirjan nimen ja/tai valitun genren mukaan
    // Päivittää haun jälkeen listan
    private void search(View view){

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        EditText searchBox = getView().findViewById(R.id.searchBox);
        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);

        String text = searchBox.getText().toString();

        Spinner genreSelect = getView().findViewById(R.id.genreSelect);
        String genreName = (String)genreSelect.getSelectedItem();
        int indexOfStart = 0;
        for (int i = 0; i < genreName.length(); i++) {
            if (genreName.charAt(i) == ' ') {
                indexOfStart = i + 1;
                break;
            }
        }
        genreName = genreName.substring(indexOfStart);

        Book[] results = new Book[0];

        Button btnSort = getView().findViewById(R.id.btnSort);

        if (genreSelect.getSelectedItemId() != 0) {
            Genre genre = MainActivity.bookDatabase.genreDao().getGenresOnName(genreName)[0];

            if (btnSort.getText().toString().equals("A-Z")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnTitleAsc(text, genre.genreId);
            }
            if (btnSort.getText().toString().equals("Z-A")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnTitleDesc(text, genre.genreId);
            }
            if (btnSort.getText().toString().equals("N-O")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnIdDesc(text, genre.genreId);
            }
            if (btnSort.getText().toString().equals("O-N")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreIdSortedOnIdAsc(text, genre.genreId);
            }

        } else {

            if (btnSort.getText().toString().equals("A-Z")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleAsc(text);
            }
            if (btnSort.getText().toString().equals("Z-A")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnTitleDesc(text);
            }
            if (btnSort.getText().toString().equals("N-O")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnIdDesc(text);
            }
            if (btnSort.getText().toString().equals("O-N")) {
                results = MainActivity.bookDatabase.bookDao().getBookOnTitleSortedOnIdAsc(text);
            }
        }
        BookListAdapter adapter = (BookListAdapter) rvBookList.getAdapter();
        adapter.updateDataset(results);
    }

    private static class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

        Book[] localDataset;

        public BookListAdapter(Book[] dataset) {
            localDataset = dataset;
        }

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

        @Override
        public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {

            // Bundelssa kaikki tiedot kirjasta
            Bundle arguments = new Bundle();
            arguments.putString("title", localDataset[position].title);
            arguments.putInt("id", localDataset[position].BookId);
            arguments.putParcelable("book", localDataset[position]);

            // TITLE
            holder.title.setText(localDataset[position].title);

            // GENRE
            Genre[] allGenres = MainActivity.bookDatabase.genreDao().getAllGenres();
            String genreString = "";
            int index = 0;
            if (localDataset[position].genreIds.length == 0) {
                holder.genre.setText(genreString);
            } else {
                for (int id : localDataset[position].genreIds) {
                    for (Genre g : allGenres) {
                        if (id == g.genreId) {
                            genreString += g.symbol + " ";
                        }
                    }
                    index++;
                }
                genreString = genreString.substring(0, genreString.length() - 1);
                holder.genre.setText(genreString);
            }
            // FINISHED
            String read = "✖";
            if (localDataset[position].finished) {
                read = "✔";
            }
            holder.read.setText(read);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.fragmentManager;
                    fragmentManager.beginTransaction()
                            .replace(R.id.contentContainer, BookDetailsFragment.class, arguments)
                            .addToBackStack("back")
                            .commit();
                }
            });

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
        }

        @Override
        public int getItemCount() {
            return localDataset.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView title;
            public final TextView genre;
            public final TextView read;

            public ViewHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.tvTitleLabel);
                genre = (TextView) itemView.findViewById(R.id.tvGenreLabel);
                read = (TextView) itemView.findViewById(R.id.tvReadLabel);
            }
        }
    }
}