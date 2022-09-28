package com.example.kirjasovellus.tabBooks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Genre;

import java.util.ArrayList;

/**
 * Luokka Genre-togglelle RecyclerViewwissä. Genreillä on napit, juilla genren voi merkata valituksi
 * tai valitsemattomaksi. Tieto tallentuu viittauksena annettuun listaan.
 */
public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

    Genre[] localDataset;
    ArrayList<Genre> selectedGenres;

    /**
     * Konstruktori GenreListAdapter:ille.
     * @param dataset taulukko tietokannan genreistä
     * @param selectedGenres viittaus Listaan, jossa ylläpidetään tietoa valituista genreistä
     */
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
    public GenreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_book_genre_list_item, parent, false);

        return new GenreListAdapter.ViewHolder(view);
    }

    /**
     * Asettaa toiminnallisuutta Recyclerview-komponenttiin.
     * Jokaisella listan jäsenellä on symboli ja nimi.
     * Jäsenellä on myös toggle-nappi, jota painamalla tieto päivittyy listaan valituista genreistä.
     * @param holder holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull GenreListAdapter.ViewHolder holder, int position) {

        // Asetetaan genren tietoja layout-komponentteihin
        holder.icon.setText(localDataset[position].symbol);
        holder.name.setText(localDataset[position].name);
        holder.button.setText("🔴");

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

    /**
     * Metodi listan pituuden palautukseen
     * @return palauttaa listan koon.
     */
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
