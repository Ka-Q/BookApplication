package com.example.kirjasovellus.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Book implements Parcelable {

    @PrimaryKey
    public int BookId;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public int pageCount;

    @ColumnInfo
    public int[] genreIds;

    @ColumnInfo
    public boolean finished;

    @ColumnInfo
    public String finishDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
