package com.example.kirjasovellus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kirjasovellus.database.Genre;

import java.util.regex.Pattern;

public class AddGenreFragment extends Fragment {

    private String[] symbolArray = new String[]{null, ""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etGenreSymbol = getView().findViewById(R.id.etGenreSymbol);

        etGenreSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etGenreSymbol.setText("");
                symbolArray[0] = "";
                symbolArray[1] = symbolArray[0];
            }
        });

        etGenreSymbol.addTextChangedListener(new EmojiWatcher(symbolArray, etGenreSymbol));

        EditText etGenreName = getView().findViewById(R.id.etGenreName);

        Button btnSaveGenre = getView().findViewById(R.id.btnSaveGenre);

        TextView tvErrorMsg = getView().findViewById(R.id.tvErrorMsg);

        btnSaveGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvErrorMsg.setText("");

                if (EmojiWatcher.isEmojiOnly(symbolArray[0])) {
                    System.out.println("EMOJI");
                } else {
                    System.out.println("EI EMOJI");
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + "Symbol is not a valid emoji. ");
                }
                if (etGenreName.getText().toString().length() < 1) {
                    System.out.println("Liian kyhyt nimi");
                    tvErrorMsg.setText(tvErrorMsg.getText().toString() + "Name is not valid.");
                }

                if(tvErrorMsg.getText().length() == 0) {
                    Genre g = new Genre();
                    g.genreId = 0;
                    g.name = etGenreName.getText().toString();
                    g.symbol = symbolArray[0];

                    MainActivity.bookDatabase.genreDao().insertAll(g);
                    MainActivity.fragmentManager.popBackStack();
                }

            }
        });
    }
}