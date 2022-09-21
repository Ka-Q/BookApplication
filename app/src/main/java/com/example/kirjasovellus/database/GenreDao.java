package com.example.kirjasovellus.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

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

    // LIVE QUERY
    @Query("SELECT * FROM genre ORDER BY name ASC")
    LiveData<List<Genre>> getAllGenresLive();

    @Query("DELETE FROM genre")
    int nukeTable();

    @Query("DELETE FROM genre WHERE genreId = :id")
    int deleteGenre(int id);
}
