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
import android.widget.Toast;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.*;
import com.example.kirjasovellus.database.Day;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment tänään-näkymälle
 */
public class TodayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    /**
     * Asettaa toiminnallisuuden today-näkymälle.
     * Käyttäjän syöte tarkistetaan ja talletetaan tietokantaan.
     * @param view view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Päivitetään Menu-napit
        View menuview = (View) getView().getParent().getParent();
        Button btnData = menuview.findViewById(R.id.btnData);
        Button btnBooks = menuview.findViewById(R.id.btnBooks);
        Button btnToday = menuview.findViewById(R.id.btnToday);
        btnData.setEnabled(true);
        btnBooks.setEnabled(true);
        btnToday.setEnabled(false);

        // Layout komponentit
        EditText etHours = getView().findViewById(R.id.etHours);
        Button btnSaveHours = getView().findViewById(R.id.btnSavehours);
        TextView tvTodayError = getView().findViewById(R.id.tvTodayError);

        // Tarkistaa käyttäjän syöttämän datan ja tallentaa tietokantaan.
        btnSaveHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double hours = 0.00;
                try {
                    hours = Double.parseDouble(etHours.getText().toString());
                } catch (Exception e) {
                    tvTodayError.setText(R.string.add_book_error_data);
                }

                if (hours > 24) {
                    tvTodayError.setText(R.string.hours_check);
                }
                else if (etHours.getText().length() == 0){
                    tvTodayError.setText(R.string.add_book_error_data);
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
                    Toast.makeText(getContext(), R.string.saved_success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        MainActivity.stopLoading();
    }
}