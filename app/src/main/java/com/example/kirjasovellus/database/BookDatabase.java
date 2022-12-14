package com.example.kirjasovellus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Sovelluksen tietokanta, joka ptää yllä tietoa kirjoista, genreistä,
 * päivistä ja käyttäjän asetuksista
 */
@Database(entities = {Book.class, Genre.class, Day.class, UserSettings.class}, version = 1)
@TypeConverters({Book.Converters.class})
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract GenreDao genreDao();
    public abstract DayDao dayDao();
    public abstract UserSettingsDao userSettingsDao();
}
