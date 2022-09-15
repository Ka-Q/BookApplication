package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Genre... genres);

    @Query("SELECT * FROM genre WHERE genreId = :id")
    Genre[] getGenresOnId(int id);

    @Query("SELECT * FROM genre WHERE genreId = :id")
    Genre getGenreOnId(int id);

    @Query("SELECT * FROM genre WHERE name = :name")
    Genre[] getGenresOnName(String name);

    @Query("SELECT * FROM genre ORDER BY name ASC")
    Genre[] getAllGenres();

    @Query("DELETE FROM genre")
    int nukeTable();

    @Query("DELETE FROM genre WHERE genreId = :id")
    int deleteGenre(int id);
}
