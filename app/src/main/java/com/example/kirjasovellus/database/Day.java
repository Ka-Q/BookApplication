package com.example.kirjasovellus.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Päivä-entiteetti tietokantaan. Ominaisuuksina:
 * <ul>
 *     <li>Päivämäärä</li>
 *     <li>tuntimäärä</li>
 * </ul>
 */
@Entity
public class Day implements Parcelable {

    @PrimaryKey()
    public Date date;

    @ColumnInfo
    public double hours;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
