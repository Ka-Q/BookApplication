package com.example.kirjasovellus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirjasovellus.database.UserSettings;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /**
     * Asettaa Asetus-näkymän napeille toiminnallisuuden.
     * Talletetaan tarvittaessa tietoa kielestä tietokantaan.
     * Napeille toiminnallisuudet testi-datan luontiin ja tietokannan tyhjentämiseen
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout-komponentit
        Button btnGenerateTestData = getView().findViewById(R.id.btnGenerateTestData);
        Button btnDeleteAllData = getView().findViewById(R.id.btnDeleteAllData);
        TextView tvLanguageSelection = getView().findViewById(R.id.tvLanguageSelection);

        // Jos kielenä on suomi, vaihtaa kielivalinnan kuvan
        Configuration config = getContext().getResources().getConfiguration();
        if (config.locale.getLanguage().equals("fi")) {
            tvLanguageSelection.setText("\uD83C\uDDEB\uD83C\uDDEE");
        }

        // Vaihtaa kielen painettaessa lippua. Tallentaa uuden kielen tietokantaan ja
        // päivittää sovelluksen käyttöliittymän
        tvLanguageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = "en";
                if (tvLanguageSelection.getText().toString().equals("\uD83C\uDDEC\uD83C\uDDE7")) {
                    lang = "fi";
                    tvLanguageSelection.setText("\uD83C\uDDEB\uD83C\uDDEE");
                }
                else if (tvLanguageSelection.getText().toString().equals("\uD83C\uDDEB\uD83C\uDDEE")) {
                    lang = "en";
                    tvLanguageSelection.setText("\uD83C\uDDEC\uD83C\uDDE7");
                }

                Locale locale = new Locale(lang);
                UserSettings us = new UserSettings();
                us.language = lang;
                us.settingsID = 0;

                Locale.setDefault(locale);
                Configuration config = getContext().getResources().getConfiguration();
                config.locale = locale;
                getContext().getResources().updateConfiguration(config ,getContext().getResources().getDisplayMetrics());

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);

                MainActivity.bookDatabase.userSettingsDao().insertAll(us);
            }
        });

        // Generoi käyttäjän hyväksyessä testidataa. Poistaa aiemman datan tietokannasta
        btnGenerateTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setMessage(R.string.settings_warning_test_data);
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.nukeAllData();
                        MainActivity.generateTestData();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });
        // Kysyy käyttäjältä varmistuksen. Hyväksyttäessä poistaa kaiken datan kieltä
        // lukuunottamatta tietokannasta.
        btnDeleteAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setMessage(R.string.settings_warning_delete_all);
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.nukeAllData();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });
    }
}