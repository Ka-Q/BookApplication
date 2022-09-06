package com.example.kirjasovellus;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);

        Book[] dataset = MainActivity.bookDatabase.bookDao().getAllBooks();

        rvBookList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookList.setAdapter(new BookListAdapter(dataset));

        Button btnSearch = getView().findViewById(R.id.btnSearch);
        EditText searchBox = getView().findViewById(R.id.searchBox);

        Button btnMore = getView().findViewById(R.id.btnMore);
        ConstraintLayout moreContainer = getView().findViewById(R.id.moreContainer);

        Button btnAddBook = getView().findViewById(R.id.btnAddBook);
        Button btnAddGenre = getView().findViewById(R.id.btnAddGenre);

        FloatingActionButton fab = getView().findViewById(R.id.fab);
        ConstraintLayout fabMenuContainer = getView().findViewById(R.id.fabMenuContainer);

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

        //Add Book button action
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, AddBookFragment.class, null)
                        .addToBackStack("back")
                        .commit();
            }
        });

        //AddGenre button action
        btnAddGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, AddGenreFragment.class, null)
                        .addToBackStack("back")
                        .commit();
            }
        });

        //More-menu button action
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

        //Keyboard search action
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                    search(getView());
                }
                return false;
            }
        });

        //Search button action
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
                view.invalidate();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void search(View view){

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        EditText searchBox = getView().findViewById(R.id.searchBox);
        RecyclerView rvBookList = getView().findViewById(R.id.rvBookList);

        String text = searchBox.getText().toString();
        Book[] results = MainActivity.bookDatabase.bookDao().getBookOnTitle(text);
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
            Bundle arguments = new Bundle();
            arguments.putString("title", localDataset[position].title);
            arguments.putParcelable("book", localDataset[position]);

            //TITLE
            holder.title.setText(localDataset[position].title);

            //GENRE
            String genreString = "";
            int index = 0;
            for (int id : localDataset[position].genreIds) {
                Genre g = MainActivity.bookDatabase.genreDao().getGenresOnId(id)[0];
                genreString += g.symbol + " ";
                index++;
            }
            genreString = genreString.substring(0, genreString.length() - 1);
            holder.genre.setText(genreString);

            //FINISHED
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