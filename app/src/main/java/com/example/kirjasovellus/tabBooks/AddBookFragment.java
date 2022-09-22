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

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;
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

        // Hakee kaikki genret tietokannasta
        Genre[] datasetAllGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Ylläpitää käyttäjän asettamai genrejä kirjalle. Annetaan viittauksena 'GenreListAdapter'ille.
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
                }
                else {
                    tvErrorMsg.setText(R.string.add_book_error_data);
                }
            }
        });
    }

    private static class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

        Genre[] localDataset;
        ArrayList<Genre> selectedGenres;

        public GenreListAdapter(Genre[] dataset, ArrayList<Genre> selectedGenres) {
            localDataset = dataset;

            // Viittaus listaan, jossa ylläpidetään valittuja genrejä
            this.selectedGenres = selectedGenres;
        }

        // Päivittää näkymän
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

            // Asetetaan genren symboli ja nimi
            holder.icon.setText(localDataset[position].symbol);
            holder.name.setText(localDataset[position].name);

            /* Toggle-nappi päivittää parametrina saatuun viittaukseen listasta tietoa siitä,
             * mitkä genret on valittu.*/
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
            public final TextView icon; // Genren symboli
            public final TextView name; // Genren nimi
            public final Button button; // Toggel-nappi genrelle

            public ViewHolder(View itemView) {
                super(itemView);
                icon = (TextView) itemView.findViewById(R.id.tvGenreItemIcon);
                name = (TextView) itemView.findViewById(R.id.tvGenreItemName);
                button = (Button) itemView.findViewById(R.id.btnDeleteGenreItem);
            }
        }
    }
}