package com.example.kirjasovellus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Book.class, Genre.class}, version = 1)
@TypeConverters({Book.Converters.class})
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract GenreDao genreDao();
}
