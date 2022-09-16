package com.example.kirjasovellus.tabData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kirjasovellus.MainActivity;
import com.example.kirjasovellus.R;

public class DataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Layout komponentit
        Button btnToggleHoursPeriod = getView().findViewById(R.id.btnToggleHoursPeriod);
        TextView tvHoursPeriod = getView().findViewById(R.id.tvHoursPeriod);
        ChartCanvas chartCanvas = getView().findViewById(R.id.chartCanvas);
        chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(7));

        btnToggleHoursPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = btnToggleHoursPeriod;
                if (b.getText().toString().equals("7")) {
                    b.setText("14");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(14));
                    chartCanvas.invalidate();
                }
                else if (b.getText().toString().equals("14")) {
                    b.setText("28");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(28));
                    chartCanvas.invalidate();
                }
                else if (b.getText().toString().equals("28")) {
                    b.setText("7");
                    chartCanvas.initialize(MainActivity.bookDatabase.dayDao().getLastDays(7));
                    chartCanvas.invalidate();
                }
            }
        });
    }
}