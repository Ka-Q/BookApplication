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

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;
import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class EditBookFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false);
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
        rvEditBookGenres.setAdapter(new EditBookFragment.GenreListAdapter(datasetAllGenres, selectedGenres));

        // Final book, jota voidaan muokata eventeissä.
        Book finalBook = book;

        // Tarkstaa käyttäjän syötteen. Jos syöte on oikeanlainen, tallentaa käyttäjän tekemät muutokset tietokantaan
        btnSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEditError.setText("");
                if (etEditBookTitle.getText().length() < 1) {
                    tvEditError.setText(tvEditError.getText() + "Title not valid. ");
                }
                if (etEditBookPageCount.getText().length() == 0) {
                    tvEditError.setText(tvEditError.getText() + "Page count not valid. ");
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
                builder.setMessage("Are you sure you want to delete this book and all data associated with it (including statistics) permanently. This action can not be reversed.");
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.bookDatabase.bookDao().deleteBook(finalBook.BookId);
                        MainActivity.fragmentManager.popBackStack();
                        MainActivity.fragmentManager.popBackStack();
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });
    }


    private static class GenreListAdapter extends RecyclerView.Adapter<EditBookFragment.GenreListAdapter.ViewHolder> {

        Genre[] localDataset;
        ArrayList<Genre> selectedGenres;

        public GenreListAdapter(Genre[] dataset, ArrayList<Genre> selectedGenres) {
            localDataset = dataset;

            // Viittaus listaan, jossa ylläpidetään valittuja genrejä
            this.selectedGenres = selectedGenres;
        }

        // Päivittää listan
        public void updateDataset(Genre[] newDataset){
            localDataset = newDataset;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EditBookFragment.GenreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_book_genre_list_item, parent, false);

            return new EditBookFragment.GenreListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EditBookFragment.GenreListAdapter.ViewHolder holder, int position) {

            // Asetetaan genren tietoja layout-komponentteihin
            holder.icon.setText(localDataset[position].symbol);
            holder.name.setText(localDataset[position].name);

            for (Genre g : selectedGenres) {
                if (g.genreId == localDataset[holder.getAdapterPosition()].genreId) {
                    holder.button.setText("🟢");
                }
            }

            // toggle-nappi, jolla käyttäjä voi päättää onko kirjalla genre vai ei
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
            public final TextView icon; // Symboli
            public final TextView name; // Nimi
            public final Button button; // Toggle-nappi

            public ViewHolder(View itemView) {
                super(itemView);
                icon = (TextView) itemView.findViewById(R.id.tvGenreItemIcon);
                name = (TextView) itemView.findViewById(R.id.tvGenreItemName);
                button = (Button) itemView.findViewById(R.id.btnDeleteGenreItem);
            }
        }
    }
}