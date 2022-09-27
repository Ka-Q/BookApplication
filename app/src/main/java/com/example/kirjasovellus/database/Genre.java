package com.example.kirjasovellus.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Genre-entiteetti tietokantaan. Ominaisuuksina:
 * <ul>
 *     <li>id</li>
 *     <li>nimi</li>
 *     <li>symboli</li>
 * </ul>
 */
@Entity
public class Genre implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int genreId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String symbol;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
