package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserSettings... userSettings);

    @Query("SELECT * FROM userSettings WHERE settingsID = 0")
    UserSettings getSettings();


}
