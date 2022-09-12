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


        etGenreSymbol.addTextChangedListener(new TextWatcher() {

            String oldSymbolString = "";
            String newSymbolString = "";


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldSymbolString = etGenreSymbol.getText().toString();
                System.out.println("VANHA " + oldSymbolString);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etGenreSymbol.getText().length() > 1) {
                    newSymbolString = etGenreSymbol.getText().toString();
                    System.out.println("UUSI " + newSymbolString);

                    String remaining = "";

                    if (newSymbolString.length() > oldSymbolString.length()) {
                        remaining = newSymbolString.substring(oldSymbolString.length());
                    }
                    else if (oldSymbolString.length() > newSymbolString.length()) {
                        remaining = newSymbolString.substring(newSymbolString.length() - symbolArray[1].length());

                    } else {
                        remaining = "";
                    }

                    System.out.println("JÄLJELLÄ " + remaining);
                    symbolArray[1] = symbolArray[0];
                    symbolArray[0] = remaining;

                } else {
                    symbolArray[0] = "";
                    symbolArray[1] = symbolArray[0];
                }
            }
        });

        EditText etGenreName = getView().findViewById(R.id.etGenreName);

        Button btnSaveGenre = getView().findViewById(R.id.btnSaveGenre);

        TextView tvErrorMsg = getView().findViewById(R.id.tvErrorMsg);

        btnSaveGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etGenreSymbol.getText().toString();
                System.out.println(str);
                tvErrorMsg.setText("");

                if (isEmojiOnly(symbolArray[0])) {
                    System.out.println("EMOJI");
                } else {
                    System.out.println("EI EMOJI");
                    //etGenreSymbol.setText("");
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

    private static Pattern r = Pattern.compile("^[\\s\n\r]*(?:(?:[\u00a9\u00ae\u203c\u2049\u2122\u2139\u2194-\u2199\u21a9-\u21aa\u231a-\u231b\u2328\u23cf\u23e9-\u23f3\u23f8-\u23fa\u24c2\u25aa-\u25ab\u25b6\u25c0\u25fb-\u25fe\u2600-\u2604\u260e\u2611\u2614-\u2615\u2618\u261d\u2620\u2622-\u2623\u2626\u262a\u262e-\u262f\u2638-\u263a\u2648-\u2653\u2660\u2663\u2665-\u2666\u2668\u267b\u267f\u2692-\u2694\u2696-\u2697\u2699\u269b-\u269c\u26a0-\u26a1\u26aa-\u26ab\u26b0-\u26b1\u26bd-\u26be\u26c4-\u26c5\u26c8\u26ce-\u26cf\u26d1\u26d3-\u26d4\u26e9-\u26ea\u26f0-\u26f5\u26f7-\u26fa\u26fd\u2702\u2705\u2708-\u270d\u270f\u2712\u2714\u2716\u271d\u2721\u2728\u2733-\u2734\u2744\u2747\u274c\u274e\u2753-\u2755\u2757\u2763-\u2764\u2795-\u2797\u27a1\u27b0\u27bf\u2934-\u2935\u2b05-\u2b07\u2b1b-\u2b1c\u2b50\u2b55\u3030\u303d\u3297\u3299\ud83c\udc04\ud83c\udccf\ud83c\udd70-\ud83c\udd71\ud83c\udd7e-\ud83c\udd7f\ud83c\udd8e\ud83c\udd91-\ud83c\udd9a\ud83c\ude01-\ud83c\ude02\ud83c\ude1a\ud83c\ude2f\ud83c\ude32-\ud83c\ude3a\ud83c\ude50-\ud83c\ude51\u200d\ud83c\udf00-\ud83d\uddff\ud83d\ude00-\ud83d\ude4f\ud83d\ude80-\ud83d\udeff\ud83e\udd00-\ud83e\uddff\udb40\udc20-\udb40\udc7f]|\u200d[\u2640\u2642]|[\ud83c\udde6-\ud83c\uddff]{2}|.[\u20e0\u20e3\ufe0f]+)+[\\s\n\r]*)+$");

    private static Boolean isEmojiOnly(String string) {
        System.out.println("ONKO " + string + " EMOJI?");
        if (string == null) return false;
        if (string.length() == 0) return false;
        if (r.matcher(string).find() || Character.UnicodeBlock.of(string.charAt(0)) == Character.UnicodeBlock.EMOTICONS) {
            return true;
        } return false;
    }

}