package com.example.kirjasovellus.tabBooks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Genre;

/**
 * Fragment genren lisäämiselle
 */
public class AddGenreFragment extends Fragment {

    // Ylläpitää tietoa siitä, mitkä ovat käyttäjän viimeisimmäksi syötetyt emojit.
    private String[] symbolArray = new String[]{null, ""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_genre, container, false);
    }

    /**
     * Asettaa toiminnallisuuden genren lisäys -näkymään.
     * Käyttäjä syöttää tiedot lisättävästä genrestä.
     *  Tarkistetaan  {@link EmojiWatcher} -luokan avulla symbolin oikeellisuus.
     * Uusi genre talletetaan tietokantaan.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout komponentit
        EditText etGenreSymbol = getView().findViewById(R.id.etGenreSymbol);
        EditText etGenreName = getView().findViewById(R.id.etGenreName);
        Button btnSaveGenre = getView().findViewById(R.id.btnSaveGenre);
        TextView tvErrorMsg = getView().findViewById(R.id.tvErrorMsg);

        // Emoji-valitsimen koodi. EmojiWatcher tarkistaa syötteen oikeellisuuden
        etGenreSymbol.addTextChangedListener(new EmojiWatcher(symbolArray, etGenreSymbol));

        // Tyhjentää Emoji-valitsimen ja asettaa valitun tyhjäksi kosketettaessa.
        etGenreSymbol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etGenreSymbol.setText("");
                symbolArray[0] = "";
                symbolArray[1] = symbolArray[0];
                return false;
            }
        });

        /* Tallennusnappi kerää syötetyn datan käyttöliittymästä ja tarkistaa, täyttääkö se vaatimukset.
         * Mikäli vaatimukset täyttyvät, tallentaa uuden genren tietokantaan. Muulloin näyttää käyttäjälle
         * virheviestin.*/
        btnSaveGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvErrorMsg.setText("");

                if (EmojiWatcher.isEmojiOnly(symbolArray[0])) {
                } else {
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + getString(R.string.add_genre_error_symbol));
                }
                if (etGenreName.getText().toString().length() < 1) {
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + getString(R.string.add_genre_error_name));
                }

                if(tvErrorMsg.getText().length() == 0) {
                    Genre g = new Genre();
                    g.genreId = 0;
                    g.name = etGenreName.getText().toString();
                    g.symbol = symbolArray[0];

                    MainActivity.bookDatabase.genreDao().insertAll(g);
                    MainActivity.fragmentManager.popBackStack();

                    Toast success = new Toast(getContext());
                    success.setText(R.string.saved_success);
                    success.show();
                }

            }
        });
    }
}