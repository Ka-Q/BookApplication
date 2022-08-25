package com.example.kirjasovellus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class, Genre.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract GenreDao genreDao();
}
