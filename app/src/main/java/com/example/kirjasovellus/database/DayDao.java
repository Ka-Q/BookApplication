package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Day... days);

    @Query("SELECT * FROM day ORDER BY date ASC")
    Day[] getAllDays();

    @Query("DELETE FROM day")
    int nukeTable();
}
