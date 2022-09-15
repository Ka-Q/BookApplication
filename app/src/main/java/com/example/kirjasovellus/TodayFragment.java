package com.example.kirjasovellus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kirjasovellus.database.Day;

import java.util.Calendar;
import java.util.Date;

public class TodayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout komponentit
        EditText etHours = getView().findViewById(R.id.etHours);
        Button btnSaveHours = getView().findViewById(R.id.btnSavehours);

        btnSaveHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double hours = Double.parseDouble(etHours.getText().toString());
                System.out.println("TUNNIT: " + hours);

                Date rawDate = Calendar.getInstance().getTime();

                // Ei oteta huomioon kellonaikaa
                Date date = new Date();
                date.setTime(0);
                date.setDate(rawDate.getDay());
                date.setMonth(rawDate.getMonth());
                date.setYear(rawDate.getYear());

                Day d = new Day();
                d.date = date;
                d.hours = hours;

                MainActivity.bookDatabase.dayDao().insertAll(d);
            }
        });
    }
}