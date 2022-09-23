package com.example.kirjasovellus;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kirjasovellus.database.UserSettings;
import com.example.kirjasovellus.tabBooks.BooksFragment;
import com.example.kirjasovellus.tabData.DataFragment;
import com.example.kirjasovellus.tabToday.TodayFragment;

import java.util.Locale;

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // FragmentManager MainActivity:sta
        FragmentManager fragmentManager = MainActivity.fragmentManager;

        Button btnData = view.findViewById(R.id.btnData);
        Button btnBooks = view.findViewById(R.id.btnBooks);
        Button btnToday = view.findViewById(R.id.btnToday);
        Button btnSettings = getView().findViewById(R.id.btnSettings);

        // Vaihtaa data-välilehdelle
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, DataFragment.class, null)
                        .addToBackStack("back")
                        .commit();

                btnData.setEnabled(false);
                btnBooks.setEnabled(true);
                btnToday.setEnabled(true);

                MainActivity.startLoading();
            }
        });

        // Vaihtaa kirjat-välilehdelle
        btnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, BooksFragment.class, null)
                        .addToBackStack("back")
                        .commit();

                btnData.setEnabled(true);
                btnBooks.setEnabled(false);
                btnToday.setEnabled(true);

                MainActivity.startLoading();
            }
        });

        // Vaihtaa tänään-välilehdelle
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, TodayFragment.class, null)
                        .addToBackStack("back")
                        .commit();

                btnData.setEnabled(true);
                btnBooks.setEnabled(true);
                btnToday.setEnabled(false);

                MainActivity.startLoading();
            }
        });

        // Vaihtaa asetus-näkymään
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, SettingsFragment.class, null)
                        .addToBackStack("back")
                        .commit();

                btnData.setEnabled(true);
                btnBooks.setEnabled(true);
                btnToday.setEnabled(true);
            }
        });

        // MenuFragment:in latautuessa, hakee käyttäjän asettaman kielen tietokannasta.
        // Jos kieli on eri, kuin nykyinen, vaihtaa sen.

        UserSettings us = MainActivity.bookDatabase.userSettingsDao().getSettings();
        String lang = us.language;

        if (!getContext().getResources().getConfiguration().locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            us.language = lang;
            us.settingsID = 0;

            Locale.setDefault(locale);
            Configuration config = getContext().getResources().getConfiguration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config ,getContext().getResources().getDisplayMetrics());

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getContext().startActivity(intent);
        }
    }
}