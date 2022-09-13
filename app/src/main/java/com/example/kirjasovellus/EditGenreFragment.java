package com.example.kirjasovellus;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.Genre;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class EditGenreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hakee tietokannasta olemassaolevat genret
        Genre[] datasetGenres = MainActivity.bookDatabase.genreDao().getAllGenres();

        // Layout-komponentit
        Spinner genreSelect = getView().findViewById(R.id.genreSelect);
        EditText etEmojiEdit = getView().findViewById(R.id.etEmojiEdit);
        Button btnSaveGenreEdit = getView().findViewById(R.id.btnSaveGenreEdit);
        TextView tvErrorMsg = getView().findViewById(R.id.tvErrorMsgEdit);
        EditText etGenreNameEdit = getView().findViewById(R.id.etGenreNameEdit);

        // Käyttäjän valitsema genre listassa, jotta muokattavissa kuuntelijoiden sisällä
        Genre[] selectedGenre = {null};

        // Kerää genrejen nimet Merkkijonolistaan ja asettaa listan spinneriin
        String genreNames[] = new String[datasetGenres.length + 1];
        genreNames[0] = "-None-";
        for (int i = 1; i < genreNames.length; i++) {
            genreNames[i] =  datasetGenres[i-1].symbol + " " + datasetGenres[i-1].name;
        }
        ArrayAdapter<String> genreNameAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, genreNames);
        genreSelect.setAdapter(genreNameAdapter);

        // Käyttäjän valitsema emoji listassa, jotta muokattavissa kuuntelijoiden sisällä
        String[] symbolArray = new String[]{null, ""};

        // Emoji-valitsimen koodi. EmojiWatcher tarkistaa syötteen oikeellisuuden
        etEmojiEdit.addTextChangedListener(new EmojiWatcher(symbolArray, etEmojiEdit));

        // Genre-spinnerin kuuntelija. Asettaa valitun genren 'selectedGenre'n ensimmäiseen alkioon
        genreSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos > 0) {
                    Genre g = datasetGenres[pos - 1];
                    selectedGenre[0] = g;

                    etGenreNameEdit.setText(g.name);
                    etEmojiEdit.getText().clear();
                    etEmojiEdit.getText().append(g.symbol);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Tyhjentää Emoji-valitsimen ja asettaa valitun tyhjäksi kosketettaessa.
        etEmojiEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etEmojiEdit.setText("");
                symbolArray[0] = "";
                symbolArray[1] = symbolArray[0];
                return false;
            }
        });

        // Alert dialog, jos genreä ei ole valittu muokattavaksi
        MaterialAlertDialogBuilder nullBuilder = new MaterialAlertDialogBuilder(getContext());
        nullBuilder.setMessage("You must choose a genre to make edits");
        nullBuilder.setPositiveButton("ok", (dialogInterface, i) -> {});

        // Tallennusnappi tallentaa muutokset genreen korvaamalla vanhan genren tietokannassa
        btnSaveGenreEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedGenre[0] == null) {
                    nullBuilder.show();
                    return;
                };
                tvErrorMsg.setText("");

                if (EmojiWatcher.isEmojiOnly(symbolArray[0])) {
                    System.out.println("EMOJI");
                } else {
                    System.out.println("EI EMOJI");
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + "Symbol is not a valid emoji. ");
                }
                if (etGenreNameEdit.getText().toString().length() < 1) {
                    System.out.println("Liian kyhyt nimi");
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + "Name is not valid.");
                }

                if (tvErrorMsg.getText().length() == 0) {
                    Genre g = new Genre();
                    g.genreId = selectedGenre[0].genreId;
                    g.name = etGenreNameEdit.getText().toString();
                    g.symbol = symbolArray[0];

                    MainActivity.bookDatabase.genreDao().insertAll(g);
                    MainActivity.fragmentManager.popBackStack();
                }
            }
        });

        // Peruutusnappi palaa takaisin edelliseen näkymään
        Button btnCancel = getView().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentManager.popBackStack();
            }
        });

        // Poista-nappi kysyy käyttäjältä, haluaako varmasti poistaa
        Button btnDeleteGenre = getView().findViewById(R.id.btnDeleteGenre);
        btnDeleteGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Genre selected = selectedGenre[0];
                if (selectedGenre[0] == null) {
                    nullBuilder.show();
                    return;
                };
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setMessage("Genre \"" + selected.name + "\" will be deleted and all books containing this genre will have it removed from them.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteGenre(selected);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    // Poistaa annetun genren tietokannasta. Käy läpi kirjat, joihin genre on merkattu, ja poistaa genren niiden listoista.
    private void deleteGenre(Genre selected) {
        Book[] booksWithGenre = MainActivity.bookDatabase.bookDao().getBookOnTitleAndGenreId("", selected.genreId);
        for (Book b : booksWithGenre) {
            int[] newIds = new int[b.genreIds.length - 1];
            int index = 0;
            for (int gId : b.genreIds) {
                if (gId != selected.genreId) {
                    newIds[index] = gId;
                    index++;
                }
            }
            b.genreIds = newIds;
        }
        MainActivity.bookDatabase.bookDao().insertAll(booksWithGenre);
        MainActivity.bookDatabase.genreDao().deleteGenre(selected.genreId);
        MainActivity.fragmentManager.popBackStack();
    }
}