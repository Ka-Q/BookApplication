package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;

/**
 * Dao päivälle. Sisältää tietokantakyselyitä.
 */
@Dao
public interface DayDao {

    /**
     * Lisää tietokantaan annetut päivät. Korvaa samalla päivämäärällä olevan päivän uudella.
     * @param days yksi tai useampi lisättävä päivä tietokantaan
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Day... days);

    /**
     * Hakee kaikki päivät tietokannasta.
     * @return Lista päiviä
     */
    @Query("SELECT * FROM day ORDER BY date DESC")
    Day[] getAllDays();

    /**
     * Hakee päivämäärää vastaavat kirjat tietokannasta. Käytännössä palauttaa aina yhden päivän.
     * @param d Haettava päivämäärä
     * @return Päivämäärää vastaava päivä
     */
    @Query("SELECT * FROM day WHERE date = :d")
    Day getDayOnDate(Date d);

    /**
     * Hakee vimmeisimmät päivät tietokannasta määritellyllä aikajaksolla.
     * @param limit Aikajakson pituus
     * @return Lista päivä
     */
    @Query("SELECT * FROM day ORDER BY date DESC LIMIT :limit")
    Day[] getLastDays(int limit);

    /**
     * Poistaa kaiken datan taulusta
     * @return Poistettujen rivien lukumäärä
     */
    @Query("DELETE FROM day")
    int nukeTable();
}
