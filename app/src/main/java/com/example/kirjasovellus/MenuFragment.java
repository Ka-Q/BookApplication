package com.example.kirjasovellus;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kirjasovellus.tabBooks.BooksFragment;
import com.example.kirjasovellus.tabData.DataFragment;
import com.example.kirjasovellus.tabToday.TodayFragment;

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

        FragmentManager fragmentManager = MainActivity.fragmentManager;

        Button btnData = view.findViewById(R.id.btnData);
        Button btnBooks = view.findViewById(R.id.btnBooks);
        Button btnToday = view.findViewById(R.id.btnToday);
        Button btnSettings = getView().findViewById(R.id.btnSettings);

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
    }
}