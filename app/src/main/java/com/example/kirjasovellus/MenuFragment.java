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
import com.example.kirjasovellus.database.UserSettingsDao;
import com.example.kirjasovellus.tabBooks.BooksFragment;
import com.example.kirjasovellus.tabData.DataFragment;
import com.example.kirjasovellus.tabToday.TodayFragment;

import java.util.Locale;

/**
 * Fragment sovelluksen päävalikolle. Napit eri välilehdille. Huolehtii myös lokalisaation hakemisesta
 * tietokannasta.
 */
public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    /**
     * Asettaa valikon napeille toiminnallisuuden.
     * Napit vaihtavat Valikon alla näkyvää fragmentta seuraavasti:
     *     <ul>
     *         <li>Data - DataFragment</li>
     *         <li>Books - BooksFragment</li>
     *         <li>Today - TodayFragment</li>
     *         <li>Settings - SettingsFragment</li>
     *     </ul>
     * Latautuessaan asettaa myös sovelluksen kielen oikeaksi.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
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

                MainActivity.currentFragment = new DataFragment();
                System.out.println("LÖYTYI: " + MainActivity.currentFragment);

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

                MainActivity.currentFragment = new BooksFragment();
                System.out.println("LÖYTYI: " + MainActivity.currentFragment);

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

                MainActivity.currentFragment = new TodayFragment();
                System.out.println("LÖYTYI: " + MainActivity.currentFragment);

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

                MainActivity.currentFragment = new SettingsFragment();
                System.out.println("LÖYTYI: " + MainActivity.currentFragment);

                btnData.setEnabled(true);
                btnBooks.setEnabled(true);
                btnToday.setEnabled(true);
            }
        });

        // Lokalisaatio
        String lang = getLanguage();
        Configuration config = getContext().getResources().getConfiguration();
        if (!config.locale.getLanguage().equals(lang)) {
            changeLanguage(lang);
        }
    }

    /**
     * Metodi sovelluksen kielen hakemiselle.
     * Kieli haetaan tietokannasta. Jos kieltä ei ole, asetetaan englanti
     * @return kielen tunnus merkkijonossa
     */
    private String getLanguage() {
        UserSettingsDao usDao = MainActivity.bookDatabase.userSettingsDao();
        UserSettings us = usDao.getSettings();
        if (us == null) {
            us = new UserSettings();
            us.language = "en";
            us.settingsID = 0;
            usDao.insertAll(us);
            MainActivity.bookDatabase.userSettingsDao().insertAll(us);
        }
        return us.language;
    }

    /**
     * Vaihtaa sovelluksen kielen
     * @param lang kielen tunnus merkkijonossa
     */
    private void changeLanguage(String lang) {
        Configuration config = getContext().getResources().getConfiguration();
        Locale locale = new Locale(lang);

        Locale.setDefault(locale);

        config.locale = locale;
        getContext().getResources().updateConfiguration(config ,getContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getContext().startActivity(intent);
    }
}