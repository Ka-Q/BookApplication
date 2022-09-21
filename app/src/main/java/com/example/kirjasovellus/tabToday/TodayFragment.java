package com.example.kirjasovellus.tabToday;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;
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
        TextView tvTodayError = getView().findViewById(R.id.tvTodayError);

        btnSaveHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double hours = Double.parseDouble(etHours.getText().toString());
                System.out.println("TUNNIT: " + hours);

                if (hours > 24) {
                    tvTodayError.setText("I refuse to believe you've read more than 24 hours i a day...");
                }
                else {
                    tvTodayError.setText("");


                    Date rawDate = Calendar.getInstance().getTime();
                    Date d = Calendar.getInstance().getTime();
                    d.setTime(0);
                    d.setYear(rawDate.getYear());
                    d.setMonth(rawDate.getMonth());
                    d.setDate(rawDate.getDate());

                    d.setHours(6);

                    System.out.println(d);
                    System.out.println(d.getTime());

                    Day day = new Day();
                    day.date = d;
                    day.hours = hours;

                    MainActivity.bookDatabase.dayDao().insertAll(day);
                }
            }
        });

        MainActivity.stopLoading();
    }
}