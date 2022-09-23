package com.example.kirjasovellus.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserSettings {

    @PrimaryKey
    public int settingsID;

    @ColumnInfo
    public String language;
}
