package com.example.kirjasovellus;

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

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;

import java.util.ArrayList;

public class AddBookFragment extends Fragment {

    public AddBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvGenreList = getView().findViewById(R.id.rvGenreList);

        Genre[] datasetAllGenres = MainActivity.bookDatabase.genreDao().getAllGenres();
        ArrayList<Genre> selectedGenres = new ArrayList();

        rvGenreList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGenreList.setAdapter(new GenreListAdapter(datasetAllGenres, selectedGenres));

        EditText etBookTitle = getView().findViewById(R.id.etBookTitle);
        EditText etPageCount = getView().findViewById(R.id.etPageCount);

        Button btnSaveBook = getView().findViewById(R.id.btnSaveBook);

        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book b = new Book();
                b.BookId = 0;
                b.title = etBookTitle.getText().toString();
                b.pageCount = Integer.parseInt(etPageCount.getText().toString());
                b.finished = false;
                b.finishDate = null;

                int[] genreList = new int[selectedGenres.size()];
                int i = 0;
                System.out.println("SELECTED GENRES");
                for (Genre g: selectedGenres) {
                    System.out.println(g.name);
                    genreList[i] = g.genreId;
                    i++;
                }

                b.genreIds = genreList;

                MainActivity.bookDatabase.bookDao().insertAll(b);
            }
        });

    }

    private static class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

        Genre[] localDataset;
        ArrayList<Genre> selectedGenres;

        public GenreListAdapter(Genre[] dataset, ArrayList<Genre> selectedGenres) {
            localDataset = dataset;
            this.selectedGenres = selectedGenres;
        }

        public void updateDataset(Genre[] newDataset){
            localDataset = newDataset;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public GenreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_book_genre_list_item, parent, false);

            return new GenreListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GenreListAdapter.ViewHolder holder, int position) {
            Bundle arguments = new Bundle();
            arguments.putString("name", localDataset[position].name);
            arguments.putParcelable("genre", localDataset[position]);

            holder.icon.setText(localDataset[position].symbol);
            holder.name.setText(localDataset[position].name);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.button.getText().equals("🔴")){
                        holder.button.setText("🟢");
                        if (!selectedGenres.contains(localDataset[holder.getAdapterPosition()])) {
                            selectedGenres.add(localDataset[holder.getAdapterPosition()]);
                        }

                    }
                    else {
                        holder.button.setText("🔴");
                        selectedGenres.remove(localDataset[holder.getAdapterPosition()]);

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return localDataset.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView icon;
            public final TextView name;
            public final Button button;

            public ViewHolder(View itemView) {
                super(itemView);

                icon = (TextView) itemView.findViewById(R.id.tvGenreItemIcon);
                name = (TextView) itemView.findViewById(R.id.tvGenreItemName);
                button = (Button) itemView.findViewById(R.id.btnDeleteGenreItem);


            }
        }
    }
}