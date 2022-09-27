package com.example.kirjasovellus.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Dao genrelle. Sisältää tietokantakyselyitä.
 */
@Dao
public interface GenreDao {

    /**
     * Lisää tietokantaan annetut genret. Korvaa samalla genreId:llä olevan päivän uudella.
     * @param genres yksi tai useampi lisättävä genre tietokantaan
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Genre... genres);

    /**
     * Hakee genreId:tä vastaavat genret tietokannasta. Käytännössä palauttaa aina yhden genren.
     * @param id Haettavan genren genreId
     * @return Lista genrejä
     */
    @Query("SELECT * FROM genre WHERE genreId = :id")
    Genre[] getGenresOnId(int id);

    /**
     * Hakee genreId:tä vastaavan genren tietokannasta. Palauttaa aina yhden genren.
     * @param id Haettavan genren genreId
     * @return GenreId:tä vastaava genre
     */
    @Query("SELECT * FROM genre WHERE genreId = :id")
    Genre getGenreOnId(int id);

    /**
     * Hakee nimeä vastaavat genret tietokannasta.
     * @param name Haettavien genrejen nimi
     * @return Lista genrejä
     */
    @Query("SELECT * FROM genre WHERE name = :name")
    Genre[] getGenresOnName(String name);

    /**
     * Hakee kaikki genret tietokannasta.
     * @return Lista genrejä
     */
    @Query("SELECT * FROM genre ORDER BY name ASC")
    Genre[] getAllGenres();

    // LIVE QUERY
    /**
     * Hakee kaikki genret tietokannasta.
     * Mahdollistaa asynkronisen haun.
     * @return Lista genrejä
     */
    @Query("SELECT * FROM genre ORDER BY name ASC")
    LiveData<List<Genre>> getAllGenresLive();

    /**
     * Poistaa kaiken datan taulusta
     * @return Poistettujen rivien lukumäärä
     */
    @Query("DELETE FROM genre")
    int nukeTable();

    /**
     * Poistaa taulusta genret, joiden genreId vastaa määritettyä id:tä. Käytännössä poistaa aina yhden genren
     * @param id Poistettavan/poistettavien genrejen genreId
     * @return poistettujen genrejen lukumäärä
     */
    @Query("DELETE FROM genre WHERE genreId = :id")
    int deleteGenre(int id);
}
