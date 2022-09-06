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
}
