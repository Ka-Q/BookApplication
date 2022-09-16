package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;

@Dao
public interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Day... days);

    @Query("SELECT * FROM day ORDER BY date ASC")
    Day[] getAllDays();

    @Query("SELECT * FROM day WHERE date = :d")
    Day getDayOnDate(Date d);

    @Query("SELECT * FROM day ORDER BY date DESC LIMIT :limit")
    Day[] getLastDays(int limit);

    @Query("DELETE FROM day")
    int nukeTable();
}
