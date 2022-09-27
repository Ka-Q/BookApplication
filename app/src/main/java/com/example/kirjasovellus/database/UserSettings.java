package com.example.kirjasovellus.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 Käyttäjäasetus-entiteetti tietokantaan.
 * Ominaisuuksina:
 * <ul>
 *     <li>id</li>
 *     <li>kieli</li>
 * </ul>
 */
@Entity
public class UserSettings {

    @PrimaryKey
    public int settingsID;

    @ColumnInfo
    public String language;
}
