package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Dao käyttäjän asetuksille. Sisältää tietokantakyselyitä.
 */
@Dao
public interface UserSettingsDao {

    /**
     * Lisää tietokantaan annetut asetukset. Korvaa samalla settingsID:llä olevan asetuksen uudella.
     * Kuuluu käyttää vain id:llä 0
     * @param userSettings yksi tai useampi lisättävä asetus tietokantaan
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserSettings... userSettings);

    /**
     * Hakee tietokannasta UserSettings, jonka id = 0.
     * @return tallennetut asetukset
     */
    @Query("SELECT * FROM userSettings WHERE settingsID = 0")
    UserSettings getSettings();


}
