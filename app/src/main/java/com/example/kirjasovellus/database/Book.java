package com.example.kirjasovellus.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Kirja-entiteetti tietokantaan.
 * Ominaisuuksina:
 * <ul>
 *     <li>id</li>
 *     <li>nimi</li>
 *     <li>sivumäärä</li>
 *     <li>lista genreId:itä</li>
 *     <li>lippu siitä, onko luettu</li>
 *     <li>päivämäärä</li>
 *     <li>milloin luettu</li>
 *     <li>muistiinpanot</li>
 * </ul>
 */
@Entity
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
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
    public Date finishDate;

    @ColumnInfo
    public String notes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    /**
     * TypeConvertereita tietokannan datalle
     */
    public static class Converters {
        @TypeConverter
        public static int[] fromString(String value) {
            String[] valueSeparated = value.split(",");
            int[] list = new int[valueSeparated.length];
            if (value.length() == 0) return new int[0];
            for (int i = 0; i < valueSeparated.length; i++) {
                list[i] = Integer.parseInt(valueSeparated[i]);
            }
            return list;
        }

        @TypeConverter
        public static String listToString(int[] list) {
            String str = "";
            for (int i = 0; i < list.length; i++){
                str += list[i] + ",";
            }
            return str;
        }

        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }
}
